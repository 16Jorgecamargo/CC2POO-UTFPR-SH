package emp.models;

import java.time.LocalDate;

/**
 * Representa um cliente com informações pessoais e financeiras.
 * Contém dados cadastrais, status de atividade e informações sobre empréstimos realizados.
 */
public class Cliente {
    private int id;
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

    /**
     * Cria um novo cliente com os dados básicos.
     *
     * @param nome nome do cliente.
     * @param cpf CPF do cliente.
     * @param telefone telefone do cliente.
     * @param email email do cliente.
     */
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

    /**
     * Retorna o identificador do cliente.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do cliente.
     *
     * @param id novo id.
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    /**
     * Retorna o nome do cliente.
     *
     * @return nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do cliente.
     *
     * @param nome novo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF do cliente.
     *
     * @return CPF.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do cliente.
     *
     * @param cpf novo CPF.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna o telefone do cliente.
     *
     * @return telefone.
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone do cliente.
     *
     * @param telefone novo telefone.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o email do cliente.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do cliente.
     *
     * @param email novo email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna o endereço do cliente.
     *
     * @return endereço.
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço do cliente.
     *
     * @param endereco novo endereço.
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Retorna o total emprestado ao cliente.
     *
     * @return total emprestado.
     */
    public double getTotalEmprestado() {
        return totalEmprestado;
    }

    /**
     * Define o total emprestado ao cliente.
     *
     * @param valor novo total emprestado.
     */
    public void setTotalEmprestado(double valor) {
        this.totalEmprestado = valor;
    }

    /**
     * Retorna o total pago pelo cliente.
     *
     * @return total pago.
     */
    public double getTotalPago() {
        return totalPago;
    }

    /**
     * Define o total pago pelo cliente.
     *
     * @param valor novo total pago.
     */
    public void setTotalPago(double valor) {
        this.totalPago = valor;
    }

    /**
     * Retorna a quantidade de parcelas pagas.
     *
     * @return parcelas pagas.
     */
    public int getParcelasPagas() {
        return parcelasPagas;
    }

    /**
     * Define a quantidade de parcelas pagas.
     *
     * @param parcelas número de parcelas pagas.
     */
    public void setParcelasPagas(int parcelas) {
        this.parcelasPagas = parcelas;
    }

    /**
     * Retorna a quantidade de parcelas pendentes.
     *
     * @return parcelas pendentes.
     */
    public int getParcelasPendentes() {
        return parcelasPendentes;
    }

    /**
     * Define a quantidade de parcelas pendentes.
     *
     * @param parcelas número de parcelas pendentes.
     */
    public void setParcelasPendentes(int parcelas) {
        this.parcelasPendentes = parcelas;
    }

    /**
     * Retorna a quantidade de parcelas atrasadas.
     *
     * @return parcelas atrasadas.
     */
    public int getParcelasAtrasadas() {
        return parcelasAtrasadas;
    }

    /**
     * Define a quantidade de parcelas atrasadas.
     *
     * @param parcelas número de parcelas atrasadas.
     */
    public void setParcelasAtrasadas(int parcelas) {
        this.parcelasAtrasadas = parcelas;
    }

    /**
     * Retorna o status de atividade do cliente.
     *
     * @return ativo.
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Define o status de atividade do cliente.
     *
     * @param ativo novo status de atividade.
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Retorna a data de cadastro do cliente.
     *
     * @return data de cadastro.
     */
    public LocalDate getDataCadastro() {
        return dataCadastro;
    }
}
