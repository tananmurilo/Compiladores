
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mateus
 */
public class AnalizadorLexico {
    
     Automato automatoLexico = new Automato();
     String textoFinal="";//juntar todos os tokens em um texto só
     
     public String getTexto(){
         return this.textoFinal;
     }
    
    public LinkedList<String> gerarTokens(LinkedList<String> linhas){
        LinkedList<String> expressoes;
        LinkedList<String> tokens = new LinkedList<String>();
        expressoes = separarTokens(linhas);
        
       int linha = 0;
        
        automatoLexico = new Automato();
        for(int i=0; i< expressoes.size(); i++){
            tokens.add(automatoLexico.iniciar(expressoes.get(i), linha));
        }
        
    return tokens;    
    
    };
    
    /**
     * Metodo vare cada linha e vai separando os pedaços ao encontrar um delimitador, espaço ou quebra de linha
     * Ex: Entrada:int x, y;  ; 34. Saída: String[0]= 'int', String[1]= 'x', String[2]= ',',String[3]= 'y',String[4]= ';',String[5]= ';',String[6]= '34.',
     * @param linhas lista com as linha do arquivo
     * @return Um uma lista com cada sub string separada por um delimitador.
     */
    public LinkedList<String> separarTokens(LinkedList<String> linhas){
        LinkedList<String> expressoes = new LinkedList<String>();
        
        textoFinal="";//inicializar o texto vazio
        char last_char = ' ';
         
        /**vare todas as linhas e vai separando os tokens quando enconta um delimitador.
         * Idéia: a gente pode colocar para chamar o metodo do autonomo para analizar o token ao final de cada 
         * separação feita e sair com uma lista de token já devidamente identificado, e caso o toke sejá invalido
         * da pra pegar o numero da linha q ele está pq vai ser o indice do for.
         */
        for(int i=0; i<linhas.size(); i++){
          String l = linhas.get(i);
          //System.out.println(" linha "+i);
          String temp ="";
          int cont =0;
          
          while(l!=null && cont < l.length()){
              char a =  l.charAt(cont);
              //System.out.println("cont: "+cont+" tmax:"+ l.length());
              //System.out.println("Linha: "+i+" Caractere: "+a);
              
              //se a variavel não tiver vazia vai armazenado os caracteres na variavel
              if(!temp.isEmpty()){
                 
                  
                  //se encontrar um delimitador ou espaço, armazena o que tiver na variavel temp na lista de tokens e inicializa a temp com o delimitador
                  if(a==' ' || a==';' || a==',' || a=='(' || a==')' || a=='{' || a=='}' || a=='[' || a==']' ){
                      expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";    
                      }
                      
          
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      if(a!=' '){ //se o que tiver separando as strings for um delimitador ele entra para variavel temporaria para na proxima interação virar um token também, e o espaço é ignorado.
                          temp=temp+b;// adiciona o delimitador; 
                          aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                          System.out.println("Automato: "+aunt);//testes
                          if(aunt!=null){
                          textoFinal = textoFinal+aunt+" "+i+"\n";  
                           }
          
                            temp = ""; //limpa a variavel
                      } 
                  }else if((((last_char>=48)&&(last_char<=57))||(last_char>=65 && last_char<=90) || (last_char>=97&&last_char<=122))&&( a=='!'||a=='&'||a=='|')){ //separar os operadore ! & | quando vem letras ou numeros antes ex: n!=c 
                      expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";   
                      }
                      
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                      
                  }else if(((a=='+' || a=='-' || a=='=' || a=='.' || a=='*' || a=='/' || a=='>' || a=='<' ))&& ((last_char>=65 && last_char<=90) || (last_char>=97&&last_char<=122)) ){ //caso o operado seja precedido de caractere ele deve ser separado
                       
                      expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";    
                      }
                      
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                  
                    }else if(((a=='+' || a=='-' || a=='=' || a=='*' || a=='/' || a=='>' || a=='<' ))&& ((last_char>=48)&&(last_char<=57)) ){ //caso o operado seja precedido de numero ele deve ser separado
                        
                      expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";   
                      }
                      
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                      
                    }else if(((a>=48)&&(a<=57)) && ((last_char=='+' || last_char=='-' || last_char=='=' || last_char=='*' || last_char=='/' || last_char=='>' || last_char=='<' || last_char=='&' || last_char=='|')) ){ //caso o operador seja sucedido de numero ele deve ser separado

                       expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";  
                      }
                      
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                      
                      }else if(((a>=65 && a<=90) || (a>=97&&a<=122)) && ((last_char=='.' || last_char=='+' || last_char=='-' || last_char=='=' || last_char=='*' || last_char=='/' || last_char=='>' || last_char=='<' || last_char=='&' || last_char=='|' )) ){ //caso o operador seja sucedido de letra ele deve ser separado

                       expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";    
                      }
                      
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                        
                      
                  }else{ //se o caractere n for um delimitador ou espaço vai armazenando na string 
                      
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                  }
                  
                  if((cont+1) == l.length()){//indentifica quando for quebra de linha(uma string representa uma linha então se não houver mais caractere na string é uma quebra de linha)
                     
                      expressoes.add(temp); //adiciona o token na lista
                     
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);
                      if(aunt!=null){
                       textoFinal = textoFinal+aunt+" "+i+"\n";    
                      }   
                  }
                  
              }else{//se a variavel tiver vazia armazena o caracter eceto espaço.
                  if(a!=' '){
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                  }
              }
              last_char = a;
              cont++;
          }  
          
          
          cont=0;//zerar contador para o proximo while
        }
        /*Aqui serão separadas pelo espaço e pelos delimitadores cada esxpressao a ser traduzida em um token*/
        return expressoes;
    };
    
    
}
