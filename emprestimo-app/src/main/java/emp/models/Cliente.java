package emp.models;

import java.time.LocalDate;

public class Cliente {
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private boolean ativo;
    private LocalDate dataCadastro;
    private double totalEmprestado;
    private double totalPago;
    private int parcelasPagas;
    private int parcelasPendentes;
    private int parcelasAtrasadas;
    
    public Cliente(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.ativo = true;
        this.dataCadastro = LocalDate.now();
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public LocalDate getDataCadastro() { return dataCadastro; }
}