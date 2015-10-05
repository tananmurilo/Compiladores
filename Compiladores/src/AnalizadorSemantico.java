
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
    public void addNaTabelaFuncao(String nome, String tipo,  List<String> atributos){
            
            String[] atr = new String[atributos.size()];
            for(int i=0; i<atributos.size(); i++){
                atr[i] = atributos.get(i);
            }
        
            Estrutura temp = new Estrutura();
            temp.setNome(nome);
            temp.setToken("funcao");
            temp.setTipo(tipo);
            temp.setTamanho1(0);
            temp.setTamanho2(0);
            temp.setParametros(atr);
            
            Map escopo = listaEscopos.get(listaEscopos.size() - 1);
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
        
        //prucurar de escopo em escopo
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
    
    public void inicializaPalavra(String nome, String token, String tipo, String tipoAtribuido, String valor, Producoes sintatico){
        
        if((tipo.equals("inteiro") || tipo.equals("real")) && !valor.equals("")) {
            tipoAtribuido = inteiro_real(valor);
        } 
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                if(tipoAtribuido.equals(tipo)) {
                    this.addNaTabela(nome, token, tipo, 0, 0); 
                } else {
                    sintatico.imprimeErro("Tipos incompatíveis: "+tipoAtribuido+" sendo atribuído à "+tipo);
                }    
            }
        }else{
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
        
    }
    //Neste caso não há comparação de tipos pois não há atribuição
    public void inicializaPalavra(String nome, String token, String tipo, Producoes sintatico){
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                this.addNaTabela(nome, token, tipo, 0, 0); 
            }                                                           
        }else{
            sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
    }
    //Quando a palavra é inicializada e o valor de outro identificador é atribuido a ela
    public void inicializaPalavraAtribuida(String nome, String token, String tipo, String nomeAtr, Producoes sintatico){
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                Estrutura simboloAtribuido = this.procurarPalavra(nomeAtr);
                if(simboloAtribuido==null){
                    sintatico.imprimeErro("Error Semantico: a palavra "+nomeAtr+" não foi encontrada");
                } else if(simboloAtribuido.getToken().equals("variavel") || simboloAtribuido.getToken().equals("constante")){
                     if(simboloAtribuido.getTipo().equals(tipo)){
                        this.addNaTabela(nome, token, tipo, 0, 0);
                    } else sintatico.imprimeErro("Tipos incompatíveis: "+simboloAtribuido.getTipo()+" sendo atribuído à "+tipo);          
                } else sintatico.imprimeErro("Error Semantico: somente constantes e variaveis podem ser atribuidas na inicialização");   
            }                                                           
        }else{
            sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
    }
    /*
    
    */
    public void inicializaRegistro(String nome, List<String[]> atributos, Producoes sintatico){
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                this.addNaTabelaRegistro(nome, atributos); 
            }                                                           
        }else{
            sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
    }
    /*
    
    */
    public void inicializaFuncao(String nome, String tipo, List<String> atributos, Producoes sintatico){
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(palavrasReservadas.buscarPalavra(nome)){ //verifica se é uma palavra reservada
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" é uma palavra reservada");
            } else {
                this.addNaTabelaFuncao(nome, tipo, atributos); 
            }                                                           
        }else{
            sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
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
