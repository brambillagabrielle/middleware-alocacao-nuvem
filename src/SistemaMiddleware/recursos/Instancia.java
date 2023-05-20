package SistemaMiddleware.recursos;

import SistemaMiddleware.servidor.Usuario;

/**
 * Classe que representa uma Instância de processamento.
 * @author Estéfani e Gabrielle
 */
public class Instancia {
    
    private String id;
    private String unidade;
    private int capacidade;
    private Usuario usuario;
    private double tempoInicial;

    /**
     * Construtor que inicia os valores dos atributos ID da Instância, a Unidade
     * em que foi alocada (ID), a sua capacidade e o Usuário que a alocou, além do
     * tempo inicial da alocação.
     * @param id - String
     * @param unidade - Unidade
     * @param capacidade - int
     * @param usuario - Usuario
     */
    public Instancia(String id, String unidade, int capacidade, Usuario usuario, double tempoInicial) {
        this.id = id;
        this.unidade = unidade;
        this.capacidade = capacidade;
        this.usuario = usuario;
        this.tempoInicial = tempoInicial;
    }

    /**
     * Método get do atributo ID
     * @return String
     */
    public String getId() {
        return id;
    }
    
    /**
     * Método set do atributo ID
     * @param id - String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Método get do atributo com o ID da Unidade
     * @return String
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * Método set do atributo com o ID da Unidade
     * @param unidade - String
     */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /**
     * Método get do atributo capacidade
     * @return int
     */
    public int getCapacidade() {
        return capacidade;
    }

    /**
     * Método set do atributo capacidade
     * @param capacidade - int
     */
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    /**
     * Método get do atributo usuário
     * @return Usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Método set do atributo usuário
     * @param usuario - Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Método get do atributo tempo inicial
     * @return double
     */
    public double getTempoInicial() {
        return tempoInicial;
    }

    /**
     * Método set do atributo  tempo inicial
     * @param tempoInicial - double
     */
    public void setTempoInicial(double tempoInicial) {
        this.tempoInicial = tempoInicial;
    }
    
}
