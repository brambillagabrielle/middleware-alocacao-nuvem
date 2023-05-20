package SistemaMiddleware.servidor;

import SistemaMiddleware.recursos.Instancia;
import SistemaMiddleware.recursos.Unidade;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Classe que contém os métodos para conexão com o cliente, através do
 * ServerSocket na porta 6666, e de todas as operações que podem ser realizadas
 * quanto às Unidades Computacionais e as Instâncias Operacionais. O recebimento
 * e envio de mensagem é realizado através da classe TrataConexão.
 * @author Estéfani e Gabrielle
 */
public class Servidor {

    private static ServerSocket serverSocket;
    private final ArrayList<Usuario> usuarios;
    private final ArrayList<Unidade> unidades;

    /**
     * Construtor que inicia a lista de usuários e unidades, além de adicionar
     * um usuário administrador (será o único admin do sistema) e um usuário
     * comum para testes
     */
    public Servidor() {
        usuarios = new ArrayList<>();
        unidades = new ArrayList<>();

        usuarios.add(new Usuario("admin", "admin123", "ADMIN"));
        usuarios.add(new Usuario("teste", "teste123", "COMUM"));
    }

    /**
     * Método que encontra um usuário na lista de usuários usando o nome passado
     * como parâmetro.
     * @param nome - String
     * @return Usuario
     */
    protected Usuario getUsuarioPorNome(String nome) {

        for (Usuario u : usuarios) {

            if (u.getUsuario().equals(nome)) {
                return u;
            }

        }

        return null;

    }

    /**
     * Método que cadastra um novo Usuário, verificando se o nome de usuário informado
     * para registro já não existe previamente.
     * @param usuario - Usuario
     * @return boolean
     */
    protected boolean cadastraUsuario(Usuario usuario) {

        if (getUsuarioPorNome(usuario.getUsuario()) == null) {

            this.usuarios.add(usuario);
            return true;

        }

        return false;

    }

    /**
     * Método que aloca uma nova Instância dentro de uma Unidade, verificando cada
     * uma das unidades da lista para caso tenha a capacidade livre que possa servir
     * para alocar a instância. Caso primeiro encontre uma unidade que não esteja ligada, 
     * guarda essa unidade como possível unidade para alocar e continua a procurar
     * por uma unidade ligada que tenha a capacidade livre desejada. Se não encontrar,
     * vai ligar e alocar a nova instância na unidade reservada.
     * @param quantidadeRecursos - int
     * @param usuario - Usuario
     * @return boolean
     */
    protected boolean alocarNovaInstancia(int quantidadeRecursos, Usuario usuario) {

        Unidade possivelUnidadeAlocacao = null;

        for (Unidade u : unidades) {

            if ((quantidadeRecursos + u.getCapacidadeAlocada())
                    <= u.getCapacidadeTotal()) {

                if (u.isLigada()) {

                    u.setCapacidadeAlocada(u.getCapacidadeAlocada() + quantidadeRecursos);
                    u.alocarInstancia(quantidadeRecursos, usuario, System.currentTimeMillis());

                    return true;

                } else if (possivelUnidadeAlocacao == null) {
                    possivelUnidadeAlocacao = u;
                }

            }

        }

        if (possivelUnidadeAlocacao != null) {

            possivelUnidadeAlocacao.setLigada(true);
            possivelUnidadeAlocacao.setTempoInicial(System.currentTimeMillis());
            possivelUnidadeAlocacao.setCapacidadeAlocada(
                    possivelUnidadeAlocacao.getCapacidadeAlocada() + quantidadeRecursos);
            
            possivelUnidadeAlocacao.alocarInstancia(
                    quantidadeRecursos, usuario, System.currentTimeMillis());

            return true;

        }

        return false;

    }

    /**
     * Método para desalocar instâncias através do ID passado pelo Cliente.
     * Se o ID foi encontrado entre as instâncias alocadas pelo usuário, vai remover a
     * Instância da Unidade e calcular o custo consumido pelo tempo e de acordo com a 
     * capacidade que foi reservada, sendo cobrado do cliente.
     * @param id - String
     * @param usuario - Usuario
     * @return boolean
     */
    protected boolean desalocarInstancia(String id, Usuario usuario) {

        for (Unidade u : unidades) {

            for (Instancia i : u.getInstancias()) {

                if (i.getId().equals(id) && i.getUsuario() == usuario) {

                    u.setCapacidadeAlocada(u.getCapacidadeAlocada() - i.getCapacidade());
                    u.getInstancias().remove(i);
                    
                    usuario.setCustoAlocacoes(usuario.getCustoAlocacoes() +
                            (((System.currentTimeMillis() - i.getTempoInicial()) 
                                    * i.getCapacidade()) / 10000));

                    if (u.getCapacidadeAlocada() == 0) {

                        u.setLigada(false);
                        u.setCustoFuncionamento(u.getCustoFuncionamento()
                            + ((System.currentTimeMillis() - u.getTempoInicial()) / 10000));

                    }

                    verificarMigracaoInstancias(u);

                    return true;

                }

            }

        }

        return false;

    }

    /**
     * Método que verifica se há uma possibilidade de migração entre unidades,
     * ocorredno toda vez que uma instância por desalocada de um Unidade de origem.
     * A possível unidade de destino precisa estar ligada e ter a capacidade livre
     * disponível para a alocação das instâncias da Unidade de origem. Caso a 
     * migração seja possível, o custo de ter mantido a instância ligada pelo 
     * provedor é calculada.
     * @param origem - Unidade
     */
    protected void verificarMigracaoInstancias(Unidade origem) {

        double capacidadeMigrada = 1;

        for (Unidade u : unidades) {

            if (u.isLigada() && u != origem &&
                    (u.getCapacidadeTotal() - u.getCapacidadeAlocada()) 
                    >= origem.getCapacidadeAlocada()) {
 
                Unidade destino = u;

                for (Instancia i : origem.getInstancias()) {

                    destino.alocarInstancia(i.getCapacidade(), i.getUsuario(), 
                            i.getTempoInicial());
                    destino.setCapacidadeAlocada(destino.getCapacidadeAlocada() + i.getCapacidade());
                    capacidadeMigrada = i.getCapacidade();

                }

                origem.setCapacidadeAlocada(0);
                origem.getInstancias().clear();
                origem.setLigada(false);
                u.setCustoFuncionamento(u.getCustoFuncionamento()
                        + (((System.currentTimeMillis() - u.getTempoInicial()) + 
                                ((20 / 100) * capacidadeMigrada)) / 10000));

            }

        }

    }

    /**
     * Método para mostrar todas as instâncias alocadas por um usuário, mostrando
     * também o custo atual calculado durante a desalocação das instâncias.
     * @param usuario - Usuario
     * @return String
     */
    protected String mostrarInstanciasUsuario(Usuario usuario) {
        
        DecimalFormat fmt = new DecimalFormat("0.00");

        String resposta = "Custo total da alocação das instâncias: R$ " + fmt.format(
                usuario.getCustoAlocacoes());

        for (Unidade u : unidades) {

            for (Instancia i : u.getInstancias()) {

                if (i.getUsuario() == usuario) {
                    resposta += "\nID da instância: " + i.getId()
                            + "\n - Capacidade alocada: " + i.getCapacidade() + "\n";
                }

            }

        }

        return resposta;

    }

    /**
     * Método para criar uma nova Unidade, criando-a com a capacidade passada
     * como parêmetro pelo Usuário e adicionando o objeto criado na lista de unidades
     * do Servidor.
     * @param capacidade - int
     */
    protected void criarNovaUnidade(int capacidade) {

        Unidade u = new Unidade(
                gerarIdUnidade(),
                capacidade
        );
        unidades.add(u);

    }

    /**
     * Método que deleta uma das unidades, encontrando na lista a Unidade com o ID
     * passado como parâmetro. Ao encontrar a Unidade, antes de excluir, verifica se 
     * não há instâncias atualmente alocadas nela.
     * @param id - String
     * @return boolean
     */
    protected boolean deletarUnidade(String id) {

        for (Unidade u : unidades) {

            if (u.getId().equals(id)) {

                if (!u.getInstancias().isEmpty()) {
                    return false;
                } else {

                    unidades.remove(u);
                    return true;

                }

            }

        }

        return false;

    }

    /**
     * Método que gera um ID para uma nova Unidade sendo criada.
     * @return String
     */
    protected String gerarIdUnidade() {

        if (unidades.isEmpty()) {
            return "u-1";
        } else {
            return "u-" + Integer.toString(unidades.size() + 1);
        }

    }

    /**
     * Método que mostra as unidades atualmente criadas e suas informações,
     * mostrando também as intâncias alocadas em cada uma e o custo calculado
     * das instâncias após desligamento.
     * @return String
     */
    protected String mostrarUnidades() {
        
        DecimalFormat fmt = new DecimalFormat("0.00");

        String resposta = "";

        for (Unidade u : unidades) {

            resposta += "\nUnidade: " + u.getId()
                    + "\n - Ligada: " + u.isLigada()
                    + "\n - Capacidade total: " + u.getCapacidadeTotal()
                    + "\n - Capacidade alocada: " + u.getCapacidadeAlocada()
                    + "\n - Custo atual: R$ " + fmt.format(u.getCustoFuncionamento());

            for (Instancia i : u.getInstancias()) {

                resposta += "\n   - Instância alocada: " + i.getId()
                        + "\n      - Capacidade: " + i.getCapacidade()
                        + "\n      - Usuário: " + i.getUsuario().getUsuario() + "\n";

            }

        }

        return resposta;

    }

    /**
     * Método que corresponde à um loop de conexão, aceitando a conexão do Cliente
     * através do Socket e iniciando uma thread para o TrataConexão tratar e responder
     * as solicitações do Cliente da sessão.
     * @throws IOException
     */
    public void loopConexao() throws IOException {

        int id = 0;

        while (true) {

            id++;
            Socket socket = serverSocket.accept();
            System.out.println("Cliente (ID: " + id + ") conectado!");

            TrataConexao trataConexao = new TrataConexao(this, socket, id);
            Thread thread = new Thread(trataConexao);
            thread.start();

        }

    }

    /**
     * Método principal que inicia o ServerSocket do Servidor e inicia o loop de Conexão.
     * @param args
     */
    public static void main(String[] args) throws ClassNotFoundException {

        System.out.println("Servidor de nuvem rodando...\n");

        try {

            Servidor servidor = new Servidor();
            serverSocket = new ServerSocket(6666);

            servidor.loopConexao();

        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }

    }

}
