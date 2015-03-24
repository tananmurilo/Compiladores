
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
        Arquivo arq = new Arquivo();
        
         /*abrir um arquivo pra teste mudar para o path do seu arquivo
        arq.read("D:\\Users\\murilo\\Desktop\\arquivo teste.txt"); //abrir um arquivo pra teste mudar para o path do seu arquivo
        System.out.println("");
        System.out.println("Teste na lista do arquivo");
        arq.print();
        
        AnalizadorLexico ana = new AnalizadorLexico();
        LinkedList<String> Tokens = new LinkedList<String>();
        Tokens =   ana.separarTokens(arq.getLinhas());
         
        System.out.println(" ");
        System.out.println("Tokens gerados pelo texto");
        for(String t:Tokens){
            System.out.println(t);
                    
        }
          */
    }
    
    
}
