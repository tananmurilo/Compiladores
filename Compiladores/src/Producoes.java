import java.util.LinkedList;
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
   private List<String> erros;
   private List<String> errosSemanticos;
   private boolean erro;
   
   AnalizadorSemantico semantico =  new AnalizadorSemantico();
  
    
public Producoes(List<String> tokens, List<String> values, List<String> lines){
    tokenList = tokens;
    valueList = values;
    linePositions = lines;
    erros = new LinkedList<>();
    errosSemanticos = new LinkedList<>();
    erro=false;
}  

public boolean imprimeErro(String erro){
    String temp=erro+" na linha " + linePositions.get(head).toString();
    erros.add(temp);
    System.out.println(erro + " na linha " + linePositions.get(head));
    return false;
};

public void imprimeErroSemantico(String erro){
    String temp=erro+" na linha " + linePositions.get(head).toString();
    errosSemanticos.add(temp);
    System.out.println(erro + " na linha " + linePositions.get(head));
   
}
//transformar a lista em uma string
public String getErros(){
    String ret = "";
    for(String p:erros){
        ret= ret+p+"\n";
    }
    for(String k:errosSemanticos){
        ret= ret+k+"\n";
    }
    if (erros.isEmpty()){
          erro=false;
          ret= ret+"\nSucesso: Nenhum erro encontrado na análise Sintática ";
    }else{
         erro=true;
         ret= ret+"\nFalha: Erro(s) encontrado(s) na análise Sintática ";
    }
    if(errosSemanticos.isEmpty()){
        ret= ret+"\nSucesso: Nenhum erro encontrado na análise Semantica ";
    }else{
         erro=true;
         ret= ret+"\nFalha: Erro(s) encontrado(s) na análise Semantica ";
    }
    return ret;
}

public boolean getErro(){
    return this.erro;
}

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
    }else if(valueList.get(head).equals("funcao")){
        if(funcao()) return iniciar2();
    } else {
         imprimeErro("Bloco algoritmo mal fechado ou ausente");
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
    if(valueList.get(head).equals("senao")) imprimeErro("senao deve vir após o término do se");
    if(valueList.get(head).equals("variaveis") && !valueList.get(head-1).equals("{")) imprimeErro("variaveis devem ser declaradas somente no início do bloco");
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
                }
            }
        } else if(valueList.get(head+1).equals(".") || valueList.get(head+1).equals("=")|| valueList.get(head+1).equals("[")){
            if(atr()){
                if(valueList.get(head).equals(";")){
                    head++;
                    if(c()) return true;
                }
            }
        }
        else if(valueList.get(head+1).equals("--") || valueList.get(head+1).equals("++")){
            if(incremento()){
                if(valueList.get(head).equals(";")){
                    head++;
                    if(c()) return true;
                }
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
    String nome="";
    String tipo="";
    if(tokenList.get(head).equals("Identificador")){
        if(valueList.get(head+1).equals(".")){
            if(aceReg()) {
                if(valueList.get(head).equals("=")){
                    head++;
                    if(valorRetornado(nome, tipo)) return true;
                }
            } 
        }else if(valueList.get(head+1).equals("[")){
            if(aceVM()) {
                if(valueList.get(head).equals("=")){
                    head++;
                    if(valorRetornado(nome,tipo)) return true;
                }
            } 
        } else{
            nome = valueList.get(head);
            System.out.println(nome);
            if(semantico.procurarPalavra(nome)!=null){ // se ja foi declarado
                Estrutura s =  new Estrutura();
                s = semantico.procurarPalavra(nome);
                tipo=s.getTipo();
            }else{
                imprimeErroSemantico("Erro "+nome+" não foi declarado");
            }
            head++;
            if(valueList.get(head).equals("=")){
                head++;
                if(valorRetornado(nome, tipo)) return true;
            }
        }
    }       
    
    return imprimeErro("Erro: atribuição mal formada");
}

public boolean valorRetornado(String nome, String tipo){ 
        if(tokenList.get(head).equals("Identificador")) {
            if(valueList.get(head+1).equals(".")){ // lendo à frente para decidir em qual função entrar
                if(aceReg()) {
                    return true;
                } 
            }else if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
                if(operacoes()) {
                    return true;
                } 
            }else if(valueList.get(head+1).equals("(")){ // lendo à frente para decidir em qual função entrar
                return chamadaFuncao();
            } else if(valueList.get(head+1).equals("[")){ // lendo à frente para decidir em qual função entrar
                return aceVM();
            } else{ 
                    head++;
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
        } else if(!valores().equals("Erro")) {
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


    public String valores(){ 
        if(tokenList.get(head).equals("Numero")) {
            head++;
            return "numero";
        } else if(tokenList.get(head).equals("Caracter")) {
            head++;            
            return "char";
        } else if(tokenList.get(head).equals("Cadeia")) {
            head++;            
            return "cadeia";
        } else if(valueList.get(head).equals("verdadeiro")) {
            head++;            
            return "booleano";
        } else if(valueList.get(head).equals("falso")) {
            head++;            
            return "booleano";
        } 
        return "Erro";
    }

/*<Inicialização>::= <ƛ> | =<X>
<X>::= <Identificador> | <Valores>
*/
// teste para =342; ou ;
public boolean inicializacao(String nome, String tipo){
    if(valueList.get(head).equals("=")){
        head++;
        if(x(nome, tipo)){
            return true;
        } else{
            return imprimeErro("Valor atribuido inválido");
        }
    }else return true; // caso lambda
        
}
//<X>::= <Identificador> | <Valores>
public boolean x(String nome, String tipo){
    if(tokenList.get(head).equals("Identificador")){
        semantico.inicializaPalavraAtribuida(nome, "variavel", tipo, valueList.get(head), this);
        head++;
        return true;
    }else {
        String tipoAtribuido = valores();  
        if(!tipoAtribuido.equals("Erro")){
        semantico.inicializaPalavra(nome, "variavel", tipo, tipoAtribuido, this);
        return true;  
      } else return false;
    } 
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
                //System.out.println("Teste valores armazenados na estrutura");
                return true;
            }
        }
    } 
    return false;
}

//<DC>::= <Tipo><Identificador>=<Valores>;<DCAcomp> 
public boolean declaraConstante(){
    //testes para o semantico
    String nome="";
    String tipo="";
    
     if(!tipo().equals("Erro")){//chama o metodo de identificar tipo
            if(valueList.get(head-1).equals("inteiro") || valueList.get(head-1).equals("real")){//inteiros ou real na tokenlist é Numero
                tipo = "numero";
            }else tipo=valueList.get(head-1);//salvar o tipo do identificador
            
            if(tokenList.get(head).equals("Identificador")){
                 nome=valueList.get(head);//salvar nome do identificador
                head++;
                if(valueList.get(head).equals("=")){
                    head++;
                    String tipoAtribuido = valores();
                    if(!tipoAtribuido.equals("Erro")){//chama o metodo de valores
                        if(valueList.get(head).equals(";")){//teste semantico
                            //teste semantico
                            semantico.inicializaPalavra(nome, "constante", tipo, tipoAtribuido, this);
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
public String tipo(){
     if(valueList.get(head).equals("inteiro")||valueList.get(head).equals("booleano")||valueList.get(head).equals("real")||
        valueList.get(head).equals("char")||valueList.get(head).equals("cadeia")){
         head++;
         return valueList.get(head);
         
     }else return "Erro";
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
    if(!tipo().equals("Erro")){
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
    String nome="";
    String tipo="";
    
    if(!tipo().equals("Erro")){//chama o metodo de identificar tipo
        
        if(valueList.get(head-1).equals("inteiro") || valueList.get(head-1).equals("real")){//inteiros ou real na tokenlist é Numero
            tipo = "numero";
        }else tipo=valueList.get(head-1);//salvar o tipo do identificador
        
        if(tokenList.get(head).equals("Identificador")){
            nome=valueList.get(head);//salvar nome do identificador
            head++;
            if(AcompDV(nome, tipo)) return true;
        }
                
        } else if(tokenList.get(head).equals("Identificador")){
            tipo=valueList.get(head);//salvar o tipo do identificador
            head++;
            if(tokenList.get(head).equals("Identificador")){
                nome=valueList.get(head);//salvar nome do identificador
                head++;
                if(valueList.get(head).equals(";")) {
                    semantico.inicializaPalavra(nome, "variavel", tipo, this);
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

public boolean AcompDV(String nome, String tipo){
    if (declaraVetor()){
        if(valueList.get(head).equals(";")){
            semantico.inicializaPalavra(nome, "variavel", tipo, this);
            head++;
            declaraVariaveis();
            return true;
        }
    } else if(inicializacao(nome, tipo)){
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
        } else if(!tipo().equals("Erro")){
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
        }   
    }
    return false;
}

private boolean parametro(){ 
    if(!tipo().equals("Erro")){
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
        return parametro();
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
        String nome="";
        String tipo="";
            
            if(tokenList.get(head-1).equals("Numero")){
                nome = valueList.get(head-1);
                tipo = "numero";
            }else if(tokenList.get(head-1).equals("Caracter")){
                nome = valueList.get(head-1);
                tipo = "char";
            }else if(tokenList.get(head-1).equals("Cadeia")){
                nome = valueList.get(head-1);
                tipo = "cadeia";
            }else if(tokenList.get(head-1).equals("Identificador")){
                //se for identificador verificar se ja foi declarado
                nome = valueList.get(head-1);
                 System.out.println(nome);
                 //System.out.println(tokenList.get(head-1));
                Estrutura s = new Estrutura();
                if(semantico.procurarPalavra(nome)!=null){// verifica se ta na lista
                    s = semantico.procurarPalavra(nome);
                    tipo = s.getTipo(); 
                    System.out.println("tipo "+tipo);
                    System.out.println("token "+s.getToken());
                }else{

                    imprimeErroSemantico("Erro "+nome+" não foi declarado");

                }
            }
           
        if(opCond(nome, tipo)){
            
            return operando();
        }
    }
    return false;
}
//<OpCond>::= ><Z> | <<Z> | == | !=
//<Z>::=      = | ƛ
private boolean opCond(String nome, String tipo){
    
    if(valueList.get(head).equals(">")||valueList.get(head).equals("<")){
        if(tokenList.get(head+1).equals("Identificador")){//ver o prox token depois < e > para saber se é identificador
            Estrutura b = new Estrutura();
            //verifica se ja foi declarado 
            if(semantico.procurarPalavra(valueList.get(head+1))!=null){// verifica se ta na lista
                b = semantico.procurarPalavra(valueList.get(head+1));
                if(tipo.equals("numero")&& b.getTipo().equals("numero")){//operações < > <= >= so podem ser feitas por inteiro e real e anbos iguais concertar isso ainda
                    //ok bateu, mas tem que corrigir pq tem q ser real < real ou inteiro < inteiro
                }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
                } 
            }else{
                imprimeErroSemantico("Erro "+nome+" não foi declarado");
             }
            
        }else if(tokenList.get(head+1).equals("Numero")){
            //no case não é identificador é valor logo n verifica se ja foi declarada
            if(tipo.equals("numero")&& tokenList.get(head+1).equals("Numero")){//operações < > <= >= so podem ser feitas por inteiro e real e anbos iguais concertar isso ainda
                    //ok bateu, mas tem que corrigir pq tem q ser real < real ou inteiro < inteiro não pode ser real > inteiro
            }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
            } 
        }
        head++;
        return true;
        //return zCond();
    }else if(valueList.get(head).equals("==")||valueList.get(head).equals("!=")){
        //operações == ou != podem ser feitas com inteiro real char ou cadeia mas ambos operando tem q ser do mesmo tipo
        //verifica se ja foi declarado
        if(tokenList.get(head+1).equals("Identificador")){//ver o prox token depois < e > para saber se é identificador
            Estrutura b = new Estrutura();
            if(semantico.procurarPalavra(valueList.get(head+1))!=null){// verifica se ta na lista
                b = semantico.procurarPalavra(valueList.get(head+1));
                if(tipo.equals("numero")&& b.getTipo().equals("numero")){//operações < > <= >= so podem ser feitas por inteiro e real e anbos iguais concertar isso ainda
                    //ok bateu,numero com numero mas mas tem q consertar inteiro tem q ser diferente de real
                }else if((tipo.equals("Caracter")||tipo.equals("char"))&& (b.getTipo().equals("char")||b.getTipo().equals("Caracter"))){
                   //se os 2 é char 
                }else if((tipo.equals("Cadeia")||tipo.equals("cadeia"))&& (b.getTipo().equals("Cadeia")||b.getTipo().equals("cadeia"))){
                   //se os 2 é cadeia
                }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
                } 
            }else{
                imprimeErroSemantico("Erro "+nome+" não foi declarado");
             }
        // no case de serem tokens do tipo "ads" 34 "a" 5.4 ou seja n verifica se ja foi declara pq n é variavel   
        }else if(tokenList.get(head+1).equals("Numero")){
            if(tipo.equals("numero")&& tokenList.get(head+1).equals("Numero")){
                 //numero com numero
            }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
            } 
        }else if(tokenList.get(head+1).equals("Caracter")){
             if((tipo.equals("Caracter")||tipo.equals("char"))&& (tokenList.get(head+1).equals("Caracter")|| tokenList.get(head+1).equals("char"))){
                   
            }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
            } 
        }else if(tokenList.get(head+1).equals("Cadeia")){
             if((tipo.equals("Cadeia")||tipo.equals("cadeia"))&& (tokenList.get(head+1).equals("Cadeia")||tokenList.get(head+1).equals("cadeia"))){
                   
            }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
            } 
        }
        
        head++;
        return true;
    }else if(valueList.get(head).equals("<=")||valueList.get(head).equals(">=")){
        //operações <= ou >=
        //verifica se ja foi declarado
        if(tokenList.get(head+1).equals("Identificador")){//ver o prox token depois < e > para saber se é identificador
            Estrutura b = new Estrutura();
            if(semantico.procurarPalavra(valueList.get(head+1))!=null){// verifica se ta na lista
                b = semantico.procurarPalavra(valueList.get(head+1));
                if(tipo.equals("numero")&& b.getTipo().equals("numero")){//operações < > <= >= so podem ser feitas por inteiro e real e anbos iguais concertar isso ainda
                    //ok bateu, mas tem que corrigir pq tem q ser real < real ou inteiro < inteiro
                }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
                } 
            }else{
                imprimeErroSemantico("Erro "+nome+" não foi declarado");
             }
         //se não for variavel, valor bruto 4 ou 4.3   
        }else if(tokenList.get(head+1).equals("Numero")){
            if(tipo.equals("numero")&& tokenList.get(head+1).equals("Numero")){//operações < > <= >= so podem ser feitas por inteiro e real e anbos iguais concertar isso ainda
                    //ok bateu, mas tem que corrigir pq tem q ser real < real ou inteiro < inteiro não pode ser real > inteiro
            }else{
                    imprimeErroSemantico("Erro tipos incompativeis de operandos");
            } 
        }
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
        } else if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
            if(operacoes()) {
                return e();
            } 
        }else{
            head++;
            return e();
        }
    } else if(tokenList.get(head).equals("Numero")) {  
        if(valueList.get(head+1).equals("+")|| valueList.get(head+1).equals("-")|| valueList.get(head+1).equals("*")|| valueList.get(head+1).equals("/")){
             if(operacoes()) {
                return e();
             } 
         } else {
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
                    head++;
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
            //System.out.println("head parou no "+head);
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
    return imprimeErro("Operação mal formatada");
}
private boolean valNum(){
    if(tokenList.get(head).equals("Numero")) {
        head++;
        return true;
    } else if(tokenList.get(head).equals("Identificador")){
        if(valueList.get(head+1).equals(".")){
          if(aceReg()){
            return true;
          }
        } else if (valueList.get(head+1).equals("[")){
             if(aceVM()){
                return true;
            }
        } else{
            head++;
            return true;
        }
    } 
    return false;
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



private boolean operando(){ 
    if(!valores().equals("Erro")) return true;
    else if(tokenList.get(head).equals("Identificador")){
        if(valueList.get(head+1).equals(".")){
          if(aceReg()){
            return true;
          }
        } else if (valueList.get(head+1).equals("[")){
             if(aceVM()){
                return true;
            }
        } else{
            head++;
            return true;
        }
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
    imprimeErro("Erro: Comando para mal formado");
    while(!bloco() && !valueList.get(head).equals("}") && !valueList.get(head).equals("##")){
        head++;
    }
    if(valueList.get(head).equals("}")) {
        head++;
    }
    return false;
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
    boolean valido =false;
    //semantico.imprimir();
    if(valNum()){
        if(tokenList.get(head-1).equals("Identificador")&&!valueList.get(head-2).equals(".")){//se for identificador antes do ++
            Estrutura s =  new Estrutura();
           
            //System.out.println(s.getTipo());
            // System.out.println(valueList.get(head-1)); //ver palavra pesquisada
            if(semantico.procurarPalavra(valueList.get(head-1))!=null){// verifica se ta na lista
                s = semantico.procurarPalavra(valueList.get(head-1));
               
                if(s.getToken().equals("constante")){
                     imprimeErroSemantico("Erro constante não pode receber atribuições fora do bloco constantes");
                }
                if( s.getTipo().equals("numero")){
                    valido=true;
                }  
           }else{
                
                imprimeErroSemantico("Erro variavel não declarada");
                
            }
        }
       
       if(z()){
           if(!valido){
              imprimeErroSemantico("Erro operações ++ ou -- só podem ser feitos com inteiro ou real"); 
              
           }
           return true;
       }
    }else if(z()){
        int headPos = head;
        if(valNum()){
            if(tokenList.get(headPos).equals("Identificador")&&!valueList.get(headPos+1).equals(".")){
                Estrutura s =  new Estrutura();
                if(semantico.procurarPalavra(valueList.get(headPos))!=null){// verifica se ta na lista
                    s = semantico.procurarPalavra(valueList.get(headPos));

                    if(s.getToken().equals("constante")){
                         imprimeErroSemantico("Erro constante não pode receber atribuições fora do bloco constantes");
                    }
                    if( s.getTipo().equals("numero")){
                        valido=true;
                    }  
               }else{
                    imprimeErroSemantico("Erro variavel não declarada");
                }
                if(!valido){
                  imprimeErroSemantico("Erro operações ++ ou -- só podem ser feitos com inteiro ou real"); 
               }
            }
            return true;
        }
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

