
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mateus
 */
public class AnalizadorLexico {
    
    public LinkedList<String> gerarTokens(LinkedList<String> linhas){
        LinkedList<String> expressoes;
        LinkedList<String> tokens = new LinkedList<String>();
        expressoes = separarTokens(linhas);
        
        Automato automatoLexico = new Automato();
        for(int i=0; i< expressoes.size(); i++){
            tokens.add(automatoLexico.iniciar(expressoes.get(i)));
        }
        
    return tokens;    
    
    };
    
    private LinkedList<String> separarTokens(LinkedList<String> linhas){
        LinkedList<String> expressoes = new LinkedList<String>();
        /*Aqui serão separadas pelo espaço e pelos delimitadores cada esxpressao a ser traduzida em um token*/
        return expressoes;
    };
    
    
}
