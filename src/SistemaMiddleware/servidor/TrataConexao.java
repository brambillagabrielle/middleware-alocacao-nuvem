package SistemaMiddleware.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import SistemaMiddleware.util.Estados;
import SistemaMiddleware.util.Mensagem;
import SistemaMiddleware.util.Status;

/**
 * Classe que recebe as requisições do Cliente e envia as respostas
 * para as solicitações, chamando os métodos da classe Servidor para realizar
 * as funções de autenticação, alocação/desalocação de instâncias, criar/deletar
 * unidades e mostrar informações.
 * @author Estéfani e Gabrielle
 */
public class TrataConexao implements Runnable {

    private final Servidor servidor;
    private final Socket socket;
    private final int id;
    Usuario autenticado;
    private Estados estado;

    /**
     * Construtor que inicia o Servidor e o Socket e ID do Cliente conectado.
     * @param servidor - Servidor
     * @param socket - Socket
     * @param id - int
     */
    public TrataConexao(Servidor servidor, Socket socket, int id) {
        this.servidor = servidor;
        this.socket = socket;
        this.id = id;
        estado = Estados.CONECTADO;
    }

    /**
     * Método que recebe as requisições de entrada e envia as mensagens de resposta
     * para o cliente na saída. A comunicação vai ocorrer até que o Cliente envie
     * o desejo de se desconectar ao digitar SAIR.
     * @throws IOException, ClassNotFoundException
     */
    private void trataConexao() throws IOException, ClassNotFoundException {

        try {

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            estado = Estados.CONECTADO;
            while (estado != Estados.SAIR) {

                Mensagem mensagem = (Mensagem) entrada.readObject();

                String operacao = mensagem.getOperacao();
                Mensagem resposta = new Mensagem(operacao + "_RESPOSTA");

                if (operacao.equals("SAIR")) {

                    System.out.println("Cliente (ID: " + id + ") desconectado!");
                    estado = Estados.SAIR;
                    break;

                } else if (estado.equals(Estados.CONECTADO)) {

                    switch (operacao) {

                        case "LOGIN":
                                
                                try {

                            String usuario = (String) mensagem.getParam("usuario");
                            String senha = (String) mensagem.getParam("senha");

                            autenticado = servidor.getUsuarioPorNome(usuario);

                            if (autenticado.getUsuario().equals(usuario)
                                    && autenticado.getSenha().equals(senha)) {

                                estado = Estados.AUTENTICADO;
                                resposta.setStatus(Status.OK);

                                resposta.setParam("privilegios", autenticado.getTipo());

                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                        } catch (Exception e) {
                            resposta.setStatus(Status.ERROR_PARAM);
                        }

                        saida.writeObject(resposta);
                        saida.flush();

                        break;

                        case "CADASTRAR":
                                
                                try {

                            String nomeUsuario = (String) mensagem.getParam("usuario");
                            String senha = (String) mensagem.getParam("senha");

                            Usuario usuario = new Usuario(nomeUsuario, senha, "COMUM");

                            if (servidor.cadastraUsuario(usuario)) {
                                resposta.setStatus(Status.OK);
                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                        } catch (Exception e) {
                            resposta.setStatus(Status.ERROR_PARAM);
                        }

                        saida.writeObject(resposta);
                        saida.flush();

                        break;

                        default:

                            resposta.setStatus(Status.ERROR);

                            saida.writeObject(resposta);
                            saida.flush();

                            break;
                    }

                } else {

                    switch (operacao) {

                        case "ALOCAR":
                            
                            try {

                            String quantidadeRecursos = (String) mensagem.getParam("recursos");
                            int quantidadeRecursosInt = Integer.parseInt(quantidadeRecursos);

                            if (servidor.alocarNovaInstancia(quantidadeRecursosInt, autenticado)) {
                                resposta.setStatus(Status.OK);
                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                        } catch (NumberFormatException e) {
                            resposta.setStatus(Status.ERROR_PARAM);
                        }

                        saida.writeObject(resposta);
                        saida.flush();

                        break;

                        case "DESALOCAR":

                            try {

                            String idInstancia = (String) mensagem.getParam("idInstancia");

                            if (servidor.desalocarInstancia(idInstancia, autenticado)) {
                                resposta.setStatus(Status.OK);
                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                        } catch (Exception e) {
                            resposta.setStatus(Status.ERROR_PARAM);
                        }

                        saida.writeObject(resposta);
                        saida.flush();

                        break;

                        case "MOSTRAR":

                            String listaInstancias = servidor.mostrarInstanciasUsuario(autenticado);

                            if (listaInstancias.length() != 0) {

                                resposta.setParam("instancias", listaInstancias);
                                resposta.setStatus(Status.OK);

                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                            saida.writeObject(resposta);
                            saida.flush();

                            break;

                        case "CRIAR_UNIDADE":
                            
                            try {

                            String capacidade = (String) mensagem.getParam("capacidade");
                            int capacidadeInt = Integer.parseInt(capacidade);

                            if (autenticado.getTipo().equals("ADMIN")) {

                                servidor.criarNovaUnidade(capacidadeInt);
                                resposta.setStatus(Status.OK);

                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                        } catch (NumberFormatException e) {
                            resposta.setStatus(Status.ERROR_PARAM);
                        }

                        saida.writeObject(resposta);
                        saida.flush();

                        break;

                        case "DELETAR_UNIDADE":
                            
                            try {

                            String idUnidade = (String) mensagem.getParam("idUnidade");

                            if (servidor.deletarUnidade(idUnidade)) {
                                resposta.setStatus(Status.OK);
                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                        } catch (Exception e) {
                            resposta.setStatus(Status.ERROR_PARAM);
                        }

                        saida.writeObject(resposta);
                        saida.flush();

                        break;

                        case "MOSTRAR_UNIDADES":

                            if (autenticado.getTipo().equals("ADMIN")) {

                                String listaUnidades = servidor.mostrarUnidades();

                                if (listaUnidades.length() != 0) {

                                    resposta.setParam("unidades", listaUnidades);
                                    resposta.setStatus(Status.OK);

                                } else {
                                    resposta.setStatus(Status.ERROR);
                                }

                            } else {
                                resposta.setStatus(Status.ERROR);
                            }

                            saida.writeObject(resposta);
                            saida.flush();

                            break;

                        default:

                            resposta.setStatus(Status.ERROR);

                            saida.writeObject(resposta);
                            saida.flush();

                            break;
                    }

                }

            }

        } catch (IOException e) {

            System.out.println("Problema no tratamento da conexão com o cliente: " + socket.getInetAddress());
            System.out.println("Erro: " + e.getMessage());

        } finally {
            socket.close();
        }

    }

    /**
     * Thread que chama o método que trata a conexão do Cliente com o Servidor,
     * de forma paralelizada.
     */
    @Override
    public void run() {

        try {
            trataConexao();
        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
            System.out.println("Erro no tratamento de conexão" + e.getMessage());

        }

    }

}
