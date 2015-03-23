
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
    
    public LinkedList<String> gerarTokens(LinkedList<String> linhas){
        LinkedList<String> expressoes;
        LinkedList<String> tokens = new LinkedList<String>();
        expressoes = separarTokens(linhas);
        
        Automato automatoLexico = new Automato();
        for(int i=0; i< expressoes.size(); i++){
            tokens.add(automatoLexico.iniciar(expressoes.get(i)));
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
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      if(a!=' '){ //se o que tiver separando as strings for um delimitador ele entra para variavel temporaria para na proxima interação virar um token também, e o espaço é ignorado.
                          temp=temp+b;// adiciona o delimitador; 
                      } 
                  }else{ //se o caractere n for um delimitador ou espaço vai armazenando na string 
                       
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                  }
                  if((cont+1) == l.length()){//indentifica quando for quebra de linha(uma string representa uma linha então se não houver mais caractere na string é uma quebra de linha)
                      expressoes.add(temp); //adiciona o token na lista
                      temp = ""; //limpa a variavel
                  }
                  
              }else{//se a variavel tiver vazia armazena o caracter eceto espaço.
                  if(a!=' '){
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                  }
              }
              cont++;
          }  
          
          cont=0;//zerar contador para o proximo while
        }
        /*Aqui serão separadas pelo espaço e pelos delimitadores cada esxpressao a ser traduzida em um token*/
        return expressoes;
    };
    
    
}
