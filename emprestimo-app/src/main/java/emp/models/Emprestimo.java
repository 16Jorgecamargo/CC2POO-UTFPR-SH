package emp.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um empréstimo contendo informações financeiras e do cliente.
 * Possui métodos para calcular o valor das parcelas, total a pagar, juros e encargos por atraso.
 */
public class Emprestimo {
    // Atributos do empréstimo
    private int id;
    private double valor;
    private double multa;
    private int prazo;
    private double taxaJuros;
    private boolean jurosSimples;
    private Cliente cliente;
    private int parcelasPagas;
    private double valorParcela;
    private LocalDate dataContratacao;
    private String status;
    private LocalDate dataSolicitacao;
    private LocalDate dataAprovacao;
    private LocalDate dataVencimento;

    /**
     * Cria um novo empréstimo com base nos valores informados. Calcula o valor da parcela usando a fórmula de financiamento.
     *
     * @param valor valor do empréstimo.
     * @param prazo número de parcelas.
     * @param taxa taxa de juros aplicada.
     * @param cliente cliente solicitante do empréstimo.
     */
    public Emprestimo(double valor, int prazo, double taxa, Cliente cliente) {
        this.valor = valor;
        this.prazo = prazo;
        this.taxaJuros = taxa;
        this.cliente = cliente;
        this.dataContratacao = LocalDate.now();
        this.status = "Em análise";
        calcularParcela();
    }
    
    /**
     * Calcula o valor da parcela utilizando a fórmula de financiamento.
     */
    private void calcularParcela() {
        this.valorParcela = valor * (taxaJuros * Math.pow(1 + taxaJuros, prazo)) /
                (Math.pow(1 + taxaJuros, prazo) - 1);
    }
    
    /**
     * Retorna o valor do empréstimo.
     *
     * @return valor.
     */
    public double getValor() {
        return valor;
    }
    
    /**
     * Retorna o prazo do empréstimo.
     *
     * @return prazo.
     */
    public int getPrazo() {
        return prazo;
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
     * Retorna a taxa de juros aplicada ao empréstimo.
     *
     * @return taxa de juros.
     */
    public double getTaxa() {
        return taxaJuros;
    }

    /**
     * Indica se o cálculo utiliza juros simples.
     *
     * @return true se juros simples, false caso contrário.
     */
    public boolean getJurosSimples() {
        return jurosSimples;
    }
    
    /**
     * Define se o empréstimo utiliza juros simples.
     *
     * @param jurosSimples true para juros simples, false para composto.
     */
    public void setJurosSimples(boolean jurosSimples) {
        this.jurosSimples = jurosSimples;
    }

    /**
     * Atualiza a quantidade de parcelas pagas.
     *
     * @param parcelasPagas número de parcelas pagas.
     */
    public void setParcelasPagas(int parcelasPagas) {
        this.parcelasPagas = parcelasPagas;
    }

    /**
     * Retorna o valor da parcela calculado.
     *
     * @return valor da parcela.
     */
    public double getValorParcela() {
        return valorParcela;
    }

    /**
     * Retorna a data de contratação do empréstimo.
     *
     * @return data de contratação.
     */
    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    /**
     * Retorna o status atual do empréstimo.
     *
     * @return status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status do empréstimo.
     *
     * @param status novo status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retorna o cliente associado ao empréstimo.
     *
     * @return objeto Cliente.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Calcula o total a pagar, multiplicando o valor da parcela pelo prazo.
     *
     * @return total a pagar.
     */
    public double getTotalAPagar() {
        return valorParcela * prazo;
    }

    /**
     * Calcula o total de juros aplicados ao empréstimo.
     *
     * @return total de juros.
     */
    public double getTotalJuros() {
        return getTotalAPagar() - valor;
    }

    /**
     * Define a data de aprovação do empréstimo.
     *
     * @param dataAprovacao data de aprovação.
     */
    public void setDataAprovacao(LocalDate dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    /**
     * Retorna a data de vencimento do empréstimo.
     *
     * @return data de vencimento.
     */
    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    /**
     * Define o valor da multa aplicada ao empréstimo.
     *
     * @param multa valor da multa.
     */
    public void setMulta(double multa) {
        this.multa = multa;
    }

    /**
     * Retorna o valor da multa.
     *
     * @return multa.
     */
    public double getMulta() {
        return multa;
    }
        
    /**
     * Define a data de vencimento do empréstimo.
     *
     * @param dataVencimento nova data de vencimento.
     */
    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    
    /**
     * Calcula os juros decorrentes de atraso no pagamento.
     * Se a data atual for posterior à data de vencimento, calcula 2% de juros ao dia.
     *
     * @return valor dos juros por atraso.
     */
    public double calcularJurosAtraso() {
        if (LocalDate.now().isAfter(getDataVencimento())) {
            long diasAtraso = ChronoUnit.DAYS.between(getDataVencimento(), LocalDate.now());
            return getValorParcela() * 0.02 * diasAtraso;
        }
        return 0.0;
    }

    /**
     * Retorna o identificador do empréstimo.
     *
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do empréstimo.
     *
     * @param id novo id.
     */
    public void setId(int id) {
        this.id = id;
    }
}