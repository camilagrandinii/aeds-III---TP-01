/**
 * TP I - Algoritmos e Estruturas de Dados III
 * @author - Camila Lacerda Grandini & Joana Woldaysnky
 * 2022 - 3o. Semestre
 */

import java.io.RandomAccessFile;
import java.util.Scanner;

public class Banco{
    RandomAccessFile arq;
    Scanner sc;

    /*
    Método construtor da classe banco
    */
    Banco(){
        try{ arq = new RandomAccessFile("dados/banco.db", "rw");
        }catch(Exception e){}   
        sc = new Scanner(System.in);
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
        byte[] ba;

        System.out.println("Escreva o seu nome: ");
        String nome = sc.nextLine();
        System.out.println("Escreva o seu cpf (sem tracos e/ou pontos): ");
        String cpf = sc.next();
        System.out.println("Escreva a sua cidade: ");
        String cidade = sc.nextLine();

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
    }

    /*
    Método que realiza pega os dados para realizar a transferencia e chama outro metodo para realiza-la
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

    /*
    Método que realiza transferencias entre os usuários, pegando como base o cpf de cada um
    */
    public void realizaTransf(String cpf_debito, String cpf_credito, float valor) throws Exception{
            try{
            
                byte[] ba;
                int tam;
                arq.seek(4);

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

                arq.seek(4);
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
        System.out.println("Qual o registro desejado para leitura? ");
        int id = sc.nextInt();

        try{
            byte[] ba;
            int tam;
            arq.seek(4);
                
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
    }

    /*
    Método que procura e "deleta" (marca a lápide como true) o registro que possua a ID provida
    */
    public void deletaRegistro() throws Exception{
        System.out.println("Qual o registro desejado para deletar? ");
        int id = sc.nextInt();
    
        try{
            byte[] ba;
            int tam;
            arq.seek(4);

            while (arq.getFilePointer()<arq.length()){ 
            Conta cRead = new Conta();
            long pos1 = arq.getFilePointer();
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            cRead.fromByteArray(ba);
            
                if (id==cRead.idConta){
                cRead.lapide=true;

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
    Método que procura e atualiza o registro que possua a ID provida
    */
    public void atualizaRegistro() throws Exception{
        System.out.println("Qual registro voce deseja atualizar? ");
        int id = sc.nextInt();
    
        try{
            byte[] ba;
                int tam;
                arq.seek(4);

                while (arq.getFilePointer()<arq.length()){ 
                Conta cRead = new Conta();

                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);
                
                    if (id==cRead.idConta){
                        long pos1 = arq.getFilePointer();

                        System.out.println("Escreva o seu nome: ");
                        String nome = sc.nextLine();
                        cRead.nomePessoa=nome;
                        System.out.println("Escreva o seu cpf (sem tracos e/ou pontos): ");
                        String cpf = sc.next();
                        cRead.cpf=cpf;
                        System.out.println("Escreva a sua cidade: ");
                        String cidade = sc.nextLine();
                        cRead.cidade=cidade;

                        ba = cRead.toByteArray();
                        if(tam==ba.length){
                        arq.seek(pos1);
                        arq.writeInt(ba.length);
                        arq.write(ba);
                        }
                        else{
                        arq.seek(pos1+4);
                        cRead.lapide=true;
                        ba = cRead.toByteArray();
                        arq.seek(arq.length());
                        arq.writeInt(ba.length);
                        arq.write(ba);
                        }
                    }
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}