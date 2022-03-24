/**
 * TP I - Algoritmos e Estruturas de Dados III
 * @author - Camila Lacerda Grandini & Joana Woldaysnky
 * 2022 - 3o. Semestre
 */

import java.util.Scanner;
public class Main {
  
  public static void printMenu(){
    System.out.println("MENU: \n1 - Criar uma Conta Bancária \n2 - Realizar uma Transferência \n3 - Ler um Registro \n4 - Atualizar um Registro \n5 - Deletar um Registro\n6 - Sair ");
  }
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Banco banco = new Banco(); //criando instância da classe banco para realizar as ações sobre ele
    printMenu();

    int op=sc.nextInt();
    int id=0;
    do{
        switch (op){
          case 1:
          banco.criaConta(id);
          id++;
          break;

          case 2:
          banco.Transferencia();
          break;

          case 3:
          try{ banco.leRegistro();
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 4:
          break;

          case 5:
          try{ banco.deletaRegistro();
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 6:
          try{
            banco.closeFile();
          }catch (Exception e){}
          System.out.println("Saindo do programa...");
          break;
        }
        printMenu();
        op=sc.nextInt();
        System.out.println("valor salvo em op: "+op); 
    }while(op==0);
        sc.close();
  }
}
