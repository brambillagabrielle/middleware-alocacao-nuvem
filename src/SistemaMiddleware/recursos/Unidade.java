package SistemaMiddleware.recursos;

import SistemaMiddleware.servidor.Usuario;
import java.util.ArrayList;

/**
 * Classe que representa uma Unidade computacional.
 * @author Estéfani e Gabrielle
 */
public class Unidade {

    private String id;
    private boolean ligada;
    private int capacidadeTotal;
    private int capacidadeAlocada;
    private double tempoInicial;
    private double custoFuncionamento;
    private ArrayList<Instancia> instancias;

    /**
     * Construtor que inicia os valores do ID da Unidade e sua capacidade total
     * configurada na criação da Unidade, iniciando-a como desligada, com capacidade
     * alocada e custo de funcionamento igual a 0 e iniciando uma lista de instâncias
     * vazia.
     * @param id - String
     * @param capacidadeTotal - int
     */
    public Unidade(String id, int capacidadeTotal) {
        this.id = id;
        ligada = false;
        this.capacidadeTotal = capacidadeTotal;
        capacidadeAlocada = 0;
        custoFuncionamento = 0.0;
        instancias = new ArrayList<>();
    }

    /**
     * Método que aloca uma nova Instância na Unidade, criado e adicionado um objeto
     * à lista de instâncias existentes, com uma capacidade informada pelo usuário
     * que está alocando-a.
     * @param quantidadeRecursos - int
     * @param usuario - Usuário
     */
    public void alocarInstancia(int quantidadeRecursos, Usuario usuario, double tempoInicial) {

        Instancia i = new Instancia(
                gerarIdInstancia(),
                this.id,
                quantidadeRecursos,
                usuario,
                tempoInicial
        );
        instancias.add(i);

    }

    /**
     * Método que gera um ID para uma nova Instância sendo criada.
     * @return String
     */
    protected String gerarIdInstancia() {

        if (instancias.isEmpty()) {
            return this.id + "&i-" + 1;
        } else {
            return this.id + "&i-" + Integer.toString(instancias.size() + 1);
        }

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
     * Método que verifica o valor do atributo ligada
     * @return String
     */
    public boolean isLigada() {
        return ligada;
    }

    /**
     * Método set do atributo ligada
     * @param ligada - boolean
     */
    public void setLigada(boolean ligada) {
        this.ligada = ligada;
    }

    /**
     * Método get do atributo capacidade total
     * @return int
     */
    public int getCapacidadeTotal() {
        return capacidadeTotal;
    }

    /**
     * Método set do atributo capacidade total
     * @param capacidadeTotal - int
     */
    public void setCapacidadeTotal(int capacidadeTotal) {
        this.capacidadeTotal = capacidadeTotal;
    }

    /**
     * Método get do atributo capacidade alocada
     * @return int
     */
    public int getCapacidadeAlocada() {
        return capacidadeAlocada;
    }

    /**
     * Método set do atributo capacidade alocada
     * @param capacidadeAlocada - int
     */
    public void setCapacidadeAlocada(int capacidadeAlocada) {
        this.capacidadeAlocada = capacidadeAlocada;
    }

    /**
     * Método get do atributo tempo inicial
     * @return double
     */
    public double getTempoInicial() {
        return tempoInicial;
    }

    /**
     * Método set do atributo tempo inicial
     * @param tempoInicial - double
     */
    public void setTempoInicial(double tempoInicial) {
        this.tempoInicial = tempoInicial;
    }

    /**
     * Método get do atributo custo de funcionamento
     * @return double
     */
    public double getCustoFuncionamento() {
        return custoFuncionamento;
    }

    /**
     * Método set do atributo custo de funcionamento
     * @param custoFuncionamento - double
     */
    public void setCustoFuncionamento(double custoFuncionamento) {
        this.custoFuncionamento = custoFuncionamento;
    }

    /**
     * Método get da lista de instâncias
     * @return ArrayList
     */
    public ArrayList<Instancia> getInstancias() {
        return instancias;
    }

    /**
     * Método set da lista de instâncias
     * @param instancias - ArrayList
     */
    public void setInstancias(ArrayList<Instancia> instancias) {
        this.instancias = instancias;
    }

}
