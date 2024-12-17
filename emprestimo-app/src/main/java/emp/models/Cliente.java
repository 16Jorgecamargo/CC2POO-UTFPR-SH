package emp.models;

import java.time.LocalDate;

public class Cliente {
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
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
        this.endereco = "";
        this.totalEmprestado = 0.0;
        this.totalPago = 0.0;
        this.parcelasPagas = 0;
        this.parcelasPendentes = 0;
        this.parcelasAtrasadas = 0;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getTotalEmprestado() {
        return totalEmprestado;
    }

    public void setTotalEmprestado(double valor) {
        this.totalEmprestado = valor;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double valor) {
        this.totalPago = valor;
    }

    public int getParcelasPagas() {
        return parcelasPagas;
    }

    public void setParcelasPagas(int parcelas) {
        this.parcelasPagas = parcelas;
    }

    public int getParcelasPendentes() {
        return parcelasPendentes;
    }

    public void setParcelasPendentes(int parcelas) {
        this.parcelasPendentes = parcelas;
    }

    public int getParcelasAtrasadas() {
        return parcelasAtrasadas;
    }

    public void setParcelasAtrasadas(int parcelas) {
        this.parcelasAtrasadas = parcelas;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }
}