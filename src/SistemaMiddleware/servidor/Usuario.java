package SistemaMiddleware.servidor;

/**
 * Classe que representa um Usuário.
 * @author Estéfani e Gabrielle
 */
public class Usuario {
    
    private String usuario;
    private String senha;
    private String tipo;
    private double custoAlocacoes;
    
    /**
     * Construtor que inicializa os valores do nome de usuário, senha e tipo
     * do usuário (COMUM/ADMIN), além do custo das alocações realizadas como 0.
     * @param usuario - String
     * @param senha - String
     * @param tipo - String
     */
    public Usuario(String usuario, String senha, String tipo) {
        this.usuario = usuario;
        this.senha = senha;
        this.tipo = tipo;
        custoAlocacoes = 0.0;
    }

    /**
     * Método get do atributo usuario (nome do usuário)
     * @return String
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Método set do atributo usuario (nome do usuário)
     * @param usuario - String
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Método get do atributo senha
     * @return String
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Método set do atributo senha
     * @param senha - String
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Método get do atributo tipo do usuário
     * @return String
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Método set do atributo do tipo do usuário
     * @param tipo - String
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Método get do atributo do custo das alocações
     * @return double
     */
    public double getCustoAlocacoes() {
        return custoAlocacoes;
    }

    /**
     * Método set do atributo do custo das alocações
     * @param custoAlocacoes - double
     */
    public void setCustoAlocacoes(double custoAlocacoes) {
        this.custoAlocacoes = custoAlocacoes;
    }
    
}
