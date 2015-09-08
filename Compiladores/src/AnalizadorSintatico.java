
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
public class AnalizadorSintatico {
    //criar a logica do sintatico
    
    //variaveis
    private List<String> linhaArquivo;
    private List<String> tokenList =  new LinkedList<>();
    private List<String> valueList =  new LinkedList<>();
    private List<String> linePositions =  new LinkedList<>();
    //private List<String> errosSintatico =  new LinkedList<>(); 
    private String erros;
    private boolean erro;
    
    public boolean getErro(){
        return this.erro;
    }
    
    public String getErros(){
        return this.erros;
    }
    //onde provavelmente vai ser nossa logida do analisador
    public List<String> IniciarSintatico(List<String> linhaArquivo){
        System.out.println("Analizador Iniciado");
        this.linhaArquivo = linhaArquivo;
        String temp[];
        
        int tokenListSize = linhaArquivo.size();
        for(int i = 0; i < tokenListSize; i++) {
            
            temp = formataTipo(i).split(" ");
            
            if((!"Error".equals(temp[0]))&&(!"Comentario".equals(temp[0]) )) {
                tokenList.add(temp[0]);
                valueList.add(temp[1]);
                linePositions.add(temp[2]);
            }
        }
        //Indicador de fim de documento
        tokenList.add("##");
        valueList.add("##");
        linePositions.add("final");
        //Segurança caso o head tente ler à frente
        tokenList.add("##");
        valueList.add("##");
        linePositions.add("final");
        
        Producoes producoes = new Producoes(tokenList, valueList, linePositions);
        // Aqui abaixo é aonde começaria a valiação do código; Depois de adicionadas todas as produções, a primeira
        //função a ser chamada deveria ser a "algoritimo"; por enquanto está atribuicao, para testes.
        
        System.out.println(producoes.iniciar());
        this.erros = producoes.getErros();//pegar os erros como string
        this.erro=producoes.getErro();
       // System.out.println(producoes.codigoGeral());
        
        return null;
    }
    
    
    /**
     * Ler uma linha especifica do arquivo
     * @param num Número da linha que deseja ler do arquivo
     * @return String contida na posição especificada.
     */
    private String lerLinha(int num){
        return this.linhaArquivo.get(num);
        
    }
    /**
     * Metodo para retornar o tipo do lexema(sujeito a mudanças futuras)
     * @param numLinha Número da linha do arquivo a ser analisado
     * @return String com o tipo de lexema
     */
    private String formataTipo(int numLinha){
        String temp[];
        temp=lerLinha(numLinha).split(" ");//dividir a string em pedaços separados por espaços
        if(temp[0].equals("PalavraReservada")){
            return "PalavraReservada "+temp[1]+" "+temp[2];    
        }else if(temp[0].equals("Identificador")){
            return "Identificador "+temp[1]+" "+temp[2];
        }else if(temp[0].equals("Número")){
            return "Numero "+temp[1]+" "+temp[2];
        }else if(temp[0].equals("Delimitador")){
            return "Delimitador "+temp[1]+" "+temp[2];
        }else if(temp[0].equals("Operador")){
            if(temp[1].equals("simples")){
                return "OperadorSimples "+temp[2]+" "+temp[3];
            }else{
                return "OperadorDuplo "+temp[2]+" "+temp[3];
            }   
        }else if(temp[0].equals("Caracter")){
            return "Caracter "+temp[1]+" "+temp[2];
        }else if(temp[0].equals("Cadeia")){
            return "Cadeia "+temp[1]+" "+temp[2];  
        }else if(temp[0].equals("Comentário")){
            if(temp[1].equals("mal")){
                return "Error";
            }else{
               return "Comentario "+temp[3]+" "+temp[4]; 
            }  
        }else if(temp[0].equals("Token")||temp[0].equals("Erro")){
            return "Error";
        }else{
            return "Error";
        } 
    }
}
