/**
 * TP I - Algoritmos e Estruturas de Dados III
 * @author - Camila Lacerda Grandini & Joana Woldaysnky
 * 2022 - 3o. Semestre
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Banco{
    RandomAccessFile arq;

    /*
    Método construtor da classe banco
    */
    Banco(){
        try{ arq = new RandomAccessFile("dados/banco.db", "rw");
        }catch(Exception e){}   
    }

    /*
    Método que fecha o arquivo aberto pelo construtor
    */
    public void closeFile() throws Exception{
        arq.close();
    }
    /*
    Método que testa se o ID da conta criada é maior que o maior ID salvo
    SE SIM, salva o ID da conta criada no lugar do outro
    */

    public void testaID(int id){
        try{
            arq.seek(0); 
            int ultimo_id = arq.readInt();
            if (id>ultimo_id)
            arq.writeInt(id);
        }catch(Exception e){e.printStackTrace();}
    }

    /*
    Método que cria a conta do usuario e salva ela no arquivo de dados
    em uma registro contendo: lápide, id, nome, cpf, cidade, transf realizadas e saldo
    */
    public void criaConta(int id){
        Scanner sc = new Scanner(System.in);
        byte[] ba;

        System.out.println("Escreva o seu nome: ");
        String nome = sc.next();
        System.out.println("Escreva o seu cpf (sem tracos e/ou pontos): ");
        String cpf = sc.next();
        System.out.println("Escreva a sua cidade: ");
        String cidade = sc.next();

        Conta conta = new Conta(id, nome, cpf, cidade);
        
        
        try{ba = conta.toByteArray();

        if (id==0){
        arq.writeInt(id);
        }
        else{
        testaID(id);
        }

        long pos = arq.length();
        arq.seek(pos);

        arq.writeInt(ba.length);
        arq.write(ba);

        }catch(Exception e){
            e.printStackTrace();
        }
        sc.close();
    }

    /*
    Método que realiza transferencias entre os usuários, pegando como base o cpf de cada um
    */
    public void Transferencia(){
    Scanner sc = new Scanner(System.in);
        System.out.println("Qual será a conta debitada? (cpf sem traços e/ou pontos) ");
        String cpf_debito = sc.next();
        System.out.println("Qual será a conta creditada? (cpf sem traços e/ou pontos) ");
        String cpf_credito = sc.next();
        System.out.println("Escreva o valor da transferencia: ");
        float valor_transf = sc.nextFloat();

        try{ realizaTransf(cpf_debito, cpf_credito, valor_transf);

        }catch(Exception e){
            e.printStackTrace();
        }
    sc.close();
    }
    public void realizaTransf(String cpf_debito, String cpf_credito, float valor) throws Exception{
        Conta conta = new Conta();
            try{
            
                byte[] ba;
                int tam;
                int ultimo_id = arq.readInt();

                while (arq.getFilePointer()<arq.length()){ 
                Conta cRead = new Conta();

                long pos1 = arq.getFilePointer();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);
                
                    if (cpf_debito==cRead.cpf){
                    cRead.saldoConta-=valor;
                    cRead.transferenciasRealizadas++;

                    ba = cRead.toByteArray();
                    arq.seek(pos1);
                    arq.writeInt(ba.length);
                    arq.write(ba);
                    }
                }

                arq.seek(0);
                ultimo_id = arq.readInt();

                while (arq.getFilePointer()<arq.length()){ 
                    Conta cRead = new Conta();
    
                    long pos1 = arq.getFilePointer();
                    tam = arq.readInt();
                    ba = new byte[tam];
                    arq.read(ba);
                    cRead.fromByteArray(ba);
                    
                        if (cpf_credito==cRead.cpf){
                        cRead.saldoConta+=valor;
                        cRead.transferenciasRealizadas++;
    
                        ba = cRead.toByteArray();
                        arq.seek(pos1);
                        arq.writeInt(ba.length);
                        arq.write(ba);
                        }
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    /*
    Método que procura e le o registro que corresponda a ID provida e printa na tela
    */
    public void leRegistro() throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual o registro desejado para leitura? ");
        int id = sc.nextInt();

        try{
            byte[] ba;
            int tam;
            int ultimo_id = arq.readInt();
                
            while (arq.getFilePointer()<arq.length()){ 
            Conta cRead = new Conta();
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            cRead.fromByteArray(ba);
            
            if (id==cRead.idConta)
            cRead.toString();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sc.close();
    }
    public void deletaRegistro() throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual o registro desejado para leitura? ");
        int id = sc.nextInt();
    
        try{
            byte[] ba;
            int tam;
            int ultimo_id = arq.readInt();

            while (arq.getFilePointer()<arq.length()){ 
            Conta cRead = new Conta();
            long pos1 = arq.getFilePointer();
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            cRead.fromByteArray(ba);
            cRead.lapide=true;

            ba = cRead.toByteArray();
                arq.seek(pos1);
                arq.writeInt(ba.length);
                arq.write(ba);
            
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sc.close();
    }
    
}