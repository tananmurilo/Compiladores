
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

    public AnalizadorSemantico() {
        Map<String, Estrutura> escopoGlobal = new HashMap<String, Estrutura>();
        listaEscopos.add(escopoGlobal);
    }   
    
    
    //cria e adiciona a lista de escopo nova
    public void criaNovoEscopo(){
        Map<String, Estrutura> novoEscopo = new HashMap<String, Estrutura>();
        listaEscopos.add(novoEscopo);
    }
    
    // adicionar na estrutura da tabela (falta implementar atributos e parametros)
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
   
        Map<String, Estrutura> escopo = listaEscopos.get(listaEscopos.size()-1);
        //prucurar se tem esse mesmo nome na lista
        if ( escopo.containsKey( nome ) ) { 
            Estrutura estrutura = escopo.get(nome);
            
            return estrutura;
                                
        }else{ 

        }   
        return null;
    }
    
    public void inicializaPalavra(String nome, String token, String tipo, String tipoAtribuido, Producoes sintatico){
        
        if(this.procurarPalavra(nome)==null){//verificar se ja esta na lista
            if(tipoAtribuido.equals(tipo)) {
                this.addNaTabela(nome, token, tipo, 0, 0); 
            } else {
                sintatico.imprimeErro("Tipos incompatíveis: "+tipoAtribuido+" sendo atribuído à "+tipo);
            }                                                              
        }else{
                sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
    }
    //Neste caso não há comparação de tipos pois não há atribuição
    public void inicializaPalavra(String nome, String token, String tipo, Producoes sintatico){
        
        if(this.procurarPalavraEscopo(nome)==null){//verificar se ja esta na lista
            
            this.addNaTabela(nome, token, tipo, 0, 0); 
                                                                       
        }else{
            sintatico.imprimeErro("Error Semantico: a palavra "+nome+" já foi declarada e não pode ser re-declarada");
        }
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
