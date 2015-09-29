
import java.util.LinkedList;
import java.util.List;

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
    
    private List<Estrutura> varGlobais =  new LinkedList<>();
    
    // adicionar na estrutura da tabela (falta implementar atributos e parametros)
    public void addNaTabela(String nome,String token, String tipo,int tam1,int tam2){
       
            Estrutura temp = new Estrutura();
            temp.setNome(nome);
            temp.setToken(token);
            temp.setTipo(tipo);
            temp.setTamanho1(tam1);
            temp.setTamanho2(tam2);

            varGlobais.add(temp); 

    }
    /**
     * @return Null se não encontrar e a estrutura se encontrar
    */
    public Estrutura procurar(String nome, String tipo){
        for(int i=0; i<varGlobais.size(); i++){
            //prucurar se tem esse mesmo nome na lista
            if(nome.equals(varGlobais.get(i).getNome())){
               
                if(tipo.equals(varGlobais.get(i).getTipo())){
                    
                    //retornar a estrutura achada 
                    // tem q ser o mesmo nome e o mesmo tipo, inteiro a != booleano a
                    return varGlobais.get(i);
                }
            }
        }
        return null;
        
    }
    
    public void imprimir(){
        for(int i=0; i<this.varGlobais.size(); i++){
            System.out.println(varGlobais.get(i).getNome());
            System.out.println(varGlobais.get(i).getToken());
            System.out.println(varGlobais.get(i).getTipo());
            System.out.println(varGlobais.get(i).getTamanho1());
            System.out.println(varGlobais.get(i).getTamanho2());
            System.out.println(" ");
              
        }
    }
}
