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
        
        Interface a = new Interface();
        a.setVisible(true);
        //Arquivo arq = new Arquivo();
        //arq.read("D:\\Users\\murilo\\Desktop\\arquivo teste.txt");
        //System.out.println("");
       // System.out.println("Teste na lista");
        //arq.print();
    }
    
    
}
