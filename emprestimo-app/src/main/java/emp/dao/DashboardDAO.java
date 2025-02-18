package emp.dao;

import emp.config.DatabaseConfig;
import emp.models.Dashboard;
import java.sql.*;

/**
 * Classe responsável por consolidar os dados para o dashboard.
 * 
 * Executa uma consulta que agrega informações das tabelas de empréstimos e pagamentos,
 * permitindo exibir dados como total emprestado, saldo devedor, número de empréstimos ativos
 * e pagamentos do mês.
 */
public class DashboardDAO {
    /**
     * Obtém os dados consolidado para exibição no dashboard.
     *
     * Consultas realizadas:
     * - Soma dos valores principais de empréstimos ativos.
     * - Soma dos valores totais de empréstimos ativos (saldo devedor).
     * - Contagem de empréstimos ativos.
     * - Soma dos valores dos pagamentos realizados no mês atual.
     *
     * @return Objeto Dashboard com os dados atualizados.
     * @throws SQLException Caso ocorra erro durante a consulta.
     */
    public Dashboard getDashboardData() throws SQLException {
        Dashboard dashboard = new Dashboard();
        String sql = "SELECT " +
                     "    (SELECT COALESCE(SUM(valor_principal), 0) FROM emprestimos WHERE status = 'Ativo') AS totalEmprestado, " +
                     "    (SELECT COALESCE(SUM(valor_total), 0) FROM emprestimos WHERE status = 'Ativo') AS saldoDevedor, " +
                     "    (SELECT COUNT(*) FROM emprestimos WHERE status = 'Ativo') AS emprestimosAtivos, " +
                     "    (SELECT COALESCE(SUM(p.valor), 0) FROM pagamentos p " +
                     "       WHERE MONTH(p.data_pagamento) = MONTH(CURRENT_DATE()) " +
                     "         AND YEAR(p.data_pagamento) = YEAR(CURRENT_DATE())) AS pagamentosMes";
                     
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            if (rs.next()) {
                dashboard.setTotalEmprestado(rs.getDouble("totalEmprestado"));
                dashboard.setSaldoDevedor(rs.getDouble("saldoDevedor"));
                dashboard.setEmprestimosAtivos(rs.getInt("emprestimosAtivos"));
                dashboard.setPagamentosMes(rs.getDouble("pagamentosMes"));
            }
        }
        return dashboard;
    }
}