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
        if(variaveis()) {
            return iniciar2();
        } else {
            proxBloco();
            imprimeErro("Bloco variaveis mal fechado ou ausente");
            return iniciar2();
        }
    } else  {
        proxBloco();
        imprimeErro("Bloco constantes mal fechado ou ausente");
        if(variaveis()) {
            return iniciar2();
        } else {
            proxBloco();
            imprimeErro("Bloco variaveis mal fechado ou ausente");
            return iniciar2();
        }
    }
}

public boolean iniciar2(){
    if(valueList.get(head).equals("algoritmo")){
        return algoritmo();
    }else if(funcao()){
        return iniciar2();
    }
    return false;
}

private void proxBloco(){
    while(!bloco() && !valueList.get(head).equals("##")){
        head++;
    }
}

private boolean bloco(){
    if(valueList.get(head).equals("algoritmo") || valueList.get(head).equals("constantes") || valueList.get(head).equals("funcao") || valueList.get(head).equals("variaveis") || valueList.get(head).equals("registro") || valueList.get(head).equals("##")){
        return true;
    } else return false;
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
/*
<CG>::= <Variaveis><C> | <Comandos><C> | <CF>;<C>| <Atr>;<C> |<Incremento>;<C>| ƛ
<C>::= <CG> | ƛ
*/
public boolean codigoGeral(){ // falta fazer + testes
    if(variaveis()){
        if(c()) return true;
    }else if(comandos()){
        if(c()) return true;
        
    }else if(tokenList.get(head).equals("Identificador")){    
        if(valueList.get(head+1).equals("(")){
            if(chamadaFuncao()){
                if(valueList.get(head).equals(";")){
                    head++;
                    if(c()) return true;
                }else return imprimeErro("falta delimitador ;");
            }
        } else if(valueList.get(head+1).equals(".") || valueList.get(head+1).equals("=")){
            if(atr()){
                if(valueList.get(head).equals(";")){
                    head++;
                    if(c()) return true;
                }else return imprimeErro("falta delimitador ;");
            }
        }
        else if(valueList.get(head+1).equals("--") || valueList.get(head+1).equals("++")){
            if(incremento()){
                if(valueList.get(head).equals(";")){
                    head++;
                    if(c()) return true;
                }else return imprimeErro("falta delimitador ;");
            }
        }
        imprimeErro("Expressão inválida");
    } else if(valueList.get(head).equals("##")){
        return imprimeErro("Fim inesperado do documento");
    } else if(valueList.get(head).equals("}")){
        return true;
    }  else if(valueList.get(head).equals("retorno")){
        return true;
    } 
    return proxCodigo(); 
}

private boolean proxCodigo(){
    while(!valueList.get(head).equals("}") && !valueList.get(head).equals(";") && !valueList.get(head).equals("##") && !valueList.get(head).equals("retorno")){
        head++;
    }
    if(valueList.get(head).equals(";")) {
        head++;
    }
    return c();    
}

private boolean c(){
    
        if(codigoGeral()){
             return true;
        }else return true; //lambda  
       
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
                    return true;
                } 
            } if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
                if(operacoes()) {
                    return true;
                } 
            }if(valueList.get(head+1).equals("(")){ // lendo à frente para decidir em qual função entrar
                return chamadaFuncao();
            } else{
                    return true;
            }
        } else if(tokenList.get(head).equals("Numero")) {  
           if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
                if(operacoes()) {
                    return true;
                } 
            } else {
                head++;
                return true;
            }
        } else if(valores()) {
                return true;
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
        if(x()){
            return true;
        } else{
            return imprimeErro("Valor atribuido inválido");
        }
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
private boolean proxCodigoCons(){
    while(!bloco() && !valueList.get(head).equals("}") && !valueList.get(head).equals(";") && !valueList.get(head).equals("##")){
        head++;
    }
    if(valueList.get(head).equals(";")) {
        head++;
    }
    return declaraConstante();    
}

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
                        }
                    }
                }
            }   
      }else if(valueList.get(head).equals("##")){
        return imprimeErro("Fim inesperado do documento");
    } else if(valueList.get(head).equals("}")){
        return true;
    } else if(bloco()){
        return false;
    } 
     
    imprimeErro("Erro ao declarar constantes"); 
    return proxCodigoCons();
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

private boolean proxCodigoReg(){
    while(!bloco() && !valueList.get(head).equals("}") && !valueList.get(head).equals(";") && !valueList.get(head).equals("##")){
        head++;
    }
    if(valueList.get(head).equals(";")) {
        head++;
    }
    return a();    
}

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
    proxBloco();
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
            }
        }
    }else if(valueList.get(head).equals("##")){
        return imprimeErro("Fim inesperado do documento");
    } else if(valueList.get(head).equals("}")){
        return true;
    } else if(bloco()){
        return false;
    } 
     
    imprimeErro("Erro ao declarar atributos do registro"); 
    return proxCodigoReg();
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
        return imprimeErro("Erro: Bloco de variaveis mal formado");
    } 
    return false;
}

private boolean proxCodigoVar(){
    while(!bloco() && !valueList.get(head).equals("}") && !valueList.get(head).equals(";") && !valueList.get(head).equals("##")){
        head++;
    }
    if(valueList.get(head).equals(";")) {
        head++;
    }
    return declaraVariaveis();    
}

public boolean declaraVariaveis(){
    if(tipo()){//chama o metodo de identificar tipo
            if(tokenList.get(head).equals("Identificador")){
                head++;
                if(AcompDV()) return true;
            }
                
    } else if(tokenList.get(head).equals("Identificador")){
        head++;
        if(tokenList.get(head).equals("Identificador")){
            head++;
            if(valueList.get(head).equals(";")) {
                head++;
                declaraVariaveis();
                return true;
            }
        }  
    }else if(valueList.get(head).equals("##")){
        return imprimeErro("Fim inesperado do documento");
    } else if(valueList.get(head).equals("}")){
        return true;
    } else if(bloco()){
        return false;
    } 
     
    imprimeErro("Erro ao declarar variáveis"); 
    return proxCodigoVar();
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
        proxBloco();
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
                    if(operando())
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
                        if(operacoes())
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

private boolean parametro(){ 
    if(tipo()){
        if(tokenList.get(head).equals("Identificador")){
            head++;
            return parametro2();   
        }
    } else if(tokenList.get(head).equals("Identificador")){
         if(tokenList.get(head).equals("Identificador")){
            head++;
            return parametro2();   
        }
    }
    
    return imprimeErro("Erro: parametro de declaração de função inválido");
}

private boolean parametro2(){ 
    if(valueList.get(head).equals(",")){
        head++;
        return parametroCF();
    } else {
        return true;
    }
}
/*
<Condicao>::= <ExpC><X> |  (<Condicao>)
<X>::= <OpL> | ƛ
<OpL>::= &&<Y> | ||<Y> | ƛ
<Y>::= <Condicao>
<ExpC>::= <Operando> <OpCond><Operando> 
<Operando>::= <Valores> | <Identificador>
<OpCond>::= ><Z> | <<Z> | == | !=
<Z>::=      = | ƛ

*/
public boolean condicao(){//falta testar
    if(expC()){
        
        return xCond();
    }else if(valueList.get(head).equals("(")){
        head++;
        if(condicao()){
            if(valueList.get(head).equals(")")){
                head++;
                return true;
            }
        }
    }
    return imprimeErro("Erro no condição");
}
//<X>::= <OpL> | ƛ
private boolean xCond(){
    return opl();
}

//<OpL>::= &&<Y> | ||<Y> | ƛ
//<Y>::= <Condicao>
private boolean opl(){
   
    if(valueList.get(head).equals("&&")|| valueList.get(head).equals("||")){
        head++;
        return condicao();
    }else return true; //lambda
}
private boolean expC(){
    
    if(operando()){
        if(opCond()){
            return operando();
        }
    }
    return false;
}
//<OpCond>::= ><Z> | <<Z> | == | !=
//<Z>::=      = | ƛ
private boolean opCond(){
    
    if(valueList.get(head).equals(">")||valueList.get(head).equals("<")){
        head++;
        return true;
        //return zCond();
    }else if(valueList.get(head).equals("==")){
        head++;
        return true;
    }else if(valueList.get(head).equals("!=")){
        head++;
        return true;
    }else if(valueList.get(head).equals("<=")){
        head++;
        return true;
    }else if(valueList.get(head).equals(">=")){
        head++;
        return true;
    }
    return false;
}
/* nosso codigo trata como 1 tokem só <=, >=
private boolean zCond(){
    if(valueList.get(head).equals("=")){
        head++;
        return true;
    }else return true; //lambda
}
*/

public boolean chamadaFuncao(){//falta fazer
    if(tokenList.get(head).equals("Identificador")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            if(valueList.get(head).equals(")")){
                head++;
                return true;
            } else if(parametroCF()){
                if(valueList.get(head).equals(")")){
                    head++;
                    return true;
                }
            }
        }
    }
    return imprimeErro("Erro: chamada de função mal formada");
}

private boolean parametroCF(){ 
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
/*
<Operacoes> ::= <ValNum><ValNumAcomp> |   (<ValNum><operandoOperacoes2> 
<ValNumAcomp> ::= <operandoOperacoes> | ƛ
<operandoOperacoes>::= <Opn><Operacoes> 
<operandoOperacoes2>::= <operandoOperacoes>) <operandoOperacoesAcomp>  | )
<operandoOperacoesAcomp>::= <operandoOperacoes> |  ƛ
<Opn>::= + | - | * |  / 
<ValNum>::= <Identificador> | <Número>
*/
public boolean operacoes(){ // falta testa mais
    
    if(valNum()){
        if(valNumAcomp()){
            System.out.println("head parou no "+head);
            return true;
        }
    }else if(valueList.get(head).equals("(")){
        head++;
        if(valNum()){
            if(operandoOperacoes2()){
                System.out.println("head parou no "+head);
                return true;
                
            }
        }
    }
    return false;
            
}
private boolean valNum(){
    if(tokenList.get(head).equals("Identificador")||tokenList.get(head).equals("Numero")){
        head++;
        return true;
    }else return false;
}
//<ValNumAcomp> ::= <operandoOperacoes> | ƛ
private boolean valNumAcomp(){
    if(valueList.get(head).equals("+") || valueList.get(head).equals("-") || valueList.get(head).equals("*") || valueList.get(head).equals("/")){
        return operandoOperacoes();
    }else {
        return true;
    }
}
private boolean operandoOperacoes(){
    if(opn()){
        return operacoes();
    }else return false;
}
//<operandoOperacoes2>::= <operandoOperacoes>) <operandoOperacoesAcomp>  | )
private boolean operandoOperacoes2(){
    if(operandoOperacoes()){
        if(valueList.get(head).equals(")")){
           head++;
           if(operandoOperacoesAcomp()){
               return true;
           }
        }
    }else if(valueList.get(head).equals(")")){
        head++;
        return true;
    }
    return false;
}
//<operandoOperacoesAcomp>::= <operandoOperacoes> |  ƛ
private boolean operandoOperacoesAcomp(){
    if(valueList.get(head).equals("+") || valueList.get(head).equals("-") || valueList.get(head).equals("*") || valueList.get(head).equals("/")){
        return operandoOperacoes();
    }else {
        return true;
    }
}

private boolean opn(){
    if(valueList.get(head).equals("+")||valueList.get(head).equals("-")||valueList.get(head).equals("*")||valueList.get(head).equals("/")){
        head++;
        return true;
    }else return false;
}



private boolean operando(){ // incompleto
    if(valores()) return true;
    else if(tokenList.get(head).equals("Identificador")){
        head++;
        return true;
    }
    return false;
}
/*
<para>::=para(<IniP>;<Condicao>;<OpFor>){<CG>}
<OpFor>::=<Operacoes> | <Incremento>
<IniP>::=<Atr> | <Identificador>
<Incremento>::=<ValNum><Z> | <Z><ValNum> 
<Z>::= ++ | --
*/
public boolean para(){//falta testar
    if(valueList.get(head).equals("para")){
        head++;
        if(valueList.get(head).equals("(")){
            head++;
            if(iniP()){
                if(valueList.get(head).equals(";")){
                    head++;
                    if(condicao()){
                        if(valueList.get(head).equals(";")){
                            head++;
                            if(opFor()){
                                if(valueList.get(head).equals(")")){
                                    head++;
                                    if(valueList.get(head).equals("{")){
                                        head++;
                                        if(codigoGeral()){//falta fazer o cg
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
                }
            }
        }
    }
    return imprimeErro("Erro: Comando para mal formado");
}

private boolean iniP(){
    if(tokenList.get(head).equals("Identificador")){
        if(tokenList.get(head+1).equals(";")){
            head++;
            return true; 
        }else {  
            return atr();
        }
    }else return imprimeErro("Erro: Parametro 1 do comando para mal formado");
}
//<OpFor>::=<Operacoes> | <Incremento>
private boolean opFor(){
    if(tokenList.get(head).equals("Numero") || tokenList.get(head).equals("Identificador")){
        if(valueList.get(head+1).equals("++") || valueList.get(head+1).equals("--")){
            if(incremento()) return true;
        } else {
            if(operacoes()) return true;
        }
    } else if(incremento()) return true;
    return imprimeErro("Erro: Parametro 3 do comando para mal formado");
}

//<Incremento>::=<ValNum><Z> | <Z><ValNum> 
//<Z>::= ++ | --
private boolean incremento(){
    if(valNum()){
       if(z()) return true;
    }else if(z()){
        if(valNum()) return true;
    }
    return false;
}

private boolean z(){
    if(valueList.get(head).equals("++")||valueList.get(head).equals("--")){
        head++;
        return true;
    }else return false;
}

//<Comandos>::= <Se> | <Enquanto> | <Para> | <Escreva> | <Leia>

public boolean comandos(){//sem testes
    if(valueList.get(head).equals("se")){
        return se();
    }else if(valueList.get(head).equals("enquanto")){
        return enquanto();
    }else if(valueList.get(head).equals("para")){
        return para();
    }else if (valueList.get(head).equals("escreva")){
        return escreva();
    }else if(valueList.get(head).equals("leia")){
        return leia();
    }else return false;
}


}

