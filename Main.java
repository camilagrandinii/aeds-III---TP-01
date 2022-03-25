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

    int op=0;
    int id=0;
    while(op!=6){
      op=sc.nextInt();
        switch (op){
          case 1:
          try{ banco.criaConta(id);
          } catch(Exception e){e.printStackTrace();}
          id++;
          break;

          case 2:
          try{ banco.Transferencia();
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 3:
          try{ banco.leRegistro();
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 4:
          try{ banco.atualizaRegistro();
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 5:
          try{ banco.deletaRegistro();
          }catch(Exception e){
            e.printStackTrace();
          }
          break;

          case 6:
          System.out.println("Saindo do programa...");
          try{
            banco.closeFile();
          }catch (Exception e){}
          break;
        }
        if (op!=6) printMenu();
    }
        sc.close();
  }
}
