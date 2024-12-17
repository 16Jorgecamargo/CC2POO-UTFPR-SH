package emp.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class Pagamento {
    private int id;
    private Emprestimo emprestimo;
    private double valor;
    private double multa;
    private double juros;
    private LocalDate dataPagamento;
    private String formaPagamento;
    private StatusPagamento status;
    private String comprovante;

    public enum StatusPagamento {
        PENDENTE("Pendente"),
        PROCESSANDO("Em Processamento"),
        CONFIRMADO("Confirmado"),
        CANCELADO("Cancelado");

        private String descricao;

        StatusPagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum FormaPagamento {
        PIX("PIX"),
        TRANSFERENCIA("Transferência"),
        DINHEIRO("Dinheiro");

        private String descricao;

        FormaPagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public Pagamento(Emprestimo emprestimo, double valor, String formaPagamento) {
        this.emprestimo = emprestimo;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.dataPagamento = LocalDate.now();
        this.status = StatusPagamento.PENDENTE;
        calcularJurosMulta();
    }

    private void calcularJurosMulta() {
        if (emprestimo.getDataVencimento().isBefore(LocalDate.now())) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataVencimento(), LocalDate.now());
            this.multa = valor * 0.02; // 2% de multa
            this.juros = valor * (0.001 * diasAtraso); // 0.1% ao dia
        }
    }

    public boolean confirmar() {
        if (this.status != StatusPagamento.CANCELADO) {
            this.status = StatusPagamento.CONFIRMADO;
            return true;
        }
        return false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Emprestimo getEmprestimo() { return emprestimo; }
    public void setEmprestimo(Emprestimo emprestimo) { this.emprestimo = emprestimo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public double getMulta() { return multa; }
    public void setMulta(double multa) { this.multa = multa; }

    public double getJuros() { return juros; }
    public void setJuros(double juros) { this.juros = juros; }

    public double getTotal() { return valor + multa + juros; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public String getComprovante() { return comprovante; }
    public void setComprovante(String comprovante) { this.comprovante = comprovante; }

    @Override
    public String toString() {
        return String.format("[%s] R$ %.2f - %s (%s)", 
            dataPagamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            getTotal(), 
            formaPagamento,
            status.getDescricao());
    }
}