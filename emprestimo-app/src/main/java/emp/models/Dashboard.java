package emp.models;

public class Dashboard {
    private double totalEmprestado;
    private double saldoDevedor;
    private int emprestimosAtivos;
    private double pagamentosMes;
    
    public Dashboard() {
        this.totalEmprestado = 0.0;
        this.saldoDevedor = 0.0;
        this.emprestimosAtivos = 0;
        this.pagamentosMes = 0.0;
    }
    
    public double getTotalEmprestado() {
        return totalEmprestado;
    }
    
    public double getSaldoDevedor() {
        return saldoDevedor;
    }
    
    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }
    
    public double getPagamentosMes() {
        return pagamentosMes;
    }
}