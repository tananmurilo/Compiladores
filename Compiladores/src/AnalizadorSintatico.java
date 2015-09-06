
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
    
    
    //onde provavelmente vai ser nossa logida do analisador
    public List<String> IniciarSintatico(List<String> linhaArquivo){
        System.out.println("Analizador Iniciado");
        this.linhaArquivo = linhaArquivo;
        String temp[];
        
        
        for(int i = 0; i < linhaArquivo.size(); i++) {
            
            temp = formataTipo(i).split(" ");
            
            if(temp[0] != "Error") {
                tokenList.add(temp[0]);
                valueList.add(temp[1]);
                linePositions.add(temp[2]);
            }
        }
        Producoes producoes = new Producoes(tokenList, valueList, linePositions);
        // Aqui abaixo é aonde começaria a valiação do código; Depois de adicionadas todas as produções, a primeira
        //função a ser chamada deveria ser a "algoritimo"; por enquanto está atribuicao, para testes.
        //System.out.println(producoes.atr());
        //n consegui fazer de outro jeito sem ser com o lambida como ;
        //executa teste para 1=1; 2-a+4*2; 3+(4)-5; 2+(d+1)*3; dao o esperado(verdadeiro)
        // para 3+); 23+4); 
        //problema quando abre uma chave e não fecha da verdadeiro 9+(6-9;
        System.out.println(producoes.condicao());
        
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
            return "Error ";
        } 
    }
}
