/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author murilo
 */
public class Estrutura {
    
    private String nome;
    private String token;
    private String tipo;
    private int tamanho1;
    private int tamanho2;
    private String parametros[]; // cada posição é um parametro e a ordem das posições é a ordem dos parametros
    private String atributos[][]; // cada linha é um atributo, as colunas são as infomações deles (nome, tipo)
    //atributos e parametros são outra estrutura

    public String[] getParametros() {
        return parametros;
    }

    public String[][] getAtributos() {
        return atributos;
    }

    public void setParametros(String[] parametros) {
        this.parametros = parametros;
    }

    public void setAtributos(String[][] atributos) {
        this.atributos = atributos;
    }
    
    
    /**
     * @return the nome
     */
    
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the tamanho1
     */
    public int getTamanho1() {
        return tamanho1;
    }

    /**
     * @param tamanho1 the tamanho1 to set
     */
    public void setTamanho1(int tamanho1) {
        this.tamanho1 = tamanho1;
    }

    /**
     * @return the tamanho2
     */
    public int getTamanho2() {
        return tamanho2;
    }

    /**
     * @param tamanho2 the tamanho2 to set
     */
    public void setTamanho2(int tamanho2) {
        this.tamanho2 = tamanho2;
    }
}
