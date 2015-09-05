import java.util.List;
/**
 *
 * @author mateus
 */
public class Producoes {
    
   int head = 0;
   private List<String> tokenList;
   private List<String> valueList;
   private List<String> linePositions;
    
public Producoes(List<String> tokens, List<String> values, List<String> lines){
    tokenList = tokens;
    valueList = values;
    linePositions = lines;
}  

private boolean imprimeErro(String erro){
    System.out.println(erro + " na linha " + linePositions.get(head));
    return false;
};

/*
<iniciar>::=<registro><iniciar> | <constantes><variaveis><iniciar2>
<iniciar2>::=<funcao><iniciar2> | <algoritmo>
<algoritmo>::=algoritmo { <CG> }

*/

public boolean iniciar(){
    if(registro()){
        return iniciar();
    } else if(constantes()){
         if(variaveis()) return iniciar2();       
         else imprimeErro("Bloco variaveis mal formatado");
    } else  imprimeErro("Bloco constantes mal formatado");
   return false;
}

public boolean iniciar2(){
    if(algoritmo())return true;
    else if(funcao()){
        return iniciar2();
    }
    return false;
}

public boolean algoritmo(){
    if(valueList.get(head).equals("algoritmo")){
        head++;
        if(valueList.get(head).equals("{")){
            head++;
            codigoGeral();
            if(valueList.get(head).equals("}")){//o } no caso as constantes vão ser vazias
                head++;
                return true;
            }
        }
    return imprimeErro("Bloco algoritmo mal formatado");    
    } 
    return false;
}

public boolean codigoGeral(){ // incompleto
  
    return true;
}

public boolean inteiro(){
    if(tokenList.get(head).equals("Numero")) {
        float valor = Float.parseFloat(valueList.get(head));
        if((valor%1) == 0){
            head++;
            return true;
        } else return imprimeErro("Tipos incompatíveis: flutuante aonde deveria ser inteiro");
    }
    return imprimeErro("Tipos incompatíveis: ineteiro esperado");
}

public boolean atr(){  
    if(tokenList.get(head).equals("Identificador")){
        if(valueList.get(head+1).equals(".")){
            if(aceReg()) {
                if(valueList.get(head).equals("=")){
                    head++;
                    if(valorRetornado()) return true;
                }
            } 
        } else{
        head++;
            if(valueList.get(head).equals("=")){
                head++;
                if(valorRetornado()) return true;
            }
        }
    }       
    
    return imprimeErro("Erro: atribuição mal formada");
}

public boolean valorRetornado(){ 
        if(tokenList.get(head).equals("Identificador")) {
            if(valueList.get(head+1).equals(".")){ // lendo à frente para decidir em qual função entrar
                if(aceReg()) {
                    if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
                    }
                } 
            } if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
                if(operacoes()) {
                    if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
                    }
                } 
            }if(valueList.get(head+1).equals("(")){ // lendo à frente para decidir em qual função entrar
                return chamadaFuncao();
            } else{
                head++;            
                if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
                }
            }
        } else if(tokenList.get(head).equals("Numero")) {  
           if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
                if(operacoes()) {
                    if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
                    }
                } 
            } else {
                head++;
                if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
                }
            }
        } else if(valores()) {  
            if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
            }
        }
        return imprimeErro("Erro: um valor deve ser atribuido");
    }
/*
<Declara_vetor>::= [ <inteiro> ] <declara_mat>
<Declara_mat>::=[ <inteiro> ]  | ƛ

*/

public boolean declaraVetor(){
    if(valueList.get(head).equals("[")){
        head++;
        if(inteiro()){
            if(valueList.get(head).equals("]")){
                head++;
                return(declaraMatriz());
            }
        }
        return imprimeErro("Erro ao declarar vetor");
    }
    
    return false;
}

public boolean declaraMatriz(){
    if(valueList.get(head).equals("[")){
        head++;
        if(inteiro()){
            if(valueList.get(head).equals("]")){
                head++;
                return true;
            }
        }
      return imprimeErro("Erro ao declarar vetor");
    } else return true;
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

/*<Inicialização>::= <ƛ> | =<X>
<X>::= <Identificador> | <Valores>
*/
// teste para =342; ou ;
public boolean inicializacao(){
    if(valueList.get(head).equals("=")){
        head++;
        return x(); 
    }else return true; // caso lambda
        
}
//<X>::= <Identificador> | <Valores>
public boolean x(){
    if(tokenList.get(head).equals("Identificador")){
        head++;
        return true;
    }else return(valores());
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
            declaraConstante();
            if(valueList.get(head).equals("}")){//o } no caso as constantes vão ser vazias
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
                    return true;
                }
            }
        }
    }
    return false;
}

//<F>::=  <atributos> | ƛ
public boolean f (){
    atributos();
    if(valueList.get(head).equals("}")){
        head++;
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

/* produção das variaveis no doc
<Variaveis>::= variaveis { <R> }
<R>::= <DV> | ƛ

<DV>::= <Tipo><Identificador><AcompDv> |  <Identificador><Identificador>;<F> 
<AcompDV> ::= <Inicializacao>;<F> | <Declara_vetor>;<F>
<F>::= <DV> | ƛ
*/

public boolean variaveis(){
    if(valueList.get(head).equals("variaveis")){
        head++;
        if(valueList.get(head).equals("{")){
            head++;
            declaraVariaveis();          
            if(valueList.get(head).equals("}")){//o } no caso as constantes vão ser vazias
                head++;
                return true;
            }
        }
    } 
    return false;
}

public boolean declaraVariaveis(){
     if(tipo()){//chama o metodo de identificar tipo
            if(tokenList.get(head).equals("Identificador")){
                head++;
                if(AcompDV()) return true;
            }
                
      } else if(valueList.get(head).equals("}")) return true;
     
    return imprimeErro("Erro ao declarar variáveis");
}

public boolean AcompDV(){
    if (declaraVetor()){
        if(valueList.get(head).equals(";")){
            head++;
            declaraVariaveis();
            return true;
        }
    } else if(inicializacao()){
        if(valueList.get(head).equals(";")){
            head++;
            declaraVariaveis();
            return true;
        }
    } 
    return false;
}

/*
Função:

	<Funcao>::= funcao <funcaoAcomp>

<funcaoAcomp> ::= vazio <Identificador>(<vazioAcomp> |
		         <Tipo> <Identificador>(<tipoAcomp>

<vazioAcomp> ::= <parametro>){<CG>} | ){<CG>}

<tipoAcomp> ::=  <parametro>){<CG> retorno <Valores>;} | ){<CG> retorno <Valores>}

<parametro>::=<Tipo><Identificador><parametroAcomp>
                         registro <Identificador><parametroAcomp>

<parametroAcomp> ::= <parametro2> | ƛ

<parametro2>::= ,<parametro>
*/

public boolean funcao(){
    if(valueList.get(head).equals("funcao")){
        head++;
        if(valueList.get(head).equals("vazio")){
            head++;
            if(tokenList.get(head).equals("Identificador")){
                head++;
                if(valueList.get(head).equals("(")){
                    head++;
                    if(funcao_vazioAcomp()) return true;
                }   
            }    
        } else if(tipo()){
            if(tokenList.get(head).equals("Identificador")){
                head++;
                if(valueList.get(head).equals("(")){
                    head++;
                    if(funcao_tipoAcomp()) return true;
                }
            }  
        }
    }
        return imprimeErro("Erro: Função mal formada");
} 

private boolean funcao_vazioAcomp(){
    if(valueList.get(head).equals(")")){
        head++;
         if(valueList.get(head).equals("{")){
             head++;
             if(codigoGeral()){
                 if(valueList.get(head).equals("}")){
                    head++;
                    return true;
                 }
             }
         }   
    }   else if(parametro()){
        if(valueList.get(head).equals(")")){
            head++;
             if(valueList.get(head).equals("{")){
                 head++;
                 if(codigoGeral()){
                     if(valueList.get(head).equals("}")){
                        head++;
                        return true;
                     }
                 }
             }   
        }   
    }
    return false;
}

private boolean funcao_tipoAcomp(){
    if(valueList.get(head).equals(")")){
        head++;
        if(valueList.get(head).equals("{")){
            head++;
            if(codigoGeral()){
                if(valueList.get(head).equals("retorno")){
                   head++;
                    if(valores())
                        if(valueList.get(head).equals(";")){
                            head++;
                            if(valueList.get(head).equals("}")){
                                head++;
                                return true;
                            }
                        }
                }  
            }
            return imprimeErro("Erro: função sem retorno");
         }   
    }   else if(parametro()){
        if(valueList.get(head).equals(")")){
            head++;
            if(valueList.get(head).equals("{")){
                 head++;
                 if(codigoGeral()){
                    if(valueList.get(head).equals("retorno")){
                        head++;
                        if(valores())
                            if(valueList.get(head).equals(";")){
                            head++;
                                if(valueList.get(head).equals("}")){
                                    head++;
                                    return true;
                                }
                            }
                    }
                    
                }
                return imprimeErro("Erro: função sem retorno");
             }   
        }   
    }
    return false;
}

private boolean parametro(){ // incompleto
    return true;
}

public boolean condicao(){//falta fazer
 
    return true;
}

public boolean chamadaFuncao(){//falta fazer
    if(tokenList.get(head).equals("Identificador")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            parametroCF();
            if(valueList.get(head).equals(")")){
                head++;
                if(valueList.get(head).equals(";")) {
                    head++;
                    return true;
                }
            }
        }
    }
    return imprimeErro("Erro: chamada de função mal formada");
}

private boolean parametroCF(){ // incompleto
    if(operando()){
        if(valueList.get(head).equals(",")){
        head++;
        return parametroCF();
        }
    return true;    
    }
    return imprimeErro("Erro: parametro de chamada função inválido");
}
//Comandos 

/*
<leia>::= leia(<DL>);
<DL>::= <Identificador><D> | <AceReg><D> | <AceVet><D> | <AceMat><D>
<D>::= <DL2> | ƛ
<DL2>::= ,<DL>
*/

public boolean leia(){
    if(valueList.get(head).equals("leia")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            if(dL()){
                if(valueList.get(head).equals(")")){
                    head++;
                    if(valueList.get(head).equals(";")){
                        head++;
                        return true;
                    } 
                }
            }
            
        }
    }
    return imprimeErro("Comando leia mal formatado");
}

/*
<DL>::= <Identificador><D> | <AceReg><D> | <AceVet><D> | <AceMat><D>
<D>::= <DL2> | ƛ
<DL2>::= ,<DL>
*/
private boolean dL(){
    if(tokenList.get(head).equals("Identificador")){ //lendo à frente
        if(valueList.get(head+1).equals(".")){//lendo à frente
            if(aceReg()) return d();
        }else if(valueList.get(head+1).equals("[")){//lendo à frente
            if(aceVM()) return d();
        }else{
            head++;
            return d();
        }
    }
    return imprimeErro("Erro: parametro(s) mal formatado(s)");
}
//<D>::= <DL2> | ƛ
//<DL2>::= ,<DL>
private boolean d(){
    if(valueList.get(head).equals(",")){
        head ++;
        return dL();
    }else return true;  //lambda 
}

/*
<escreva>::= escreva(<DE>);
<DE>::= <Identificador><E> | <AceReg><E> | <AceVet><E> | <AceMat><E> | <CadeiasConstantes><E> | <CaracteresConstantes><E>
<E>::= <DE2> | ƛ
<DE2>::= ,<DE> 

*/
public boolean escreva(){//falta fazer, vou terminar nestante
    if(valueList.get(head).equals("escreva")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            if(de()){
                if(valueList.get(head).equals(")")){
                    head++;
                    if(valueList.get(head).equals(";")){
                        head++;
                        return true;
                    } 
                }
            }
            
        }
    }
    return imprimeErro("Comando escreva mal formatado");
}
private boolean de(){
    if(tokenList.get(head).equals("Caracter")||tokenList.get(head).equals("Cadeia")){
        head++;
        return e();
    }else if(tokenList.get(head).equals("Identificador")){
        if(valueList.get(head+1).equals(".")){//lendo à frente
            if(aceReg()) return e();
        } else if(valueList.get(head+1).equals("[")){//lendo à frente
            if(aceVM()) return e();
        }else{
            head++;
            return e();
        }
    }
    return false;
}

private boolean e(){
   if(valueList.get(head).equals(",")){
        head ++;
        return de();
    }else return true;  //lambida 
}

/*comando enquanto se e senao dependem das funcoes condicao e codigogeral que estão incompletas
<Enquanto>::= enquanto(<Condicao>){<CG>}
*/
public boolean enquanto(){
    if(valueList.get(head).equals("enquanto")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            if(condicao()){//falta fazer a condição
                if(valueList.get(head).equals(")")){
                    head++;
                    if(valueList.get(head).equals("{")){
                        head++;
                        if(codigoGeral()){
                            if(valueList.get(head).equals("}")){
                                head++;
                                return true;
                            }
                        }
                    }
                }
            }
        }
       
    }
    return imprimeErro("Erro no comando enquanto");
}

/*
<se>::=se(<Condicao>){<CG>}<I>
<I>::= senao{<CG>} | ƛ
*/

public boolean se(){
    if(valueList.get(head).equals("se")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            if(condicao()){//falta fazer a condicao
                if(valueList.get(head).equals(")")){
                    head++;
                    if(valueList.get(head).equals("{")){
                        head++;
                        if(codigoGeral()){
                            if(valueList.get(head).equals("}")){
                                head++;
                                return i();
                                
                            }
                        }
                    }
                }
            }
        }
    }
    return imprimeErro("Erro no comando se");  
}
//<I>::= senao{<CG>} | ƛ
private boolean i(){
    if(valueList.get(head).equals("senao")){
        head++;
        if(valueList.get(head).equals("{")){
            head++;
            if(codigoGeral()){
                if(valueList.get(head).equals("}")){
                    return true;
                }
            }
        }
    }
    return true; //se for lambida retorna verdadeiro
}

//funcoes de acesso
//<AceReg>::= <Identificador>.<Identificador>
public boolean aceReg(){
    if(tokenList.get(head).equals("Identificador")){
        head++;
        if(valueList.get(head).equals(".")){
            head++;
            if(tokenList.get(head).equals("Identificador")){
               head++;
               return true;
            }
        }
    }
    return imprimeErro("Acesso ao registrador incorreto");
}
//<AceVet>::= <Identificador>[<Numero>]
//public boolean aceVet(){
//    if(tokenList.get(head).equals("Identificador")){
//        head++;
//        if(valueList.get(head).equals("[")){
//            head++;
//            if(inteiro()){
//                if(valueList.get(head).equals("]")){
//                    head++;
//                    return true;
//                }
//            }
//        }
//    }
//    return imprimeErro("acessso ao vetor incorreto");
//}
////<AceMat>::= <Identificador>[<Numero>][<Numero>]
//public boolean aceMat(){
//     if(tokenList.get(head).equals("Identificador")){
//        head++;
//        if(valueList.get(head).equals("[")){
//            head++;
//            if(inteiro()){
//                if(valueList.get(head).equals("]")){
//                    head++;
//                    if(valueList.get(head).equals("[")){
//                        head++;
//                        if(inteiro()){
//                            if(valueList.get(head).equals("]")){
//                                head++;
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//    return imprimeErro("acessso a matriz incorreto");
//}

private boolean aceVM(){
    if(tokenList.get(head).equals("Identificador")){
        head++;
        return declaraVetor();
    }
    return false;
}

private boolean operacoes(){ // incompleto
    return true;
}

private boolean operando(){ // incompleto
    if(valores()) return true;
    else if(tokenList.get(head).equals("Identificador")){
        head++;
        return true;
    }
    return false;
}

}

