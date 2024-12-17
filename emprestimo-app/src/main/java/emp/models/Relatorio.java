package emp.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Relatorio {
    public enum TipoRelatorio {
        POR_CLIENTE("Por Cliente"),
        POR_EMPRESTIMO("Por Empréstimo"), 
        FINANCEIRO("Financeiros Gerais");

        private String descricao;
        TipoRelatorio(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }

    public enum Periodo {
        MES_ATUAL("Mês Atual"),
        TRIMESTRE("Trimestre"),
        SEMESTRE("Semestre"),
        ANO("Ano"),
        PERSONALIZADO("Período Personalizado");

        private String descricao;
        Periodo(String descricao) { this.descricao = descricao; }
        public String getDescricao() { return descricao; }
    }

    private TipoRelatorio tipo;
    private Periodo periodo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Cliente cliente;
    private String status;
    private Double valorMinimo;
    private Double valorMaximo;
    private List<Emprestimo> emprestimos;
    private String anotacoes;

    public Relatorio(TipoRelatorio tipo, Periodo periodo) {
        this.tipo = tipo;
        this.periodo = periodo;
        this.emprestimos = new ArrayList<>();
        configurarPeriodo();
    }

    private void configurarPeriodo() {
        LocalDate hoje = LocalDate.now();
        switch (periodo) {
            case MES_ATUAL:
                dataInicio = hoje.withDayOfMonth(1);
                dataFim = hoje;
                break;
            case TRIMESTRE:
                dataInicio = hoje.minusMonths(3);
                dataFim = hoje;
                break;
            case SEMESTRE:
                dataInicio = hoje.minusMonths(6);
                dataFim = hoje;
                break;
            case ANO:
                dataInicio = hoje.minusYears(1);
                dataFim = hoje;
                break;
            case PERSONALIZADO:
                // Datas serão definidas externamente
                break;
        }
    }

    public void filtrarEmprestimos(List<Emprestimo> todosEmprestimos) {
        emprestimos = todosEmprestimos.stream()
            .filter(this::aplicarFiltros)
            .collect(Collectors.toList());
    }

    private boolean aplicarFiltros(Emprestimo emp) {
        boolean filtroData = emp.getDataContratacao().isAfter(dataInicio) && 
                           emp.getDataContratacao().isBefore(dataFim.plusDays(1));
        
        boolean filtroCliente = cliente == null || emp.getCliente().equals(cliente);
        boolean filtroStatus = status == null || emp.getStatus().equals(status);
        boolean filtroValor = (valorMinimo == null || emp.getValor() >= valorMinimo) &&
                            (valorMaximo == null || emp.getValor() <= valorMaximo);
        
        return filtroData && filtroCliente && filtroStatus && filtroValor;
    }

    // Métodos de Análise
    public double calcularTotalEmprestado() {
        return emprestimos.stream()
                .mapToDouble(Emprestimo::getValor)
                .sum();
    }

    public double calcularTotalRecebido() {
        return emprestimos.stream()
                .mapToDouble(e -> e.getValorParcela() * e.getParcelasPagas())
                .sum();
    }

    public Map<String, Long> contarPorStatus() {
        return emprestimos.stream()
                .collect(Collectors.groupingBy(
                    Emprestimo::getStatus,
                    Collectors.counting()
                ));
    }

    public Map<LocalDate, Double> calcularPagamentosPorDia() {
        return emprestimos.stream()
                .collect(Collectors.groupingBy(
                    Emprestimo::getDataContratacao,
                    Collectors.summingDouble(Emprestimo::getValorParcela)
                ));
    }

    // Getters e Setters
    public TipoRelatorio getTipo() { return tipo; }
    public void setTipo(TipoRelatorio tipo) { this.tipo = tipo; }

    public Periodo getPeriodo() { return periodo; }
    public void setPeriodo(Periodo periodo) { 
        this.periodo = periodo;
        configurarPeriodo();
    }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getValorMinimo() { return valorMinimo; }
    public void setValorMinimo(Double valorMinimo) { this.valorMinimo = valorMinimo; }

    public Double getValorMaximo() { return valorMaximo; }
    public void setValorMaximo(Double valorMaximo) { this.valorMaximo = valorMaximo; }

    public List<Emprestimo> getEmprestimos() { return emprestimos; }
    public void setEmprestimos(List<Emprestimo> emprestimos) { this.emprestimos = emprestimos; }

    public String getAnotacoes() { return anotacoes; }
    public void setAnotacoes(String anotacoes) { this.anotacoes = anotacoes; }
}