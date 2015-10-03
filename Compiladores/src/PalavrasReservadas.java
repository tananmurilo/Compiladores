
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author murilo
 */
public class PalavrasReservadas {
    /**
     * Inicializar a lista de palavras resevadas
     * 
     */
    PalavrasReservadas(){
      lista = new LinkedList<String>();
        lista.add("algoritmo");
        lista.add("variaveis");
        lista.add("constantes");
        lista.add("registro");
        lista.add("funcao");
        lista.add("retorno");
        lista.add("vazio");
        lista.add("se");
        lista.add("senao");
        lista.add("enquanto");
        lista.add("para");
        lista.add("leia");
        lista.add("escreva");
        lista.add("inteiro");
        lista.add("real");
        lista.add("booleano");
        lista.add("char");
        lista.add("cadeia");
        lista.add("verdadeiro");
        lista.add("falso");
    }
    
    //declaração de variaveis, listas e etc
    private LinkedList<String> lista;
    
    /**
     * Metodo para verificar se a palavra buscada esta na lista de palavras resevadas.
     * @param palavra string com a palavra procurada.
     * @return true se for uma palavra reservada, false se não for.
     */
    public boolean buscarPalavra(String palavra){
        return lista.contains(palavra);
    }
    
    public boolean adicionarPalavra(String palavra){
        return lista.add(palavra);
    }
    
    
}
