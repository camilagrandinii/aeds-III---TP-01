package dados;

import java.io.RandomAccessFile;
import java.util.Scanner;
import java.io.IOException;

    class main3 {
    public static Scanner sc = new Scanner(System.in);
    public static void CRUD(int escolha, Scanner sc) throws IOException {
        RandomAccessFile arq = new RandomAccessFile("dados/cliente.db", "rw");

        byte[] ba;
        Cliente c1;

        switch(escolha){
     //create
            case 1:
            String nome = "";
            String cpf = "";
            String cidade = "";

            sc.nextLine();
            for(int x = 0; x != 1;){
                System.out.print("Por favor digite o nome:"); nome = sc.nextLine();
                if(nome.length() < 100){
                    x = 1;
                }else System.out.println("Por favor, abrevie o nome!");
            }
            for(int x = 0; x != 1;){
                System.out.print("Por favor digite o cpf do paciente->"); cpf = sc.nextLine();
                if((cpf.length() >= 1 && cpf.length() <= 999999999)){
                    x = 1;
                }else System.out.println("Por favor, digite um cpf válido!");
            }
            for(int x = 0; x != 1;){
                System.out.print("Por favor digite a cidade do cliente:"); cidade = sc.nextLine();
                if(cidade.length() < 30){
                    x = 1;
                }else System.out.println("Por favor, abrevie o nome da cidade!");
            }

            c1 = new Cliente(nome, cpf, cidade);

            ba = c1.toByteArray();
            arq.seek(arq.length());
            arq.writeChar(' ');//escrita da lapide
            arq.writeInt(ba.length);//escrita do tamanho do arquivo
            arq.write(ba);

            break;

            case 2:
            int idCliente;

            System.out.print("Id do cliente: ");
            idCliente = sc.nextInt();

            char lapide;
            int tam = 0;
            int idDetectado = -1;

            try{
                int i = 4;

                while (idCliente != idDetectado && i < arq.length()) {
                    arq.seek(i);

                    lapide = arq.readChar();// Leitura da lapide 
                    i += 2;
                    arq.seek(i);
                    tam = arq.readInt();
                    i += 4;
                    
                    if (lapide == ' ') {
                        arq.seek(i);
                        idDetectado = arq.readInt();
                    }
                    i += tam;
                }

                if (idCliente == idDetectado) {
                    arq.seek(i - tam);
                    ba = new byte[tam];
                    arq.read(ba);
                    c1 = new Cliente();
                    c1.fromByteArray(ba);
                    c1.print();

                } else {
                    System.out.println("Arquivo nao encontrado");
                }
                
            } catch (Exception e) {
                System.out.println("Arquivo nao encontrado (nenhum registro no arquivo)");
            }
            break;


            case 3:
     
             System.out.print("Id do Cliente: ");
             idCliente = sc.nextInt();
    
             char lapide2;
             int tam2 = 0;
             int idDetectado2 = -1;

             try{
                 int i = 4;
     
                 while (idCliente != idDetectado2 && i < arq.length()) {
                     arq.seek(i);
     
                     lapide2 = arq.readChar();// Leitura da lapide 
                     i += 2;
                     arq.seek(i);
                     tam2 = arq.readInt();
                     i += 4;
                     
                     if (lapide2 == ' ') {
                         arq.seek(i);
                         idDetectado2 = arq.readInt();
                     }
                     i += tam2;
                 }
     
                 if (idCliente == idDetectado2) {
                     //Criar nome cliente
     
                     String nome2 = "";
                     String cpf2 = "";
                     String cidade2 = "";
                     int transferencias = -1;
                     float saldo = 0F;
     
                     for(int x = 0; x != 1;){
                         System.out.print("Por favor digite o nome:"); 
                         sc.nextLine();
                         nome2 = sc.nextLine();
                         if(nome2.length() < 100){
                             x = 1;
                         }
                         else System.out.println("Por favor, abrevie o nome!");
                     }
                     for(int x = 0; x != 1;){
                         System.out.print("Por favor digite o cpf do paciente->"); cpf2 = sc.nextLine();
                         if((cpf2.length() >= 1 && cpf2.length() <= 999999999)){
                             x = 1;
                         }else System.out.println("Por favor, digite um cpf válido!");
                     }
                     for(int x = 0; x != 1;){
                         System.out.print("Por favor digite a cidade do cliente:"); cidade2 = sc.nextLine();
                         if(cidade2.length() < 30){
                             x = 1;
                         }else System.out.println("Por favor, abrevie o nome da cidade!");
                     }
                     for(int x = 0; x != 1;){
                         System.out.print("Por favor digite as tranferencias realizadas:"); transferencias = sc.nextInt();
                         x=1;
                     }
                     for(int x = 0; x != 1;){
                         System.out.print("Por favor digite seu saldo:"); saldo = sc.nextInt();
                         x=1;
                     }
     
                     c1 = new Cliente(idCliente, nome2, cpf2, cidade2, transferencias, saldo);
                     ba = c1.toByteArray();
     
                     //tentar colocar novo cliente no lugar do cliente antigo
                     if (ba.length <= tam2){
                         arq.seek(i - tam2);
                         arq.write(ba);
     
                     }
                     //apagar cliente antigo e colocar o cliente novo no final do arquivo
                     else{
                         arq.seek(i - (tam2 + 6));
                         arq.writeChar('*');
                         arq.seek(arq.length());
                         arq.writeChar(' ');
                         arq.writeInt(ba.length);
                         arq.write(ba);
     
                     }
                 } else {
                     System.out.println("Arquivo nao encontrado");
                 }
     
             }catch (Exception e) {
                 System.out.println("Arquivo nao encontrado (nenhum registro no arquivo)");
             }
             break;
               

            case 4:
                Cliente c2;

                int idCliente1;
                int idCliente2;
                int qntTransferida=0;

                System.out.print("Id do cliente 1: ");
                idCliente1 = sc.nextInt();
                System.out.print("Id do cliente 2: ");
                idCliente2 = sc.nextInt();
                System.out.print("Quanto você quer conferir:: ");
                qntTransferida = sc.nextInt();

                char lapide3;
                int tam3 = 0;
                int idDetectado3 = -1;

                int transferencias = -1;
                float saldo = 0F;

                try{
                    int i = 4;
        
                    while (idCliente1 != idDetectado3 && i < arq.length()) {
                        arq.seek(i);
        
                        lapide3 = arq.readChar();// Leitura da lapide 
                        i += 2;
                        arq.seek(i);
                        tam3 = arq.readInt();
                        i += 4;
                        
                        if (lapide3 == ' ') {
                            arq.seek(i);
                            idDetectado3 = arq.readInt();
                        }
                        i += tam3;
                    }
        
                    if (idCliente1 == idDetectado3) {

                        arq.seek(i - tam3);
                        ba = new byte[tam3];
                        arq.read(ba);
                        c1 = new Cliente();
                        c1.fromByteArray(ba);
                        c1.saldoConta += qntTransferida;
                        c1.print();
        
                        //return c1
        
                    } else {
                        System.out.println("Arquivo nao encontrado");
                    }
        
                    while (idCliente2 != idDetectado3 && i < arq.length()) {
                        arq.seek(i);
        
                        lapide2 = arq.readChar();// Leitura da lapide 
                        i += 2;
                        arq.seek(i);
                        tam2 = arq.readInt();
                        i += 4;
                        
                        if (lapide2 == ' ') {
                            arq.seek(i);
                            idDetectado3 = arq.readInt();
                        }
                        i += tam2;
                    }
        
                    if (idCliente2 == idDetectado3) {
                        arq.seek(i - tam3);
                        ba = new byte[tam3];
                        arq.read(ba);
                        c2 = new Cliente(transferencias, saldo);
                        c2.fromByteArray(ba);
                        c2.saldoConta -= qntTransferida;
                        c2.print();
        
                        //return c1
        
                    } else {
                        System.out.println("Arquivo nao encontrado");
                    }
                } catch (Exception e) {
                    System.out.println("Arquivo nao encontrado (nenhum registro no arquivo)");
                }
            break;

            case 5:
            
                System.out.print("Id do cliente: ");
                idCliente = sc.nextInt();

                char lapide4;
                int tam4 = 0;
                int idDetectado4 = -1;
             
                try{
                    int i = 4;
            
                    while (idCliente != idDetectado4 && i < arq.length()) {
                        arq.seek(i);
            
                        lapide4 = arq.readChar();// Leitura da lapide 
                        i += 2;
                        arq.seek(i);
                        tam4 = arq.readInt();
                        i += 4;
                        
                        if (lapide4 == ' ') {
                            arq.seek(i);
                            idDetectado4 = arq.readInt();
                        }
                        i += tam4;
                    }
            
                    if (idCliente == idDetectado4) {
                    arq.seek(i - (tam4 + 6));
                    arq.writeChar('*');
                    System.out.println("\nArquivo deletado:");
            
                    arq.seek(i - tam4);
                    ba = new byte[tam4];
                    arq.read(ba);
                    c1 = new Cliente();
                    c1.fromByteArray(ba);
                    c1.print();
            
                } else {
                        System.out.println("Arquivo nao encontrado");
                    }
            
                } catch (Exception e) {
                    System.out.println("Arquivo nao encontrado (nenhum registro no arquivo)");
                }
                break;
                

                case 6:
  //leitura de todos


try{
        int i = 4;// é importante que i comece valendo quatro para o programa pular a leitura do
                // cabecalho do arquivo

        while (i < arq.length()) {
            arq.seek(i);

            lapide = arq.readChar();// leitura da lapide de determinado time
            i += 2;
            arq.seek(i);
            tam = arq.readInt();// leiturea do tamanho de cada time
            i += 4;

            if (lapide == ' ') {
                arq.seek(i);
                ba = new byte[tam];
                arq.read(ba);
                c1 = new Cliente();
                c1.fromByteArray(ba);
                c1.print();
            }

            i += tam;
        } 
    }catch (Exception e) {}
                    break;

                    case 0:
                    System.out.println("\nTenha um otimo dia!");
                    break;

                   default:
                    System.out.println("\nPor favor, escolha uma opcao valida!");
                   break;
        }
                    arq.close();
}
    public static void main (String[] args) throws Exception{

        int escolha=0;

        //MENU RECURSIVO COM OPCAO DE PARADA SE NECESSARIO
        do{
            System.out.println("\nMenu de opcoes:\n1 - Criar uma conta\n2 - Obter dados de um cliente\n"+
                            "3 - Alterar dados do cliente\n4 - Realizar uma Transferencia\n5 - Deletar\n6 - Dados de todos os clientes\n0 - Sair do sistema");
            System.out.print("\nQual opção desejada:"); 
            escolha = sc.nextInt();
            sc.nextLine();// essa linha de codigo serve para evitar um erro q ocorre ao usar o nextLine() logo apos o nextInt()
            CRUD(escolha, sc);
            
        }while(escolha != 0);
    }

}

