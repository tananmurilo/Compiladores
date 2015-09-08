
import java.util.LinkedList;
import java.util.List;

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
     List<String> Tokens =  new LinkedList<>();
     String textoFinal="";//juntar todos os tokens em um texto só
     Boolean numeroFloat = false;
     private boolean erro = false;
     
     public boolean getErro(){
         return this.erro;
     }
     public String getTexto(){
         return this.textoFinal;
     }
     
     public List<String> getTokens(){
         return this.Tokens;
     }
      
      public void setTextoFinal(String aunt, int i){
        int linha = i+1;   
        textoFinal = textoFinal+aunt+" "+linha+"\n";
        Tokens.add(aunt+" "+linha);
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
               System.out.println(a+"     >"+temp+"<");
               System.out.println(numeroFloat);
               
               
                   //identificar comentarios e cadeia constante antes de quebrar o texto em pedacinhos.
                if(a=='/'||a==34 ||a==39){ //se encontrar uma / verificar se é comentario e se for " verificar se é cadeia constante
                      
                      if(a=='/'){ // se uma barra entra na logica de procurar comentarios
                          if(cont+1< l.length()&&(l.charAt(cont+1)=='/')){ //comentario de linha ver se o prox caracter é um barra

                              
                                expressoes.add(temp); //adiciona o token na lista
                                String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                System.out.println("Automato: "+aunt);//testes
                                if(aunt!=null){
                                    setTextoFinal(aunt, i);  
                                  
                                }
                              
                                 temp = ""; 
                              
                              
                              while(l!=null && cont < l.length()){ //vare toda a linha e coloca o tiver nela na variavel
                                    a =  l.charAt(cont);  
                                    String b = String.valueOf(a);
                                    temp= temp+b;
                                    cont++;
                              }
                                 temp = enviarToken(expressoes, temp, i, a);

                         }else if(cont+1< l.length()&&(l.charAt(cont+1)=='*')){//comentario de bloco ver se o prox caracter é um *
                              boolean fim = false;
                              int linhaInicial  = i;
                              expressoes.add(temp); //adiciona o token na lista
                                String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                System.out.println("Automato: "+aunt);//testes
                                if(aunt!=null){
                                    setTextoFinal(aunt, i);  
                                  
                              }
                              temp = "/*"; 
                              cont= cont+2;
                              while(!fim && i<linhas.size() && linhas.get(i)!=null){ //ir linha por linha até achar o fim do comentario
                                  l = linhas.get(i);
                                  System.out.println("tamanho da linha:"+linhas.size());
                                  System.out.println("cont:"+i);
                                  System.out.println("valor de l:"+l);
                                  
                                  
                                  while(l!=null && cont < l.length()&&(!fim)){ //vare toda a linha e coloca o tiver nela na variavel até encontrar o fechamento de comentario
                                    a =  l.charAt(cont);
                                    
                                    if(cont+1 == l.length()){
                                       String b = String.valueOf(a);
                                       temp= temp+b;
                                       cont++;
                                    }else if(a=='*'&&(l.charAt(cont+1)=='/')){//final do comentario de bloco
                                        //System.out.println("FIM");
                                         fim = true;
                                            //separa e envia pro automato o que já tinha na var temp
                                         temp = temp+"*/";
                                         cont = cont +2;
                                         expressoes.add(temp); //adiciona o token na lista
                                         aunt = automatoLexico.iniciar(temp, linhaInicial);//analizar o token no autonomo
                                         System.out.println("Automato: "+aunt);//testes
                                         if(aunt!=null){
                                             textoFinal = textoFinal+aunt+" "+linhaInicial +"\n";   
                                         }
                                            
                                         temp = ""; 
                                    }else{
                                       
                                       String b = String.valueOf(a);
                                       temp= temp+b;
                                       cont++;  
                                    }
                                    
                                  }
                                  
                                  if(cont>=l.length()&&!fim){//final de linha
                                       //System.out.println("if fim de linha");
                                        cont = 0;
                                        temp= temp+" ";//quebra de linha concatenar a proxima linha com espaço, caso queira pode-se colocar \n pra concatenar com enter
                                        i++; 
                                            
                                         
                                  }
                                  
                                  
                              }
                              
                              if( linhas.get(i)==null){
                                  textoFinal = textoFinal+"Comentário de bloco não fechado "+temp+" "+i+"\n"; 
                              }
                              
                          }else{
                                if(!temp.isEmpty()){
                                   expressoes.add(temp); //adiciona o token na lista
                                   String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                   System.out.println("Automato: "+aunt);//testes
                                   if(aunt!=null){
                                       setTextoFinal(aunt, i);  

                                   }
                                   temp = "/";

                               }else{
                                   temp = "/";
                               }
                         }
                      }else if(a==34){//cadeia constante
                            if(!temp.isEmpty()){
                                expressoes.add(temp); //adiciona o token na lista
                                String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                System.out.println("Automato: "+aunt);//testes
                                if(aunt!=null){
                                    setTextoFinal(aunt, i);  
                                  
                                }
                                temp = ""; 
                            }
                            a =  l.charAt(cont);
                            String b = String.valueOf(a);
                            temp= temp+b; //add aspas no temp
                            boolean fim = false;  
                            cont++;
                            System.out.println(temp);
                            while(l!=null && cont < l.length()&&!fim){ //vare a linha e coloca o que tiver antes das aspas ou da quebra de linha na variavel temp
                                    a =  l.charAt(cont); 
                                    if(a==34){  
                                         
                                        b = String.valueOf(a);
                                        temp= temp+b; //add aspas no temp
                                        expressoes.add(temp); //adiciona o token na lista
                                        String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                        System.out.println("Automato: "+aunt);//testes
                                        if(aunt!=null){
                                            setTextoFinal(aunt, i);  

                                        }
                                        System.out.println(temp);
                                        fim=true;
                                        temp = ""; 
                                    }else{
                                        b = String.valueOf(a);
                                        temp= temp+b;
                                        cont++;
                                        
                                    }
                                    System.out.println(temp);
                                    
                                    
                            }
                            if(!fim && cont == l.length()){
                                expressoes.add(temp); //adiciona o token na lista
                                String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                System.out.println("Automato: "+aunt);//testes
                                if(aunt!=null){
                                    setTextoFinal(aunt, i);  
                                  
                                }
                                temp = ""; 
                            }
                                
                      }else if(a==39){//caracter constante
                            if(!temp.isEmpty()){
                                expressoes.add(temp); //adiciona o token na lista
                                String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                System.out.println("Automato: "+aunt);//testes
                                if(aunt!=null){
                                    setTextoFinal(aunt, i);  
                                  
                                }
                                temp = ""; 
                            }
                            a =  l.charAt(cont);
                            String b = String.valueOf(a);
                            temp= temp+b; //add aspas no temp
                            boolean fim = false;  
                            cont++;
                            System.out.println(temp);
                            while(l!=null && cont < l.length()&&!fim){ //vare a linha e coloca o que tiver antes das aspas ou da quebra de linha na variavel temp
                                    a =  l.charAt(cont); 
                                    if(a==39){  
                                         
                                        b = String.valueOf(a);
                                        temp= temp+b; //add aspas no temp
                                        expressoes.add(temp); //adiciona o token na lista
                                        String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                        System.out.println("Automato: "+aunt);//testes
                                        if(aunt!=null){
                                            setTextoFinal(aunt, i);  

                                        }
                                        System.out.println(temp);
                                        fim = true;
                                        temp = ""; 
                                    }else{
                                        b = String.valueOf(a);
                                        temp= temp+b; 
                                        cont++;
                                        
                                    }
                                    System.out.println(temp);
                                    
                                    
                            }
                            if(!fim && cont == l.length()){
                                expressoes.add(temp); //adiciona o token na lista
                                String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                                System.out.println("Automato: "+aunt);//testes
                                if(aunt!=null){
                                    setTextoFinal(aunt, i);  
                                  
                                }
                                temp = ""; 
                            }
                                
                      }else{
                          
                      } //fecha chave do if que separa comentarios e cadeia constante                    
                }else if(!temp.isEmpty() && (a != '\t')){//se a variavel não tiver vazia vai armazenado os caracteres na variavel
                    //se encontrar um caractere que não pertence a linguagem ou representa nada, tratar como um delimitador e gerar o erro
                    
                    if(a<32 || a=='#' || a=='$' || a=='%' || a==':' || a=='?' || a=='@' || a=='^' || a=='`' || a=='~' ||a==92 || a>126){
                        temp = enviarToken(expressoes, temp, i, a);//chamar a função para separar o token que havia na string antes do caractere encontrado
                        //analizar o caracter
                        expressoes.add(temp); //adiciona o token na lista 
                        String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                        if(aunt!=null){
                           setTextoFinal(aunt, i);  
                        }
                        temp = "";
                   
                    }else if(a==' ' || a==';' || a==',' || a=='(' || a==')' || a=='{' || a=='}' || a=='[' || a==']' ){//se encontrar um delimitador ou espaço, armazena o que tiver na variavel temp na lista de tokens e inicializa a temp com o delimitador
                        expressoes.add(temp); //adiciona o token na lista
                        String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                        System.out.println("Automato: "+aunt);//testes
                        if(aunt!=null){
                         setTextoFinal(aunt, i);   
                        }


                        temp = ""; //limpa a variavel
                        String b = String.valueOf(a);
                      if(a!=' '){ //se o que tiver separando as strings for um delimitador ele entra para variavel temporaria para na proxima interação virar um token também, e o espaço é ignorado.
                          temp=temp+b;// adiciona o delimitador; 
                          aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                          System.out.println("Automato: "+aunt);//testes
                          if(aunt!=null){
                          setTextoFinal(aunt, i); 
                           }
          
                            temp = ""; //limpa a variavel
                      } 
                    }else if((((last_char>=48)&&(last_char<=57))||(last_char>=65 && last_char<=90) || (last_char>=97&&last_char<=122))&&( a=='!'||a=='&'||a=='|')){ //separar os operadore ! & | quando vem letras ou numeros antes ex: n!=c 
                      
                      temp = enviarToken(expressoes, temp, i, a);
                      
                    }else if(((a=='+' || a=='-' || a=='=' || a=='.' || a=='*' || a=='/' || a=='>' || a=='<' ))&& ((last_char>=65 && last_char<=90) || (last_char>=97&&last_char<=122)) ){ //caso o operado seja precedido de caractere ele deve ser separado
                       
                      temp = enviarToken(expressoes, temp, i, a);
                  
                    }else if(((a=='+' || a=='-' || a=='=' || a=='*' || a=='/' || a=='>' || a=='<' ))&& ((last_char>=48)&&(last_char<=57)) ){ //caso o operado seja precedido de numero ele deve ser separado
                        
                      temp = enviarToken(expressoes, temp, i, a);
                      
                    }else if(a=='.'&&last_char=='.'){//pontos seguido de ponto
                        temp = enviarToken(expressoes, temp, i, a);
                    }else if(((a>=48)&&(a<=57)) && ((last_char=='+' || last_char=='-' || last_char=='=' || last_char=='*' || last_char=='/' || last_char=='>' || last_char=='<' || last_char=='&' || last_char=='|')) ){ //caso o operador seja sucedido de numero ele deve ser separado
                        
                      numeroFloat=false;
                      temp = enviarToken(expressoes, temp, i, a);
                      
                    }else if(((a>=65 && a<=90) || (a>=97&&a<=122)) && ((last_char=='.' || last_char=='+' || last_char=='-' || last_char=='=' || last_char=='*' || last_char=='/' || last_char=='>' || last_char=='<' || last_char=='&' || last_char=='|' )) ){ //caso o operador seja sucedido de letra ele deve ser separado

                      temp = enviarToken(expressoes, temp, i, a);
                        
                    }else if(a=='.' && ((last_char>=48)&&(last_char<=57))){
                        
                        if(!numeroFloat) {
                            
                            if(cont+1 < l.length()){
                                char prox = l.charAt(cont+1);
                                if((prox>=48)&&(prox<=57)){//se tiver número na sequencia
                                    String b = String.valueOf(a);
                                    temp= temp+b;// concatenar com os caracteres;
                                    numeroFloat = true;  
                                }else{
                                     temp = enviarToken(expressoes, temp, i, a);
                                    numeroFloat = false; 
                                }
                            }else{//se tiver qualquer coisa diferente de um numero na sequencia
                                temp = enviarToken(expressoes, temp, i, a);
                                numeroFloat = false; 
                            }
                             
                        }else {
                              temp = enviarToken(expressoes, temp, i, a);
                              numeroFloat = false;
                        }
                   
                    }else if(((a>=48)&&(a<=57)) && last_char=='.' && temp.equals(".")){     
                      temp = enviarToken(expressoes, temp, i, a);
                        
                    }else if(((a>=48)&&(a<=57)) && last_char!='.'){ 
                        String b = String.valueOf(a);
                        temp= temp+b;// concatenar com os caracteres;
                        numeroFloat=false;
                        
                    }else{  String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                    }
                    
                  
                if((cont+1) == l.length()){//indentifica quando for quebra de linha(uma string representa uma linha então se não houver mais caractere na string é uma quebra de linha)
                     
                      expressoes.add(temp); //adiciona o token na lista
                     
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);
                      if(aunt!=null){
                       setTextoFinal(aunt, i);   
                      }   
                }
                
                  
            }else{//se a variavel tiver vazia armazena o caracter eceto espaço.
                    //se for um caractere fora da linguagem separar o token, error caracter invalido
                 if((cont+1) == l.length()){//indentifica quando for quebra de linha
                     if(a!=' '){
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                      expressoes.add(temp); //adiciona o token na lista
                     
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);
                      if(aunt!=null){
                       setTextoFinal(aunt, i);   
                      }   
                      numeroFloat=false;
                    }
                      
                 }else if(a == '\t'){
                     
                     
                     
                     
                 } else if(a=='_' || a<32 || a=='#' || a=='$' || a=='%' || a==':' || a=='?' || a=='@' || a=='^' || a=='`' || a=='~' ||a==92 || a>126){
                    String b = String.valueOf(a);
                    temp= temp+b;//
                    expressoes.add(temp); //adiciona o token na lista
                    String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                    if(aunt!=null){
                       setTextoFinal(aunt, i);  
                    }
                      
                    temp = ""; //limpa a variavel
                    
                 } else if(a==' ' || a==';' || a==',' || a=='(' || a==')' || a=='{' || a=='}' || a=='[' || a==']' ){//delimitadores
                       
                    String b = String.valueOf(a);
                    temp= temp+b;//
                    expressoes.add(temp); //adiciona o token na lista
                    String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                    if(aunt!=null){
                       setTextoFinal(aunt, i);  
                    }

                    temp = ""; //limpa a variavel                        
                     
                     
                } else if(a!=' '){
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                      numeroFloat=false;
                 }
            }
              last_char = a;
              cont++;
          }  
          
          
          cont=0;//zerar contador para o proximo while
        }
        
        if(!(textoFinal.contains("Token mal formado") || textoFinal.contains("Token não identificado") || textoFinal.contains("Erro de símbolo")   || textoFinal.contains("Comentário de bloco não fechado")))  {
            textoFinal = textoFinal + "\n Sucesso! Nenhum erro encontrado na analise léxica";
            erro=false;
        } else{  textoFinal = textoFinal + "\nHá erros encontrados na analise léxica";
        erro=true;
        }
        /*Aqui serão separadas pelo espaço e pelos delimitadores cada esxpressao a ser traduzida em um token*/
        return expressoes;
    };
    
    private String enviarToken(LinkedList<String> expressoes, String temp, int i, char a){
        
                       
        
                      expressoes.add(temp); //adiciona o token na lista
                      String aunt = automatoLexico.iniciar(temp, i);//analizar o token no autonomo
                      System.out.println("Automato: "+aunt);//testes
                      if(aunt!=null){
                       
                       setTextoFinal(aunt, i);  
                      }
                                                    
                      temp = ""; //limpa a variavel
                      String b = String.valueOf(a);
                      temp= temp+b;// concatenar com os caracteres;
                      
                      return temp;
    }
}
