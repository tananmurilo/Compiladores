/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mateus
 */
public class Automato {
    
    int[][] matrizTransicao = new int [8][8];
    int estadoAtual = 0;
    
    public String iniciar(String input){
      
        String output = "";
        
        inicializaMatriz();
        
        
        for(int i =0; i< input.length(); i++){
            atualizaEstado(estadoAtual, input.charAt(i));
        }
        
        output = getToken();
        
        return output;
    };
    
    private void inicializaMatriz(){
        // Aqui ser'ao listadas todas as possíveis transições
    }
    
    private int atualizaEstado(int estado, int entrada){
            return matrizTransicao[estado] [entrada];
    };
    
    private String getToken(){
        switch(estadoAtual){
            case 8:
                return "<numero>";
                
            default:  
                return null;
                
        }     
    }
}
