
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author murilo
 */
public class AnalisadorSintatico {
    //criar a logica do sintatico
    
    //variaveis
    private LinkedList<String> linhaArquivo;
    
    //onde provavelmente vai ser nossa logida do analisador
    public LinkedList<String> IniciarSintatico(LinkedList<String> linhaArquivo){
        this.linhaArquivo = linhaArquivo;
        
        
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
    private String identificarTipo(int numLinha){
        String temp[];
        temp=lerLinha(numLinha).split(" ");//dividir a string em pedaços separados por espaços
        if(temp[0]=="PalavraReservada"){
            return "PalavraReservada";    
        }else if(temp[0]=="Identificador"){
            return "Identificador";
        }else if(temp[0]=="Número"){
            return "Numero";
        }else if(temp[0]=="Delimitador"){
            return "Delimitador";
        }else if(temp[0]=="Operador"){
            if(temp[1]=="simples"){
                return "OperadorSimples";
            }else{
                return "OperadorDuplo";
            }   
        }else if(temp[0]=="Caracter"){
            return "CaracterConstante";
        }else if(temp[0]=="Cadeia"){
            return "CadeiaConstante";  
        }else if(temp[0]=="Comentário"){
            if(temp[1]=="mal"){
                return "Error";
            }else{
               return "Comentario"; 
            }  
        }else if(temp[0]=="Token"||temp[0]=="Erro"){
            return "Error";
        }else{
            return "Error";
        } 
    }
}
