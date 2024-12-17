package emp.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private int id;
    private double valor;
    private int prazo;
    private double taxaJuros;
    private Cliente cliente;
    private int parcelasPagas = 0;
    private double valorParcela;
    private LocalDate dataContratacao;
    private String status;
    private LocalDate dataSolicitacao;
    private LocalDate dataAprovacao;
    private LocalDate dataVencimento;

    public Emprestimo(double valor, int prazo, double taxa, Cliente cliente) {
        this.valor = valor;
        this.prazo = prazo;
        this.taxaJuros = taxa;
        this.cliente = cliente;
        this.dataContratacao = LocalDate.now();
        this.status = "Em análise";
        calcularParcela();
    }

    // Getters e Setters
    public double getValor() {
        return valor;
    }
    
    private void calcularParcela() {
        this.valorParcela = valor * (taxaJuros * Math.pow(1 + taxaJuros, prazo)) /
                (Math.pow(1 + taxaJuros, prazo) - 1);
    }

    public int getPrazo() {
        return prazo;
    }

    public int getParcelasPagas() {
        return parcelasPagas;
    }

    public double getTaxa() {
        return taxaJuros;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public double getTotalAPagar() {
        return valorParcela * prazo;
    }

    public double getTotalJuros() {
        return getTotalAPagar() - valor;
    }

    public void setDataAprovacao(LocalDate dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public double calcularJurosAtraso() {
        if (LocalDate.now().isAfter(getDataVencimento())) {
            long diasAtraso = ChronoUnit.DAYS.between(getDataVencimento(), LocalDate.now());
            return getValorParcela() * 0.02 * diasAtraso; // 2% ao dia de juros
        }
        return 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}