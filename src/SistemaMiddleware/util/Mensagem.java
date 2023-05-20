package SistemaMiddleware.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe que representa as mensagens enviadas do Servidor para o Cliente (saída) e do
 * Cliente para o Servidor (entrada).
 * @author Estéfani e Gabrielle
 */
public class Mensagem implements Serializable {
    
    private final String operacao;
    private Status status;
    Map<String, Object> params;
    
    /**
     * Construtor que cria uma nova mensagem, iniciando os valores da operação e
     * dos paramêtros.
     * @param operacao
     */
    public Mensagem(String operacao) {
       this.operacao = operacao;
       params = new HashMap<>();
    }
    
    /**
     * Método get do atributo operação
     * @return String
     */
    public String getOperacao() {
        return operacao;
    }
    
    /**
     * Método get do atributo status
     * @return String
     */
    public Status getStatus() {
        return status;
    }
        
    /**
     * Método set do atributo status
     * @param status - Status
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
     * Método get do atributo param, mostrando um parâmetro a partir da chave
     * informada
     * @param chave
     */
    public Object getParam(String chave) {
        return params.get(chave);
    }
    
    /**
     * Método set do atributo param, criando um novo parâmetro com chave e valor
     * @param chave
     * @param valor
     */
    public void setParam(String chave, Object valor) {
        params.put(chave, valor);
    }
    
}