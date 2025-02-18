package emp.models;

/**
 * Representa os dados financeiros para o dashboard.
 * Agrega informações como total emprestado, saldo devedor, quantidade de empréstimos ativos e pagamentos do mês.
 */
public class Dashboard {
    private double totalEmprestado;
    private double saldoDevedor;
    private int emprestimosAtivos;
    private double pagamentosMes;
    
    /**
     * Cria um dashboard com valores iniciais zerados.
     */
    public Dashboard() {
        this.totalEmprestado = 0.0;
        this.saldoDevedor = 0.0;
        this.emprestimosAtivos = 0;
        this.pagamentosMes = 0.0;
    }
    
    /**
     * Retorna o total emprestado.
     *
     * @return total emprestado.
     */
    public double getTotalEmprestado() {
        return totalEmprestado;
    }
    
    /**
     * Define o total emprestado.
     *
     * @param totalEmprestado novo total emprestado.
     */
    public void setTotalEmprestado(double totalEmprestado) {
        this.totalEmprestado = totalEmprestado;
    }
    
    /**
     * Retorna o saldo devedor.
     *
     * @return saldo devedor.
     */
    public double getSaldoDevedor() {
        return saldoDevedor;
    }
    
    /**
     * Define o saldo devedor.
     *
     * @param saldoDevedor novo saldo devedor.
     */
    public void setSaldoDevedor(double saldoDevedor) {
        this.saldoDevedor = saldoDevedor;
    }
    
    /**
     * Retorna a quantidade de empréstimos ativos.
     *
     * @return número de empréstimos ativos.
     */
    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }
    
    /**
     * Define a quantidade de empréstimos ativos.
     *
     * @param emprestimosAtivos nova quantidade de empréstimos ativos.
     */
    public void setEmprestimosAtivos(int emprestimosAtivos) {
        this.emprestimosAtivos = emprestimosAtivos;
    }
    
    /**
     * Retorna o valor dos pagamentos realizados no mês.
     *
     * @return pagamentos do mês.
     */
    public double getPagamentosMes() {
        return pagamentosMes;
    }
    
    /**
     * Define o valor dos pagamentos do mês.
     *
     * @param pagamentosMes novo valor de pagamentos.
     */
    public void setPagamentosMes(double pagamentosMes) {
        this.pagamentosMes = pagamentosMes;
    }
}