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
   
public boolean atr(int index){  //Incompleto
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
}
