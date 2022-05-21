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
    RandomAccessFile arq, arqIndice;
    Scanner sc;

    /*
    Método construtor da classe banco
    */
    Banco(){
        try{ arq = new RandomAccessFile("dados/banco.db", "rw");
        }catch(FileNotFoundException e){ System.out.println("Erro: Arquivo nao encontrado!");}   
        try{ arqIndice = new RandomAccessFile("dados/indicesDados.db", "rw");
        }catch(FileNotFoundException e){ System.out.println("Erro: Arquivo nao encontrado!");}   
        sc = new Scanner(System.in);
    }

    /*
    Método que fecha o arquivo aberto pelo construtor
    */
    public void closeFile() throws Exception{
        arq.close();
        arqIndice.close();
    }
    /*
    Método que testa se o CPF da conta criada já existe
    SE SIM, não deixa criar a conta
    */
    public boolean testeCpfJaExistente(String cpf) throws Exception{
        boolean teste_cpf=false;
        int tam;
        byte[] ba;
        try{
            arq.seek(4); //pula o maior ID que esta gravado no arquivo

            while (arq.getFilePointer()<arq.length()){ 
            Conta cRead = new Conta();
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            cRead.fromByteArray(ba);

                if (cpf.equals(cRead.cpf)){
                teste_cpf=true;
                }
            }
        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
        return teste_cpf;
    }
    /*
    Método que testa se o ID da conta criada é maior que o maior ID salvo
    SE SIM, salva o ID da conta criada no lugar do outro
    */
    public void testaID(int id) throws Exception{
        try{
            arq.seek(0); 
            int ultimo_id = arq.readInt();
            if (id>ultimo_id){
            arq.seek(0);
                arq.writeInt(id);
            }
        }catch(IOException e){System.out.println("Erro: Nao foi possível ler o arquivo!");}
    }

    /*
    Método que cria a conta do usuario e salva ela no arquivo de dados
    em uma registro contendo: lápide, id, nome, cpf, cidade, transf realizadas e saldo
    */
    public void criaConta(int id) throws Exception{
        byte[] ba, baIndice;
        boolean teste = false;
        String nome="", cpf="", cidade="";

        while(teste==false){
            System.out.println("Escreva o seu nome: ");
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
        if (testeCpfJaExistente(cpf)==true)
        {
            throw new Exception("CPF ja cadastrado!");
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

            long pos1 = arq.getFilePointer(); //pega o endereço no qual está localizado o proximo registro que será escrito
            
            baIndice=conta.toByteArrayIndice(pos1); //cria um array de bytes com as informações: id, cpf e endereço de localização

            arq.writeInt(ba.length);
            arq.write(ba);

            arqIndice.seek(arqIndice.length());
            arqIndice.write(baIndice); //escreve o array de bytes no arquivo de indice

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
        long end_credito, end_debito;
            try{
                end_debito = pesquisa_binariaString(cpf_debito);
                Conta cRead = new Conta();

                if(end_debito!=-1){
                arq.seek(end_debito);
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);

                    cRead.saldoConta-=valor;
                    cRead.transferenciasRealizadas++;

                    ba = cRead.toByteArray();
                    arq.seek(end_debito);
                    arq.writeInt(ba.length);
                    arq.write(ba);
                }
                else System.out.println("Erro: cpf informado para debito nao foi encontrado");

                end_credito = pesquisa_binariaString(cpf_credito);

                if(end_credito!=-1){
                arq.seek(end_credito);
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);

                    cRead.saldoConta+=valor;
                    cRead.transferenciasRealizadas++;

                    ba = cRead.toByteArray();
                    arq.seek(end_credito);
                    arq.writeInt(ba.length);
                    arq.write(ba);
                }
                if(end_credito==-1)
                System.out.println("Erro: cpf informado para credito nao foi encontrado");
                
            } catch(IOException e){
                System.out.println("Erro: Nao foi possível ler o arquivo!");
            }
    }
        
    /*
    Método que procura e le o registro que corresponda a ID provida e printa na tela
    */
    public void leRegistro() throws Exception{
        System.out.println("Qual o registro desejado para leitura? ");
        int id = sc.nextInt(), tam;
        byte[] ba;
        long end_leitura;

        try{
            end_leitura=pesquisa_binaria(id);
            if(end_leitura!=-1){
                Conta cRead = new Conta();
                arq.seek(end_leitura);
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);
                
                System.out.println("cRead lapide = "+cRead.lapide+"id conta: "+cRead.idConta);

                if (cRead.lapide==false && id==cRead.idConta){
                    System.out.println(cRead.toString()+"\n");
                }
                else if(cRead.lapide==true && id==cRead.idConta){
                    System.out.println("O registro desejado foi excluido!");
                }
            }
            else
                System.out.println("Erro: ID nao encontrado!");
            
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
        byte[] ba;
        int tam;
        long end_deleta;
    
        try{
            end_deleta = pesquisa_binaria(id);
            if(end_deleta!=-1){
            Conta cRead = new Conta();
            arq.seek(end_deleta);
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            cRead.fromByteArray(ba);
            
            cRead.lapide=true;

            ba = cRead.toByteArray();
                arq.seek(end_deleta);
                arq.writeInt(ba.length);
                arq.write(ba);
                
            atualizaEnderecoIndice(cRead.idConta, -1);
            }
            else System.out.println("Erro: ID nao encontrado!");

        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }

    /*
    Método que procura e atualiza o registro que possua a ID provida
    */
    public void atualizaRegistro() throws Exception{
        System.out.println("Qual registro voce deseja atualizar? ");
        int id = sc.nextInt();
        byte[] ba;
        int tam;
        long end_atualiza;
        boolean teste=false;
    
        try{
                end_atualiza = pesquisa_binaria(id);
                if(end_atualiza!=-1){
                Conta cRead = new Conta();
                arq.seek(end_atualiza);
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                cRead.fromByteArray(ba);
    
                        while(teste==false){
                            System.out.println("Escreva o seu nome: ");
                            sc.nextLine();
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
                        arq.seek(end_atualiza);
                        arq.writeInt(ba.length);
                        arq.write(ba);
                        }
                        else{
                        arq.seek(end_atualiza+4); //pula o tamanho da instancia que esta gravada no arquivo
                        arq.writeBoolean(true);

                        arq.seek(arq.length());
                        long pt = arq.getFilePointer();

                        arq.writeInt(ba.length);
                        arq.write(ba);

                        atualizaEnderecoIndice(cRead.idConta, pt);
                        }
                }
                else System.out.println("Erro: O ID informado nao foi encontrado");
        } catch(IOException e){
            System.out.println("Erro: Nao foi possível ler o arquivo!");
        }
    }
    /*
    Método que procura o ultimo de id salvo no arquivo para determinar quantos registros estão salvos nele
    */
    public int numContas(){
        int numContas=0;
        try{
            arq.seek(0);
            numContas = arq.readInt();
        }catch(Exception e){}
        return numContas;
    }
    /*
    Método que procura le todos os dados presentes no arquivo de indices
    */
    public Conta[] lerTodosDados(){
        int cont=0, tam;
        byte[] ba;
        tam = numContas();
        tam++;
        Conta[] cRead = new Conta[tam];
        try{
            arqIndice.seek(0);
            while (arqIndice.getFilePointer()<arqIndice.length()){ 
                cRead[cont] = new Conta();
                ba = new byte[25];
                arqIndice.read(ba);
                cRead[cont].fromByteArrayIndice(ba);
                cont++;
            }
        }catch(Exception e){}
        return cRead;
    }
    /*
    Método que realiza uma pesquisa binaria nos dados do arquivo de indice
    Tomando como base para a pesquisa os dados salvos no vetor de contas
    */
    public long pesquisa_binaria(int pesquisa_id){
        int cont = numContas();
        cont++; // devemos somar 1 pra criar o vetor com posições suficientes para caber todos os registros do arq indice
        Conta[] contas = new Conta[cont];
        contas = lerTodosDados();

        int beggining = 0, end = cont-1;
            while (beggining <= end) {
                int middle = beggining + (end - beggining) / 2;
                
                if (pesquisa_id==contas[middle].idConta) //achou 
                    return contas[middle].end; // retorna endereço da pesquisa no arquivo de dados (se a pesquisa estiver presente nele)
                
                else if (pesquisa_id>contas[middle].idConta) // Se a string é maior, ignora a metade da esquerda
                    beggining = middle + 1;

                else // Se a string é menor é menor, ignora a metade da direita
                    end = middle - 1;
            }
        return -1; // else return -1 
    }
    /*
    Método que atualiza no arquivo de índice o endereço do registro no arquivo 
    de dados
    */
    public void atualizaEnderecoIndice(int pesquisa_id, long ponteiro){
        Conta cRead;
        byte[] ba;
        long pt;
        try{
            arqIndice.seek(0);
            while(arqIndice.getFilePointer()<arqIndice.length()){
                pt = arqIndice.getFilePointer();
                cRead = new Conta();
                ba = new byte[25];
                arqIndice.read(ba);
                cRead.fromByteArrayIndice(ba);
                if(cRead.idConta==pesquisa_id){
                    arqIndice.seek(pt);
                    arqIndice.write(cRead.toByteArrayIndice(ponteiro));
                }
            }
        }catch(Exception e){}
    }
    /*
    Método que realiza uma pesquisa binaria nos dados do arquivo de indice
    Tomando como base para a pesquisa os dados salvos no vetor de contas
    */
    public long pesquisa_binariaString(String pesquisa_string){
        int cont = numContas();
        Conta[] contas = new Conta[cont];
        contas = lerTodosDados();
		// Returns index da pesquisa no array (se a pesquisa estiver presente nela)
        int beggining = 0, end = cont-1;
            while (beggining <= end) {
                int middle = beggining + (end - beggining) / 2;

                if (pesquisa_string.compareTo(contas[middle].cpf)==0) //achou 
                    return contas[middle].end;
                
                if (pesquisa_string.compareTo(contas[middle].cpf)>0) // Se a string é maior, ignore left half
                    beggining = middle + 1;

                else // Se a string é menor é menor, ignore right half
                    end = middle - 1;
            }
        return -1; // else return -1 
    }
}