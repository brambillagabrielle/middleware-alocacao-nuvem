package SistemaMiddleware.cliente;

import SistemaMiddleware.util.Mensagem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que envia as requisições de clientes não autenticados, clientes comuns
 * e administradores para o servidor (recebido pelo TrataConexao), recebendo a
 * resposta sobre o sucesso ou falha das operações solicitadas.
 * @author Estéfani e Gabrielle
 */
public class Cliente {

    /**
     * Método principal que inicia o Cliente e estabelece a conexão com o servidor
     * através de Socket, com a porta 6666. Inicia com o Cliente não autenticado podendo
     * fazer o login ou criar um novo cadastro. Após o Cliente se autenticar com
     * sucesso, pode realizar as operações que correspondem ao tipo de Cliente
     * (COMUM/ADMIN) e suas permissões.
     * @param args
     */
    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 6666);

            System.out.println(" _________________________ ");
            System.out.println("|                         |");
            System.out.println("|        Nuvem EGS        |");
            System.out.println("|_________________________|");

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            boolean sair = false, autenticado = false;
            Mensagem mensagem, resposta;
            String acao, usuario, senha, privilegios = "";

            while (!sair) {

                if (!autenticado) {

                    System.out.println("\nQue ação deseja realizar? (LOGIN/CADASTRAR/SAIR): ");
                    System.out.print(" > ");
                    acao = teclado.readLine();

                    switch (acao.toUpperCase()) {

                        case "LOGIN":

                            mensagem = new Mensagem("LOGIN");

                            System.out.println("Insira seu nome de usuário: ");
                            System.out.print(" > ");
                            usuario = teclado.readLine();
                            mensagem.setParam("usuario", usuario);

                            System.out.println("Insira sua senha de usuário: ");
                            System.out.print(" > ");
                            senha = teclado.readLine();
                            mensagem.setParam("senha", senha);

                            saida.writeObject(mensagem);
                            saida.flush();

                            resposta = (Mensagem) entrada.readObject();

                            switch (resposta.getStatus()) {

                                case OK:

                                    autenticado = true;
                                    privilegios = resposta.getParam("privilegios").toString();
                                    System.out.println("\n * Usuário autenticado com sucesso!");
                                    break;

                                case ERROR:

                                    System.out.println("\n * Usuário não autenticado: nome e/ou senha inválidos!");
                                    break;

                                case ERROR_PARAM:

                                    System.out.println("\n * Usuário não autenticado: dados inseridos são inválidos!");
                                    break;

                            }

                            break;

                        case "CADASTRAR":

                            mensagem = new Mensagem("CADASTRAR");

                            System.out.println("Insira o nome do usuário para cadastrar: ");
                            System.out.print(" > ");
                            usuario = teclado.readLine();
                            mensagem.setParam("usuario", usuario);

                            System.out.println("Insira a senha do usuário para cadastrar: ");
                            System.out.print(" > ");
                            senha = teclado.readLine();
                            mensagem.setParam("senha", senha);

                            saida.writeObject(mensagem);
                            saida.flush();

                            resposta = (Mensagem) entrada.readObject();

                            switch (resposta.getStatus()) {

                                case OK:

                                    System.out.println("\n * Usuário cadastrado com sucesso!");
                                    break;

                                case ERROR:

                                    System.out.println("\n * Usuário não pode ser cadastrado: usuário já existe!");
                                    break;

                                case ERROR_PARAM:

                                    System.out.println("\n * Usuário não pode ser cadastrado: dados inseridos são inválidos!");
                                    break;

                            }

                            break;

                        case "SAIR":

                            mensagem = new Mensagem("SAIR");
                            saida.writeObject(mensagem);
                            saida.flush();

                            sair = true;

                            break;

                        default:
                            System.out.println("\n * Ação inválida!");

                    }

                } else {

                    if (privilegios.equals("COMUM")) {

                        System.out.println("\nQue ação deseja realizar? (ALOCAR/DESALOCAR/MOSTRAR/SAIR): ");
                        System.out.print(" > ");
                        acao = teclado.readLine();

                        switch (acao.toUpperCase()) {

                            case "ALOCAR":

                                mensagem = new Mensagem("ALOCAR");

                                System.out.println("Insira a quantidade de recursos que deseja alocar: ");
                                System.out.print(" > ");
                                String quantidadeRecursos = teclado.readLine();
                                mensagem.setParam("recursos", quantidadeRecursos);

                                saida.writeObject(mensagem);
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();

                                switch (resposta.getStatus()) {

                                    case OK:

                                        System.out.println("\n * Instância alocada com sucesso!");
                                        break;

                                    case ERROR:

                                        System.out.println("\n * Instância não pode ser alocada: não há unidades disponíveis para alocação!");
                                        break;

                                    case ERROR_PARAM:

                                        System.out.println("\n * Instância não pode ser alocada: valor inserido é inválido!");
                                        break;

                                }

                                break;

                            case "DESALOCAR":

                                mensagem = new Mensagem("DESALOCAR");

                                System.out.println("Insira o ID da instância para desalocar: ");
                                System.out.print(" > ");
                                String idInstancia = teclado.readLine();
                                mensagem.setParam("idInstancia", idInstancia);

                                saida.writeObject(mensagem);
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();

                                switch (resposta.getStatus()) {

                                    case OK:

                                        System.out.println("\n * Instância desalocada com sucesso!");
                                        break;

                                    case ERROR:

                                        System.out.println("\n * Instância não pode ser desalocada: ID não encontrado!");
                                        break;

                                    case ERROR_PARAM:

                                        System.out.println("\n * Instância não pode ser desalocada: ID inserido é inválido!");
                                        break;

                                }

                                break;

                            case "MOSTRAR":

                                mensagem = new Mensagem("MOSTRAR");

                                saida.writeObject(mensagem);
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();

                                switch (resposta.getStatus()) {

                                    case OK:

                                        System.out.println("\n * Instâncias alocadas: \n\n" + resposta.getParam("instancias"));
                                        break;

                                    case ERROR:

                                        System.out.println("\n * Erro ao monstrar as instâncias alocadas: nenhuma instância para mostrar!");
                                        break;

                                    case ERROR_PARAM:

                                        System.out.println("\n * Erro ao monstrar as instâncias alocadas!");
                                        break;

                                }

                                break;

                            case "SAIR":

                                mensagem = new Mensagem("SAIR");
                                saida.writeObject(mensagem);
                                saida.flush();

                                sair = true;

                                break;

                            default:
                                System.out.println("\n * Ação inválida!");

                        }

                    } else {

                        System.out.println("\nQue ação deseja realizar? (CRIAR_UNIDADE/DELETAR_UNIDADE/MOSTRAR_UNIDADES/SAIR): ");
                        System.out.print(" > ");
                        acao = teclado.readLine();

                        switch (acao.toUpperCase()) {

                            case "CRIAR_UNIDADE":

                                mensagem = new Mensagem("CRIAR_UNIDADE");

                                System.out.println("Insira a capacidade da unidade para ser criada: ");
                                System.out.print(" > ");
                                String capacidade = teclado.readLine();
                                mensagem.setParam("capacidade", capacidade);

                                saida.writeObject(mensagem);
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();

                                switch (resposta.getStatus()) {

                                    case OK:

                                        System.out.println("\n * Unidade criada com sucesso!");
                                        break;

                                    case ERROR:

                                        System.out.println("\n * Unidade não pode ser criada: não há unidades dispoíveis para alocação!");
                                        break;

                                    case ERROR_PARAM:

                                        System.out.println("\n * Unidade não pode ser criada: capacidade inserida inválida!");
                                        break;

                                }

                                break;
                                
                            case "DELETAR_UNIDADE":
                                
                                mensagem = new Mensagem("DELETAR_UNIDADE");

                                System.out.println("Insira o ID da unidade para deletar: ");
                                System.out.print(" > ");
                                String idUnidade = teclado.readLine();
                                mensagem.setParam("idUnidade", idUnidade);

                                saida.writeObject(mensagem);
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();

                                switch (resposta.getStatus()) {

                                    case OK:

                                        System.out.println("\n * Unidade deletada com sucesso!");
                                        break;

                                    case ERROR:

                                        System.out.println("\n * Unidade não pode ser deletada: há instâncias alocadas na unidade!");
                                        break;

                                    case ERROR_PARAM:

                                        System.out.println("\n * Unidade não pode ser deletada: ID inserido é inválido!");
                                        break;

                                }
                                
                                break;

                            case "MOSTRAR_UNIDADES":

                                mensagem = new Mensagem("MOSTRAR_UNIDADES");

                                saida.writeObject(mensagem);
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();

                                switch (resposta.getStatus()) {

                                    case OK:

                                        System.out.println("\n * Unidades existentes: \n\n" + resposta.getParam("unidades"));
                                        break;

                                    case ERROR:

                                        System.out.println("\n * Erro ao mostrar as unidades existem: nenhuma unidade para mostrar!");
                                        break;

                                    case ERROR_PARAM:

                                        System.out.println("\n * Erro ao mostrar as unidades existentes!");
                                        break;

                                }

                                break;

                            case "SAIR":

                                mensagem = new Mensagem("SAIR");
                                saida.writeObject(mensagem);
                                saida.flush();

                                sair = true;

                                break;

                            default:
                                System.out.println("\n * Ação inválida!");

                        }

                    }

                }

            }

            entrada.close();
            saida.close();
            socket.close();

        } catch (IOException e) {

            System.out.println("Erro no cliente: " + e);
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);

        } catch (ClassNotFoundException e) {

            System.out.println("Erro no cast: " + e.getMessage());
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);

        }

    }

}
