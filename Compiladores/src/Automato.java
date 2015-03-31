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
    /*Lembrete
       Estados nº - descrição
        0 - estado inicial
        1 - identificadores e palavras reservadas
        2 - numeros inteiros
        3 - numeros fracionados
        4 - delimitadores
        5 - Estado final para operador simples . * + - = < > /
        6 - caracter constante
        7 - caracter constatne parte final
        8 - Cadeia Constante
        9 - Cadeia Constante parte final
        10 - operador !=
        11 - estado final para operadores duplos != == && || ++ == --
        12 - operador &&
        13 - operador ||
        14 - operador <=, >= ==
        15 - operador --
        16 - operador ++
        17 - comentarios
        18 - comentario de linha
        19 - comentario de bloco
        20 - comentario de bloco
        21 - comentario de bloco estado final
        98 - token mal formado(pode ser dividi mais tarde em numero mal formado, identificado mal formado etc..)
        99 - caracter não encontrado/não pertence a linguagem
    */
    
    
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
                    
                }else if(ch==' ' || ch==';' || ch==',' || ch=='(' || ch==')' || ch=='{' || ch=='}' || ch=='[' || ch==']' ){//delimitadores
                
                    estadoAtual=4; //estado para delimitadores
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                
                }else if(ch=='+' || ch=='-' || ch=='=' || ch=='.' || ch=='*' || ch=='/' || ch=='>' || ch=='<' ){
                    if((ch=='>' || ch=='<' || ch=='=')&&(i+1 < input.length())){
                        estadoAtual=14; //estado para operadores <=, >= e ==
                        cr = String.valueOf(ch);
                        texto= texto+cr;// concatenar com os caracteres; 
                    }else if((ch=='-')&&(i+1 < input.length())){
                        estadoAtual=15; //estado para operadores --
                        cr = String.valueOf(ch);
                        texto= texto+cr;// concatenar com os caracteres; 
                    }else if((ch=='+')&&(i+1 < input.length())){
                        estadoAtual=16; //estado para operadores ++
                        cr = String.valueOf(ch);
                        texto= texto+cr;// concatenar com os caracteres; 
                    }else{
                        estadoAtual=5; //estado para operadores de um caractere
                        cr = String.valueOf(ch);
                        texto= texto+cr;// concatenar com os caracteres; 
                    }
                   
                    
                }else if(ch==39){//caracter constante
                    estadoAtual=6;
                    //colocar as ' no texto
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if(ch==34){ //cadeia constante
                    estadoAtual=8;
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if(ch==33){ // operador !=
                    estadoAtual=10;
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if(ch==38){ // operador &&
                    estadoAtual=12;
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if(ch==124){ // operador ||
                    estadoAtual=13;
                    cr = String.valueOf(ch);
                    texto= texto+cr;// concatenar com os caracteres;
                    
                }else if(ch==47){ // barra
                    estadoAtual=17;
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
                
            }else if(estadoAtual==6){ //caracter constante
                if(((ch>=32&&ch<=38)||(ch>=40&&ch<=126))){ //ver se é um caracter valido
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                    estadoAtual = 7;  //estado que identifica se o prox caracter é '
                }else{
                    estadoAtual = 98;
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }   
            }else if(estadoAtual==7){//caracter constante parte final
                if((ch==39)&&(i+1 == input.length())){//se o caracter for ' é o fim do caracter constante caso contrario é error
                    //coloca as ' na string
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                    estadoAtual = 7; 
                }else{
                    estadoAtual = 98;
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }   
                
            }else if(estadoAtual==8){//cadeia  constante parte final
                if(((ch>=32&&ch<=33)||(ch>=35&&ch<=126))){ //ver se é um caracter valido.
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                   
                }else if(ch==34&&(i+1 == input.length())){//identifica o final da cadeia
                    estadoAtual = 9;
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                    
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }   
                
            }else if(estadoAtual==10){
                if(ch=='='&&(i+1 == input.length())){
                   estadoAtual = 11; //!=
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==12){
                if(ch=='&'&&(i+1 == input.length())){
                   estadoAtual = 11; //!= && ||
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==13){
                if(ch=='|'&&(i+1 == input.length())){
                   estadoAtual = 11; //!= && ||
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==14){
                if(ch=='='&&(i+1 == input.length())){
                   estadoAtual = 11; //operador duplo
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==15){
                if(ch=='-'&&(i+1 == input.length())){
                   estadoAtual = 11; //operador duplo
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==16){
                if(ch=='+'&&(i+1 == input.length())){
                   estadoAtual = 11; //operador duplo
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==17){
                if(ch=='/'){
                   estadoAtual = 18; //comentario de linha
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else if(ch=='*'){
                   estadoAtual = 19; //comentario de bloco
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                }else{
                    estadoAtual = 98; //error
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
            }else if(estadoAtual==18){
                if(ch>=32&&ch<=126){
                   estadoAtual = 18; //comentario de linha
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else{
                    estadoAtual = 98;
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
                
            }else if(estadoAtual==19){
                if((ch>=32&&ch<=41) || (ch>=43&&ch<=126)){
                   estadoAtual = 19; //comentario de bloco
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else if(ch=='*'){
                   estadoAtual = 20; //comentario de bloco
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                }else{
                    estadoAtual = 98;
                    cr = String.valueOf(ch);
                    texto= texto+cr;
                }
                
            }else if(estadoAtual==20){
                if((ch>=32&&ch<=41) || (ch>=43&&ch<=126)){
                   estadoAtual = 19; //volta pro estado 19
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                   
                }else if(ch=='*'){
                   estadoAtual = 20; //comentario de bloco
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                }else if(ch=='/'){
                   estadoAtual = 21; //comentario de bloco estado final
                   cr = String.valueOf(ch);
                   texto= texto+cr; 
                }else{
                    estadoAtual = 98;
                    cr = String.valueOf(ch);
                    texto= texto+cr;
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
                    return "PalavraReservada "+txt;
                }else{
                    return "Identificador "+txt;
                }
                
            case 2:
                return "Número "+txt;
                
            case 3:
                return "Número "+txt;
                
            case 4:
                return "Delimitador "+txt;
                
            case 5:
                return "Operador simples "+txt;
            case 7:
                return "Caracter constante "+txt;
                
            case 9:
                return "Cadeia constante "+txt;
            case 11:
                return "Operador duplo "+txt;//== != <= >= ||
            case 18:
                return "Comentário de linha "+txt;
            case 21:
                return "Comentário de bloco "+txt;
                
            case 98:
                return "Token mau formado "+txt;
                
            case 99:
                return "Token não identificado "+txt;
                 
            default:  
                return null;
                
        }     
    }
}
