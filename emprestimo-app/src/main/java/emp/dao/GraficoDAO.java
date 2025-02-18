package emp.dao;

import emp.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * DAO responsável pelas consultas que geram dados gráficos a partir das tabelas "pagamentos" e "emprestimos".
 */
public class GraficoDAO {

    /**
     * Recupera a soma dos valores de pagamentos por hora do dia atual.
     *
     * Realiza uma consulta na tabela "pagamentos" agrupando os registros por hora (campo data_pagamento).
     *
     * @return lista com o total de pagamentos para cada hora (0 a 23).
     * @throws SQLException se ocorrer erro durante a consulta.
     */
    public List<Double> getPagamentosPorHora() throws SQLException {
        List<Double> pagamentosPorHora = new ArrayList<>();
        String sql = "SELECT HOUR(data_pagamento) AS hora, COALESCE(SUM(valor), 0) AS total " +
                     "FROM pagamentos " +
                     "WHERE DATE(data_pagamento) = CURDATE() " +
                     "GROUP BY HOUR(data_pagamento) " +
                     "ORDER BY hora";
        for (int i = 0; i < 24; i++) {
            pagamentosPorHora.add(0.0);
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int hora = rs.getInt("hora");
                double total = rs.getDouble("total");
                pagamentosPorHora.set(hora, total);
            }
        }
        return pagamentosPorHora;
    }

    /**
     * Recupera a soma dos valores de pagamentos por mês do ano atual.
     *
     * Realiza uma consulta na tabela "pagamentos" agrupando os registros por mês (campo data_pagamento).
     *
     * @return lista com o total de pagamentos para cada mês (índice 0 a 11).
     * @throws SQLException se ocorrer erro durante a consulta.
     */
    public List<Double> getPagamentosPorMes() throws SQLException {
        List<Double> pagamentosPorMes = new ArrayList<>();
        String sql = "SELECT MONTH(data_pagamento) AS mes, COALESCE(SUM(valor), 0) AS total " +
                     "FROM pagamentos " +
                     "WHERE YEAR(data_pagamento) = YEAR(CURDATE()) " +
                     "GROUP BY MONTH(data_pagamento) " +
                     "ORDER BY mes";
        for (int i = 0; i < 12; i++) {
            pagamentosPorMes.add(0.0);
        }
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int mes = rs.getInt("mes") - 1;
                double total = rs.getDouble("total");
                pagamentosPorMes.set(mes, total);
            }
        }
        return pagamentosPorMes;
    }
    
    /**
     * Recupera a contagem de empréstimos agrupados por status.
     *
     * Realiza uma consulta na tabela "emprestimos" agrupando os registros pelo campo status.
     *
     * @return mapa onde a chave é o status e o valor é a contagem de registros com esse status.
     * @throws SQLException se ocorrer erro durante a consulta.
     */
    public Map<String, Integer> getContagemStatusEmprestimos() throws SQLException {
        Map<String, Integer> statusContagem = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as contagem FROM emprestimos GROUP BY status";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                statusContagem.put(rs.getString("status"), rs.getInt("contagem"));
            }
        }
        return statusContagem;
    }
}