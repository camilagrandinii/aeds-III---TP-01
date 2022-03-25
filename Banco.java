/**
 * TP I - Algoritmos e Estruturas de Dados III
 * @author - Camila Lacerda Grandini & Joana Woldaysnky
 * 2022 - 3o. Semestre
 */

import java.io.RandomAccessFile;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Banco{
    RandomAccessFile arq;
    Scanner sc;

    /*
    Método construtor da classe banco
    */
    Banco(){
        try{ arq = new RandomAccessFile("dados/banco.db", "rw");
        }catch(FileNotFoundException e){ System.out.println("Erro: Arquivo nao encontrado!");}   
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
    public void testaID(int id) throws Exception{
        try{
            arq.seek(0); 
            int ultimo_id = arq.readInt();
            if (id>ultimo_id)
            arq.writeInt(id);
        }catch(IOException e){System.out.println("Erro: Nao foi possível ler o arquivo!");}
    }

    /*
    Método que cria a conta do usuario e salva ela no arquivo de dados
    em uma registro contendo: lápide, id, nome, cpf, cidade, transf realizadas e saldo
    */
    public void criaConta(int id) throws Exception{
        byte[] ba;
        boolean teste = false;
        String nome="", cpf="", cidade="";

        while(teste==false){
            System.out.println("Escreva o seu nome: ");
            sc.nextLine();
            nome = sc.nextLine();
            if(nome.length()<100)
            teste=true;
            else System.out.println("Nome invalido, digite uma abreviacao por favor!");
        }

        teste=false;
        
        while(teste==false){
            System.out.println("Escreva o seu cpf (sem tracos e/ou pontos): ");
            cpf = sc.nextLine();
            if(cpf.length()==11)
            teste=true;
            else System.out.println("Digite um CPF valido!");
        }

        teste=false;

        while(teste==false){
            System.out.println("Escreva a sua cidade: ");
            cidade = sc.nextLine();

            if (cidade.length()<40)
            teste=true;
            else System.out.println("Nome da cidade invalido!");
        }

        Conta conta = new Conta(id, nome, cpf, cidade);
        
        try{
            ba = conta.toByteArray();
            arq.seek(0);
            if (id==0){
            arq.writeInt(id);
            }
            else{
                try{ testaID(id); 
                }catch (IOException e){System.out.println("Erro: Nao foi possível ler o arquivo!");}
            }

            arq.seek(arq.length());
            arq.writeInt(ba.length);
            arq.write(ba);

        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }

    /*
    Método que pega os dados para realizar a transferencia e chama outro metodo para realiza-la
    */
    public void Transferencia() throws Exception{
        String cpf_debito="", cpf_credito="";
        boolean teste=false;
        while(teste==false){
            System.out.println("Qual será a conta debitada? (cpf sem traços e/ou pontos) ");
            cpf_debito = sc.next();
            if(cpf_debito.length()==11)
            teste=true;
            else System.out.println("Digite um CPF valido!");
        }
        teste=false;
        while(teste==false){
            System.out.println("Qual será a conta creditada? (cpf sem traços e/ou pontos) ");
            cpf_credito = sc.next();
            if(cpf_credito.length()==11)
            teste=true;
            else System.out.println("Digite um CPF valido!");
        }
        System.out.println("Escreva o valor da transferencia: ");
        float valor_transf = sc.nextFloat();

        try{ realizaTransf(cpf_debito, cpf_credito, valor_transf);

        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }

    /*
    Método que realiza transferencias entre os usuários, pegando como base o cpf de cada um e o valor informado pela
    função Transferencia()
    */
    public void realizaTransf(String cpf_debito, String cpf_credito, float valor) throws Exception{
        byte[] ba;
        int tam;    
            try{
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
                
            } catch(IOException e){
                System.out.println("Erro: Nao foi possível ler o arquivo!");
            }
        }
        
    /*
    Método que procura e le o registro que corresponda a ID provida e printa na tela
    */
    public void leRegistro() throws Exception{
        System.out.println("Qual o registro desejado para leitura? ");
        int id = sc.nextInt();
        byte[] ba;
        int tam;

        try{

            arq.seek(4);
            while (arq.getFilePointer()<arq.length()){ 
                Conta cRead = new Conta();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);
                
                if (id==cRead.idConta){
                    System.out.println(cRead.toString()+"\n");
                }
                else
                System.out.println("Erro: ID nao encontrado!");
            }
            
        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }

    /*
    Método que procura e "deleta" (marca a lápide como true) o registro que possua a ID provida
    */
    public void deletaRegistro() throws Exception{
        System.out.println("Qual registro voce deseja deletar? ");
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
                else{
                    System.out.println("Erro: ID nao encontrado!");
                }
            }
        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }

    /*
    Método que procura e atualiza o registro que possua a ID provida
    */
    public void atualizaRegistro() throws Exception{
        System.out.println("Qual registro voce deseja atualizar? ");
        int id = lerInteiro();
        boolean teste=false;
    
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

                        while(teste==false){
                            System.out.println("Escreva o seu nome: ");
                            cRead.nomePessoa = sc.nextLine();
                            if(cRead.nomePessoa.length()<100)
                            teste=true;
                            else System.out.println("Nome invalido, digite uma abreviacao por favor!");
                        }

                        teste=false;
                        
                        while(teste==false){
                            System.out.println("Escreva o seu cpf (sem tracos e/ou pontos): ");
                            cRead.cpf = sc.nextLine();
                            if(cRead.cpf.length()==11)
                            teste=true;
                            else System.out.println("Digite um CPF valido!");
                        }

                        teste=false;

                        while(teste==false){
                            System.out.println("Escreva a sua cidade: ");
                            cRead.cidade = sc.nextLine();

                            if (cRead.cidade.length()<40)
                            teste=true;
                            else System.out.println("Nome da cidade invalido!");
                        }

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
        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }

    /*
    Função que transforma o inteiro lido com nextLine em inteiro 
    para evitar o erro do comando nextLine ler o enter do nextInt 
    */
    public int lerInteiro() {
        String digitado = "";
    
        digitado = sc.nextLine();
    
        return Integer.parseInt(digitado);
    }
}