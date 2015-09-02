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


/* produção das constantes no doc
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
