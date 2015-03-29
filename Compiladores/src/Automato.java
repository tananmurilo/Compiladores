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
    //int estadoAtual = 0;
    
    
    public String iniciar(String input, int linha){
        int estadoAtual = 0;
        String output = "";
        String texto="";
        String cr="";
        
        //inicializaMatriz();
        
        
        for(int i =0; i< input.length(); i++){
            
            char ch = input.charAt(i);
            
            if(estadoAtual==0){ //se o estado for inicial do automato 
               
                //identificar os primeiros estados aqui tipo, 1 2 3 4 ...cada um vai seguir para uma folha do automato.
                if((ch>=65 && ch<=90) || (ch>=97&&ch<=122)){ //letra a-z|A-Z
                    
                    estadoAtual=1; //estado para identificadores
                    //System.out.println("primeira ltra: "+ch);
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if((ch>=48)&&(ch<=57)){ //numeros 0-9
                    estadoAtual=2; //estado para numeros
                    //System.out.println("primeira numero: "+ch);
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if(ch==' ' || ch==';' || ch==',' || ch=='(' || ch==')' || ch=='{' || ch=='}' || ch=='[' || ch==']' ){
                
                    estadoAtual=4; //estado para delimitadores
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                
                }else if(ch=='+' || ch=='-' || ch=='=' || ch=='.' || ch=='*' || ch=='/' || ch=='>' || ch=='<' ){
                
                    estadoAtual=5; //estado para operadores de um caractere
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else{
                    //System.err.println("ERRO não encontrou o caracter");
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                    estadoAtual=99; //estado de erro para ser exibido.
                    //estadoAtual=0;
                    
                }
                
            }else if(estadoAtual==1){ //identificadore e palavras reservadas
               
                if((ch>=65 && ch<=90) || (ch>=97&&ch<=122) || (ch>=48&&ch<=57)||(ch==95)){ //letra a-z|A-Z numero e _
                    //System.out.println("sengada + letra: "+ch);
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                 
                }else{//erro se o caractere lido não for letra numero ou _
                    estadoAtual=99; //estado de erro para ser exibido.
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    //System.err.println("ERRO token");
                }
                
            }else if(estadoAtual==2){ //numeros
                if((ch>=48)&&(ch<=57)){ // numero 
                    //System.out.println("sengada + letra: "+ch);
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                 
                }else if(ch==46){
                    if (i+1 == input.length()) {//caso não tenha mais caractere depois do .
                         estadoAtual = 98;
                         cr = String.valueOf(ch);
                         texto= texto+cr;
                    }else{
                        cr = String.valueOf(ch);
                        texto= texto+cr;
                        estadoAtual = 3;
                    }
                }else{
                    estadoAtual=99; //estado de erro para ser exibido.
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                   // System.err.println("ERRO caracter não encontrado");
                }
            
            }else if(estadoAtual==3){
                if((ch>=48)&&(ch<=57)){ //numero 
                    //System.out.println("sengada + letra: "+ch);
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                }else{
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                    estadoAtual=98; //estado de erro para ser exibido.
                }
                
            }else if(estadoAtual==98){//estado error de token mau formado
                cr = String.valueOf(ch);
                texto= texto+cr;
                
            }else if(estadoAtual==99){ //estado error de token/caracteres não identificado/não pertencem a linguagem
                cr = String.valueOf(ch);
                texto= texto+cr;
            }else{
                estadoAtual=99;
                cr = String.valueOf(ch);
                texto= texto+cr;
                
               // System.err.println("ERRO nenhum estado encontrado");
            }
            //atualizaEstado(estadoAtual, input.charAt(i));
        }
        //pega o toke + seu identificador
        output = getToken(texto, estadoAtual, linha);
        
        return output;
    };
    
    private void inicializaMatriz(){
        //matrizTransicao[0][7];
    }
    
    private int atualizaEstado(int estado, int entrada, int linha){
            return matrizTransicao[estado] [entrada];
    };
    
    /**
     * Método retorna o token e seu tipo
     * @param txt texto correspondente ao token
     * @param estado estado final/estado onde o algoritmo terminou 
     * @return Token completo
     */
    private String getToken(String txt, int estado, int linha){
        switch(estado){
            case 1: 
                PalavrasReservadas reservadas = new PalavrasReservadas();
                if(reservadas.buscarPalavra(txt)==true){
                    return "<PalavraReservada, "+txt+">";
                }else{
                    return "<Identificador, "+txt+">";
                }
                
            case 2:
                return "<Número, "+txt+">";
                
            case 3:
                return "<Número, "+txt+">";
                
            case 4:
                return "<Delimitador, "+txt+">";
                
            case 5:
                return "<Operador, "+txt+">";
                
            case 8:
                return "<numero>";
                
            case 98:
                return "<Token mau formado na linha " +linha+ ", "+txt+">";
                
            case 99:
                return "<token não identificado  na linha " +linha+ ", "+txt+">";
                 
            default:  
                return null;
                
        }     
    }
}
