/**
 * TP I - Algoritmos e Estruturas de Dados III
 * @author - Camila Lacerda Grandini & Joana Woldaysnky
 * 2022 - 3o. Semestre
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class Conta {
  protected int idConta;
  protected String nomePessoa;
  protected String cpf;
  protected String cidade;
  protected int transferenciasRealizadas;
  protected float saldoConta;
  protected boolean lapide;

  public Conta(int id, String nome, String cpf, String cidade) {
    this.idConta = id;
    this.nomePessoa = nome;
    this.cpf = cpf;
    this.cidade = cidade;
    this.transferenciasRealizadas = 0;
    this.saldoConta = 0;
    this.lapide = false;
  }

  public Conta() {
    this.idConta = -1;
    this.nomePessoa = "";
    this.cpf = "";
    this.cidade = "";
    this.transferenciasRealizadas = 0;
    this.saldoConta = 0;
    this.lapide=false;
  }

  public String toString() {
    return "\nID....: " + this.idConta + "\nNome Pessoa.: " + this.nomePessoa + "\nCPF.: " + this.cpf + "\nCidade.: " + this.cidade + "\nTransferências Realizadas.: " + this.transferenciasRealizadas + "\nSaldo.: " + this.saldoConta;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeBoolean(lapide);
    dos.writeInt(idConta);
    dos.writeUTF(nomePessoa);
    dos.writeUTF(cpf);
    dos.writeUTF(cidade);
    dos.writeInt(transferenciasRealizadas);
    dos.writeFloat(saldoConta);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    idConta = dis.readInt();
    nomePessoa = dis.readUTF();
    cpf = dis.readUTF();
    cidade = dis.readUTF();
    transferenciasRealizadas = dis.readInt();
    saldoConta = dis.readFloat();
  }
}