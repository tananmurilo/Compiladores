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
    
    int estadoAtual = 0;
    String output = "";
    String texto="";
    String cr="";
    
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
        97 - caracter inválido (?@#$%_~^: etc)
        98 - token mal formado(pode ser dividi mais tarde em numero mal formado, identificado mal formado etc..)
        99 - caracter não encontrado/não pertence a linguagem
    */
    
    
    public String iniciar(String input, int linha){
        
        
        estadoAtual = 0;
        texto = "";
        cr = "";

        for(int i =0; i< input.length(); i++){
            
            char ch = input.charAt(i);
            
            if(estadoAtual==0){ //se o estado for inicial do automato 
               
                //identificar os primeiros estados aqui tipo, 1 2 3 4 ...cada um vai seguir para uma folha do automato.
                if((ch>=65 && ch<=90) || (ch>=97&&ch<=122)){ //letra a-z|A-Z
                    atualizaEstado(1,ch);//estado para identificador
                    
                }else if((ch>=48)&&(ch<=57)){ //numeros 0-9
                    atualizaEstado(2,ch);//estado para numeros
                    
                }else if(ch==' ' || ch==';' || ch==',' || ch=='(' || ch==')' || ch=='{' || ch=='}' || ch=='[' || ch==']' ){//delimitadores
                    atualizaEstado(4,ch); //estado para delimitadores
                
                }else if(ch=='+' || ch=='-' || ch=='=' || ch=='.' || ch=='*'|| ch=='>' || ch=='<' ){
                    if((ch=='>' || ch=='<' || ch=='=')&&(i+1 < input.length())){
                        atualizaEstado(14,ch); //estado para operadores <=, >= e ==

                    }else if((ch=='-')&&(i+1 < input.length())){
                        atualizaEstado(15,ch); //estado para operadores --

                    }else if((ch=='+')&&(i+1 < input.length())){
                        atualizaEstado(16,ch); //estado para operadores ++

                    }else{
                        atualizaEstado(5,ch); //estado para operadores de um caractere

                    }
                   
                    
                }else if(ch==39){//caracter constante ''
                    atualizaEstado(6,ch); 

                }else if(ch==34){ //cadeia constante ""
                    atualizaEstado(8,ch); 

                }else if(ch==33){ // operador !=
                    atualizaEstado(10,ch); //erro

                }else if(ch==38){ // operador &&
                    atualizaEstado(12,ch);
                    
                }else if(ch==124){ // operador ||
                    atualizaEstado(13,ch);
                    
                }else if(ch=='/'){ // barra
                   
                    if(i+1<input.length()){
                       if( input.charAt(i+1)=='/' ||  input.charAt(i+1)=='*'){
                           atualizaEstado(17,ch); 
                           
                        } 
                    }else{
                        atualizaEstado(5,ch); //operador
                        
                    }  
                }else if(ch<32 || ch=='_' || ch=='#' || ch=='$' || ch=='%' || ch==':' || ch=='?' || ch=='@' || ch=='^' || ch=='`' || ch=='~' ||ch==92 || ch>126){
                    atualizaEstado(97,ch);
                }else{
                    atualizaEstado(99,ch); //erro
                    
                }
                
            }else if(estadoAtual==1){ //identificadore e palavras reservadas
               
                if((ch>=65 && ch<=90) || (ch>=97&&ch<=122) || (ch>=48&&ch<=57)||(ch==95)){ //letra a-z|A-Z numero e _
                    atualizaEstado(1,ch); 
                 
                }else{//erro se o caractere lido não for letra numero ou _
                   atualizaEstado(99,ch); //erro
                }
                
            }else if(estadoAtual==2){ //numeros
                if((ch>=48)&&(ch<=57)){ // numero 
                    atualizaEstado(2,ch); 

                }else if(ch==46){
                    if (i+1 == input.length()) {//caso não tenha mais caractere depois do .
                        atualizaEstado(98,ch); //erro
                        
                    }else{
                        atualizaEstado(3,ch); //numero

                    }
                }else{
                    atualizaEstado(99,ch); //erro
                }
            
            }else if(estadoAtual==3){
                if((ch>=48)&&(ch<=57)){ //numero 
                    atualizaEstado(3,ch); 
                   
                }else{
                    atualizaEstado(98,ch); //erro
                }
                
            }else if(estadoAtual==6){ //caracter constante
                if(((ch>=32&&ch<=38)||(ch>=40&&ch<=126))){ //ver se é um caracter valido
                    atualizaEstado(7,ch); //estado que identifica se o prox caracter é '

                }else{
                    atualizaEstado(98,ch); //erro
                }   
            }else if(estadoAtual==7){//caracter constante parte final
                if((ch==39)&&(i+1 == input.length())){//se o caracter for ' é o fim do caracter constante caso contrario é error
                    atualizaEstado(7,ch); 
                     
                }else{
                    atualizaEstado(98,ch); //erro
                }   
                
            }else if(estadoAtual==8){//cadeia  constante parte final
                if(((ch>=32&&ch<=33)||(ch>=35&&ch<=126))&& i+1 < input.length()){ //ver se é um caracter valido.
                    atualizaEstado(8,ch); //cadeia constante
                   
                }else if(ch==34){//identifica o final da cadeia
                    atualizaEstado(9,ch);
       
                }else{
                    atualizaEstado(98,ch); //erro
                }  
                
            }else if(estadoAtual==10){
                if(ch=='='&&(i+1 == input.length())){
                    atualizaEstado(11,ch); //operador duplo
                }else{
                    atualizaEstado(98,ch); //erro
                }
            }else if(estadoAtual==12){
                if(ch=='&'&&(i+1 == input.length())){
                    atualizaEstado(11,ch); //operador duplo
 
                }else{
                    atualizaEstado(98,ch); //erro
                }
            }else if(estadoAtual==13){
                if(ch=='|'&&(i+1 == input.length())){
                    atualizaEstado(11,ch); //operador duplo
                   
                }else{
                    atualizaEstado(98,ch); //erro
                }
            }else if(estadoAtual==14){
                if(ch=='='&&(i+1 == input.length())){
                    atualizaEstado(11,ch); //operador duplo
                   
                }else{
                    atualizaEstado(98,ch); //erro
                }
            }else if(estadoAtual==15){
                if(ch=='-'&&(i+1 == input.length())){
                    atualizaEstado(11,ch); //operador duplo

                }else{
                    atualizaEstado(98,ch); //erro
    
                }
            }else if(estadoAtual==16){
                if(ch=='+'&&(i+1 == input.length())){
                    atualizaEstado(11,ch); //operador duplo

                }else{
                    atualizaEstado(98,ch); //erro

                }
            }else if(estadoAtual==17){
                
                if(ch=='/'){
                    atualizaEstado(18,ch); //comentario de linha

                }else if(ch=='*'){
                    atualizaEstado(19,ch); //comentario de bloco
 
                }else{
                     atualizaEstado(98,ch); //erro
                    
                }
            }else if(estadoAtual==18){
               
                 atualizaEstado(18,ch); //comentario de linha
                 /*
                }else{
                     atualizaEstado(98,ch);
                }
                */
                
            }else if(estadoAtual==19){
                 System.out.println(ch);
                if(ch=='*'){
                   
                    atualizaEstado(20,ch); //
                   
                }else {
                    atualizaEstado(19,ch); //Coementario de bloco estado 20
                    
                }
                
            }else if(estadoAtual==20){
 
                if(ch=='/'){
                   atualizaEstado(21,ch); //cometario de bloco estado final
                   
                }else if(ch=='*'){
                    atualizaEstado(20,ch);//fica no 20 enquanto ler *
                    
                }else {
                    atualizaEstado(19,ch);//volta pro 19 caso ler um caractere
                 
                }
                
            }else if(estadoAtual==97){//estado erro caracter inválido
                atualizaEstado(97,ch);
                
            }else if(estadoAtual==98){//estado error de token mau formado
                 atualizaEstado(98,ch);
                
            }else if(estadoAtual==99){ //estado error de token/caracteres não identificado/não pertencem a linguagem
                atualizaEstado(99,ch);
            }else{
                 atualizaEstado(99,ch);
                
            }
            
        }
        //pega o toke + seu identificador
        output = getToken(texto, estadoAtual, linha);
        
        return output;
    };
    
    
    /**
     * Atualiza o estado do automato
     * @param proxEstado proximo estado que o automato deve ir
     * @param caracterLido caractere lido no estado atual
     */
    private void atualizaEstado(int proxEstado, char caracterLido){
            estadoAtual = proxEstado; //comentario de bloco
            cr = String.valueOf(caracterLido);
            texto= texto+cr; 
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
                
            case 19:
                return "Comentário mal fechado "+txt;
                
            case 20:
                return "Comentário mal fechado "+txt;    
                
            case 21:
                return "Comentário de bloco "+txt;
            case 97:
                return "Erro de símbolo "+txt;
                
            case 98:
                return "Token mal formado "+txt;
                
            case 99:
                return "Token não identificado "+txt;
                 
            default:  
                return null;
                
        }     
    }
}
