/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author murilo
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class Arquivo {
    //lista para armazenar as strings
    private LinkedList<String> linha;
    
    /**
     * A lista contendo todas as linha do arquivo.
     * @return lista contendo todas as linha do arquivo, cada indice da lista corresponde a uma linha do arquivo
     */
    public LinkedList<String> getLinhas(){
        return this.linha;
    }
    /**
     * Metodo visualizar os elementos da lista, apenas para testes
     */
    public void print(){
        for(int i=0; i<linha.size(); i++){
            System.out.println(linha.get(i));
        }
    }
      
    /**
     * Método para ler um arquivo e armazenar as linhas do arquivo em uma lista
     * @param nome String contendo o path/nome do arquivo txt
     */
    public void read(String nome){
        linha = new LinkedList<String>();
        try {
        FileReader arq = new FileReader(nome);
        BufferedReader lerArq = new BufferedReader(arq);

        String l = lerArq.readLine(); // lê a primeira linha
        linha.add(l); //adicionar a linha do arquivo a lista
         // a variável "l" recebe o valor "null" quando o processo
        // de repetição atingir o final do arquivo texto
        while (l != null) {
          //System.out.printf("%s\n", l); //teste

          l = lerArq.readLine(); // lê da segunda até a última l
          linha.add(l); //adicionar a linha do arquivo a lista
        }

        arq.close();
      } catch (IOException e) {
          System.err.printf("Erro na abertura do arquivo: %s.\n",
            e.getMessage());
      }
      
    }
  

    
}
