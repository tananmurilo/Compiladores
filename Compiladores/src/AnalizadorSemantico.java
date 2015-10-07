
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author murilo
 */
public class AnalizadorSemantico {
    
    private List<Map> listaEscopos =  new LinkedList<>();
    private PalavrasReservadas palavrasReservadas =  new PalavrasReservadas();

    public AnalizadorSemantico() {
        Map<String, Estrutura> escopoGlobal = new HashMap<String, Estrutura>();
        listaEscopos.add(escopoGlobal);
    }  
    
    //cria e adiciona a lista de escopo nova
    public void criaNovoEscopo(){
        Map<String, Estrutura> novoEscopo = new HashMap<String, Estrutura>();
        listaEscopos.add(novoEscopo);
    }
    
    //remove ultimo da lista de escopo nova
    public void removeNovoEscopo(){
        int i=listaEscopos.size()-1; 
        listaEscopos.remove(i);
    }
    
    // adicionar na estrutura da tabela ( parametros)
    public void addNaTabela(String nome,String token, String tipo,int tam1,int tam2){
                  
            
            Estrutura temp = new Estrutura();
            temp.setNome(nome);
            temp.setToken(token);
            temp.setTipo(tipo);
            temp.setTamanho1(tam1);
            temp.setTamanho2(tam2);

            Map escopo = listaEscopos.get(listaEscopos.size() - 1);
            escopo.put(nome, temp); 
    }
    /*
    
    */
     // adicionar registro na estrutura da tabela 
    public void addNaTabelaRegistro(String nome, List<String[]> atributos){
            
            String[][] atr = new String[atributos.size()][2];
            for(int i=0; i<atributos.size(); i++){
                atr[i] = atributos.get(i);
                addNaTabela(nome+"."+atr[i][1], "variavel" , atr[i][0], 0, 0);// adiciona os atributos como variaveis na tabela com o nome: registro.atributo
            }
        
            Estrutura temp = new Estrutura();
            temp.setNome(nome);
            temp.setToken("registro");
            temp.setTipo("registro");
            temp.setTamanho1(0);
            temp.setTamanho2(0);
            temp.setAtributos(atr);
            
            Map escopo = listaEscopos.get(listaEscopos.size() - 1);
            escopo.put(nome, temp); 
            
    }
    /*
    
    */
    // adicionar funcao na estrutura da tabela 
    public void addNaTabelaFuncao(String nome, String tipo,  List<String[]> atributos){
            
            String[] atr = new String[atributos.size()];
            for(int i=0; i<atributos.size(); i++){
                atr[i] = atributos.get(i)[0];
            }
        
            Estrutura temp = new Estrutura();
            temp.setNome(nome);
            temp.setToken("funcao");
            temp.setTipo(tipo);
            temp.setTamanho1(0);
            temp.setTamanho2(0);
            temp.setParametros(atr);
            
            Map escopo = listaEscopos.get(0);
            escopo.put(nome, temp); 
            
    }
    /**
     * @return Null se não encontrar e a estrutura se encontrar
    */
    public Estrutura procurarPalavra(String nome){
    //prucurar de escopo em escopo
    for(int i=listaEscopos.size()-1; i>=0; i--){
        Map<String, Estrutura> escopo = listaEscopos.get(i);
        //prucurar se tem esse mesmo nome na lista
        if ( escopo.containsKey( nome ) ) { 
            Estrutura estrutura = escopo.get(nome);
             
            return estrutura;
                               
        }else{ 

        }               
    }
        
        return null;
    }
    
    //procura somente no escopo mais próximo
    public Estrutura procurarPalavraEscopo(String nome){
        
        
        int i=listaEscopos.size()-1; 
        Map<String, Estrutura> escopo = listaEscopos.get(i);
        //prucurar se tem esse mesmo nome na lista
        if ( escopo.containsKey( nome ) ) { 
            Estrutura estrutura = escopo.get(nome);

            return estrutura;

        }else{ 

        }                       
        
        return null;
    }
    
     //procura se existe constante com este nome
    public boolean procurarConstantes(String nome){
        
        Map<String, Estrutura> escopo = listaEscopos.get(0);
        //prucurar se tem esse mesmo nome na lista
        if(escopo.containsKey( nome )){
            Estrutura estrutura = escopo.get(nome);
            if(estrutura.getToken().equals("constante")) return false;
        }
        
        return true;
    }
    
    public void inicializaPalavra(String nome, String token, String tipo, String tipoAtribuido, String valor, Producoes sintatico){
        
        if((tipo.equals("inteiro") || tipo.equals("real")) && !valor.equals("")) {
            tipoAtribuido = inteiro_real(valor);
        } 
        if(procurarConstantes(nome)){
            if(this.procurarPalavraEscopo(nome)==null){//verificar se ja esta na lista
                if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                    sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma palavra reservada");
                } else {
                    if(tipoAtribuido.equals(tipo)) {
                        this.addNaTabela(nome, token, tipo, 0, 0); 
                    } else {
                        sintatico.imprimeErroSemantico("Tipos incompatíveis: "+tipoAtribuido+" sendo atribuído à "+tipo);
                    }    
                }
            }else{
                    sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" já foi declarada neste escopo e não pode ser re-declarada");
            }
        } else sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma constante declarada e não pode ser re-declarada");
        
    }
    //Neste caso não há comparação de tipos pois não há atribuição
    public void inicializaPalavra(String nome, String token, String tipo, Producoes sintatico){
        
        if(procurarConstantes(nome)){
            if(this.procurarPalavraEscopo(nome)==null){//verificar se ja esta na lista
                if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                    sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma palavra reservada");
                } else {
                    this.addNaTabela(nome, token, tipo, 0, 0); 
                }                                                           
            }else{
                sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" já foi declarada neste escopo e não pode ser re-declarada");
            }
        } else sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma constante declarada e não pode ser re-declarada");
        
    }
    //Quando a palavra é inicializada e o valor de outro identificador é atribuido a ela
    public void inicializaPalavraAtribuida(String nome, String token, String tipo, String nomeAtr, Producoes sintatico){
           
        if(procurarConstantes(nome)){
            if(this.procurarPalavraEscopo(nome)==null){//verificar se ja esta na lista
                if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                    sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma palavra reservada");
                } else {
                    Estrutura simboloAtribuido = this.procurarPalavra(nomeAtr);
                    if(simboloAtribuido==null){
                        sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nomeAtr+" não foi encontrada");
                    } else if(simboloAtribuido.getToken().equals("variavel") || simboloAtribuido.getToken().equals("constante")){
                         if(simboloAtribuido.getTipo().equals(tipo)){
                            this.addNaTabela(nome, token, tipo, 0, 0);
                        } else sintatico.imprimeErroSemantico("Tipos incompatíveis: "+simboloAtribuido.getTipo()+" sendo atribuído à "+tipo);          
                    } else sintatico.imprimeErroSemantico("Error Semantico: somente constantes e variaveis podem ser atribuidas na inicialização");   
                }                                                           
            }else{
                sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" já foi declarada neste escopo e não pode ser re-declarada");
            }
        } else sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma constante declarada e não pode ser re-declarada");
        
    }
    /*
    
    */
    public void inicializaRegistro(String nome, List<String[]> atributos, Producoes sintatico){
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                this.addNaTabelaRegistro(nome, atributos); 
            }                                                           
        }else{
            sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
    }
    /*
    
    */
    public void inicializaFuncao(String nome, String tipo, List<String[]> atributos, Producoes sintatico){
        
        this.criaNovoEscopo();
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                this.addNaTabelaFuncao(nome, tipo, atributos);
                for (String[] atributo : atributos) {
                    addNaTabela(atributo[1], "variavel", atributo[0], 0, 0);
                }
            }                                                           
        }else{
            sintatico.imprimeErroSemantico("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
    }
    /*
    
    */
    public void verificaChamadaFuncao(String nome, List<String> entrada, Producoes sintatico){
        
        Estrutura funcao = this.procurarPalavra(nome);
        
        String parametrosEsperado =nome+"(";
        String parametrosInseridos = nome+"("; 
        
        if(funcao!=null){
            if(funcao.getToken().equals("funcao")){
                String[] parametros = funcao.getParametros();
                
                for (int i = 0; i < parametros.length; i++) { //concateno os parametros esperados
                    if(i==0) parametrosEsperado = parametrosEsperado+parametros[i];
                    else parametrosEsperado = parametrosEsperado+", "+parametros[i];
                }
                
                for (int i = 0; i < entrada.size(); i++) { //concateno os parametros inseridos
                    if(i==0) parametrosInseridos = parametrosInseridos+entrada.get(i);
                    else parametrosInseridos = parametrosInseridos+", "+entrada.get(i);
                }
                
                parametrosEsperado = parametrosEsperado+(")");
                parametrosInseridos = parametrosInseridos+(")");
                
                if(parametros.length == entrada.size()){
                
                    boolean erro = false;
                    for (int i = 0; i < entrada.size(); i++) {
                        if(!parametros[i].equals(entrada.get(i))){
                            erro = true;
                            break;
                        }
                    }
                    if(erro) sintatico.imprimeErroSemantico("Parametros não batem: esperado: "+parametrosEsperado+", inserido: "+parametrosInseridos);
                } else sintatico.imprimeErroSemantico("Parametros não batem: esperado: "+parametrosEsperado+", inserido: "+parametrosInseridos);
            } else sintatico.imprimeErroSemantico("Error Semantico: a função "+nome+" não foi declarada");
        } else sintatico.imprimeErroSemantico("Error Semantico: a função "+nome+" não foi declarada");
    }
    /*
    
    */
    public String inteiro_real(String numero){
       
        if((numero.indexOf('.')) < 0){
            return "inteiro";
        } else return "real";
    }
    
    public void imprimir(){
        for(int i=0; i<this.listaEscopos.size(); i++){
                
            Iterator it = listaEscopos.get(i).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Estrutura escopo = (Estrutura)pair.getValue();
                System.out.println(escopo.getNome());
                System.out.println(escopo.getToken());
                System.out.println(escopo.getTipo());
                System.out.println(escopo.getTamanho1());
                System.out.println(escopo.getTamanho2());
                System.out.println(" ");
                it.remove(); // avoids a ConcurrentModificationException
            }
                          
        }
    }
}
