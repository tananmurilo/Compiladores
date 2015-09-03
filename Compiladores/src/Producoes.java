import java.util.List;
/**
 *
 * @author mateus
 */
public class Producoes {
    
   int head = 0;
   private List<String> tokenList;
   private List<String> valueList;
    
public Producoes(List<String> tokens, List<String> values){
    tokenList = tokens;
    valueList = values;
}  

/*Primarios falta fazer o numero
<Numero>::= <Digito><Inteiro> | <Digito> | <Inteiro>.<Inteiro>
<Inteiro>::= <Digito><Inteiro> 
<Digito>::= 0  | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
<Delimitadores>::= ; | , | ( | ) | { | } | [ | ]

*/


public boolean inteiro(){
    if(digito()){
        if(inteiro()){
            return true;
        }else if(tokenList.get(head).equals("Delimitador")){//delimitador seria um lambida
            return true;  
        }else return false;
    }else return false;
}

public boolean digito(){
    if(valueList.get(head).equals("0")||valueList.get(head).equals("1")||valueList.get(head).equals("2")||valueList.get(head).equals("3")
    ||valueList.get(head).equals("4")||valueList.get(head).equals("5")||valueList.get(head).equals("6")||valueList.get(head).equals("7")
    ||valueList.get(head).equals("8")||valueList.get(head).equals("9")){
        head++;
        return true;
    }else return false;
}

public boolean atr(){  //Incompleto
    if(tokenList.get(head).equals("Identificador")){
        head++;
        if(valueList.get(head).equals("="))
            head++;
            if(valorRetornado()) return true;
    }      
    
    return false;
}

public boolean valorRetornado(){ //Incompleto
        if(tokenList.get(head).equals("Identificador")) {
            head++;            
            return true;
        } else if(valores()) {  
            return true;
        }
        return false;
    }
/*
<Declara_vetor>::= [ <inteiro> ] <declara_mat>
<Declara_mat>::=[ <inteiro> ]  | ƛ


public boolean declaraVetor(){
    if(valueList.get(head).equals("[")){
        head++;
        if(inteiro()){
            
        }
    }
}
*/

public boolean valores(){ //Incompleto
        if(tokenList.get(head).equals("Numero")) {
            head++;            
            return true;
        } else if(tokenList.get(head).equals("Caracter")) {
            head++;            
            return true;
        } else if(tokenList.get(head).equals("Cadeia")) {
            head++;            
            return true;
        } else if(valueList.get(head).equals("verdadeiro")) {
            head++;            
            return true;
        } else if(valueList.get(head).equals("falso")) {
            head++;            
            return true;
        } 
        return false;
    }

/*<Inicialização>::= <ƛ> | =<X>
<X>::= <Identificador> | <Valores>
*/
// teste para =342; ou ;
public boolean inicializacao(){
    if(valueList.get(head).equals("=")){
        head++;
        return x(); 
    }else if(valueList.get(head).equals(",")||valueList.get(head).equals(";")){//coloquei como sendo o ƛ o encontro de um delimidor , ou ;, já que variaveis sem inicialização vem logo acompanhadas desses delimitadores
        return true;
    }else return false;
}
//<X>::= <Identificador> | <Valores>
public boolean x(){
    if(tokenList.get(head).equals("Identificador")|| valores()){
        head++;
        return true;
    }else return false;
}

/* produção das constanttes no doc
<Constantes>::= constantes{ <H> }
<H>::= <DC> | ƛ

<DC>::= <Tipo><Identificador>=<Valores>;<DCAcomp> 
<DCAcomp> ::= <DC> | ƛ
*/
public boolean constantes(){
    if(valueList.get(head).equals("constantes")){
        head++;
        if(valueList.get(head).equals("{")){
            head++;
            if(declaraConstante()||valueList.get(head).equals("}")){//o } no caso as constantes vão ser vazias
                head++;
                return true;
            }
        }
    } 
    return false;
}

//<DC>::= <Tipo><Identificador>=<Valores>;<DCAcomp> 
public boolean declaraConstante(){
     if(tipo()){//chama o metodo de identificar tipo
            if(tokenList.get(head).equals("Identificador")){
                head++;
                if(valueList.get(head).equals("=")){
                    head++;
                    if(valores()){//chama o metodo de valores
                        if(valueList.get(head).equals(";")){
                            head++;
                            return dCAcomp();
                        }else return false;
                    }else return false;
                }else return false;  
            }else return false;   
      }else return false;
}


//<DCAcomp> ::= <DC> | ƛ
public boolean dCAcomp(){//pode gerar outra declaração de constante ou encontrar o lambida
    if(declaraConstante()||valueList.get(head).equals("}")){
        return true;
    }else return false;
}


/*
<Tipo>::= inteiro | booleano | real | char | cadeia
*/
public boolean tipo(){
     if(valueList.get(head).equals("inteiro")||valueList.get(head).equals("booleano")||valueList.get(head).equals("real")||
        valueList.get(head).equals("char")||valueList.get(head).equals("cadeia")){
         head++;
         return true;
         
     }else return false;
}


/*gramatica
<Reg>::= registro<G>
<G>::=  <Identificador>{<F>}
<F>::=  <atributos> | ƛ

<atributos>::= <Tipo><Identificador>;<A>
<A>::= <atributos> | ƛ

*/

public boolean registro(){
    if(valueList.get(head).equals("registro")){
        head++;
        if(tokenList.get(head).equals("Identificador")){
            head++;
            if(valueList.get(head).equals("{")){
                head++;
                if(f()){
                    head++;
                    return true;
                }
            }
        }
    }
    return false;
}

//<F>::=  <atributos> | ƛ
public boolean f (){
    if(atributos()||valueList.get(head).equals("}")){
        return true;
    }else return false;
}

//<atributos>::= <Tipo><Identificador>;<A>
public boolean atributos(){
    if(tipo()){
        if(tokenList.get(head).equals("Identificador")){
            head++;
            if(valueList.get(head).equals(";")){
                head++;
                return a();
            }else return false;
        }else return false;
    }else return false;
}

//<A>::= <atributos> | ƛ
public boolean a(){
    if(atributos()||valueList.get(head).equals("}")){
        return true;
    }else return false;
}

}
