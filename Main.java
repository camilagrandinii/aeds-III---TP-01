/**
 * TP I - Algoritmos e Estruturas de Dados III
 * @author - Camila Lacerda Grandini & Joana Woldaysnky
 * 2022 - 3o. Semestre
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
  public static void criaConta(int id){
    Scanner sc = new Scanner(System.in);
      ArrayList<Conta> alunosWrite = new ArrayList<Conta>();
      System.out.println("Escreva o seu nome: ");
      String nome = sc.next();
      System.out.println("Escreva o seu cpf (sem tracos e/ou pontos): ");
      String cpf = sc.next();
      System.out.println("Escreva a sua cidade: ");
      String cidade = sc.next();

      Conta aluno = new Conta(id, nome, cpf, cidade);
      alunosWrite.add(aluno);
    sc.close();
  }
  public static void realizaTransferencia(){
    Scanner sc = new Scanner(System.in);
      System.out.println("Qual será a conta debitada? (cpf sem traços e/ou pontos) ");
      String cpf_debito = sc.next();
      System.out.println("Qual será a conta creditada? (cpf sem traços e/ou pontos) ");
      String cpf_credito = sc.next();
      System.out.println("Escreva o valor da transferencia: ");
      float valor_transf = sc.nextFloat();
    sc.close();
  }
  public static void leRegistro(int id) throws Exception{
    RandomAccessFile arq; //ja traz os métodos do dataOutputStream ou inputStream
    try{
      arq = new RandomAccessFile("dados/conta.db", "rw");
    
      byte[] ba;
      int tam;

      arq.seek(0);
      while (arq.getFilePointer()<arq.length()){ 
        Conta aRead = new Conta();
        tam = arq.readInt();
        ba = new byte[tam];
        arq.read(ba);
        aRead.fromByteArray(ba);
        
        if (id==aRead.idConta)
        aRead.toString();
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void printMenu(){
    System.out.println("MENU: \n1 - Criar uma Conta Bancária \n2 - Realizar uma Transferência \n3 - Ler um Registro \n4 - Atualizar um Registro \n5 - Deletar um Registro\n6 - Sair ");
  }
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    printMenu();
    ArrayList<Conta> alunosRead = new ArrayList<Conta>();
    int op=sc.nextInt();
    int id=0;
    do{
        switch (op){
          case 1:
          criaConta(id);
          id++;
          break;

          case 2:
          realizaTransferencia();
          break;

          case 3:
          System.out.println("Qual o registro desejado para leitura? ");
          try{ leRegistro(id); 
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 4:
          break;

          case 5:
          break;

          case 6:
          System.out.println("Saindo do programa...");
          break;
        }
        printMenu();
        op=sc.nextInt(); 
    }while(op==0);
        sc.close();

    RandomAccessFile arq; //ja traz os métodos do dataOutputStream ou inputStream
    byte[] ba;
    int tam;

    /*
        arq = new RandomAccessFile("dados/conta.db", "rw");
        for (int i=0; i<alunosWrite.size(); i++){
            ba = alunosWrite.get(i).toByteArray();
            arq.writeInt(ba.length);
            arq.write(ba);
        }
    */

    for (int i=0; i<alunosRead.size(); i++){
      System.out.println(alunosRead.get(i));
    }
    System.out.println("oi");
    
  }
}
