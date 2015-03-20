/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author murilo
 */



public class Main {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        System.out.println("teste");
        PalavrasReservadas reservadas = new PalavrasReservadas();
        //testar busca nas palavras resevadas
        System.out.println(reservadas.buscarPalavra("registro"));
    }
    
    
}
