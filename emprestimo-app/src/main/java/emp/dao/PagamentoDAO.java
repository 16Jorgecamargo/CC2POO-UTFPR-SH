package emp.dao;

import emp.config.DatabaseConfig;
import emp.models.Pagamento;
import java.sql.*;

/**
 * DAO responsável pelas operações na tabela "pagamentos".
 */
public class PagamentoDAO {

    /**
     * Insere um novo pagamento na tabela "pagamentos".
     *
     * @param pagamento objeto Pagamento contendo os dados para inserção.
     * @throws SQLException se ocorrer erro durante a operação de inserção.
     */
    public void inserir(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamentos (emprestimo_id, valor, forma_pagamento, status, data_pagamento) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, pagamento.getEmprestimo().getId());
            stmt.setDouble(2, pagamento.getValor());
            stmt.setString(3, pagamento.getFormaPagamento());
            stmt.setString(4, pagamento.getStatus().getDescricao());
            stmt.setDate(5, Date.valueOf(pagamento.getDataPagamento()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir pagamento, nenhuma linha afetada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pagamento.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao inserir pagamento, nenhum ID obtido.");
                }
            }
        }
    }
    
    /**
     * Atualiza o status de um pagamento na tabela "pagamentos".
     *
     * @param pagamentoId identificador do pagamento.
     * @param status novo status a ser definido.
     * @throws SQLException se ocorrer erro durante a atualização.
     */
    public void atualizarStatus(int pagamentoId, Pagamento.StatusPagamento status) throws SQLException {
        String sql = "UPDATE pagamentos SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.getDescricao());
            stmt.setInt(2, pagamentoId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Busca um pagamento na tabela "pagamentos" com base no ID.
     *
     * @param id identificador do pagamento.
     * @return objeto Pagamento se encontrado; caso contrário, retorna null.
     * @throws SQLException se ocorrer erro durante a consulta.
     */
    public Pagamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pagamentos WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pagamento pagamento = new Pagamento(null, rs.getDouble("valor"), rs.getString("forma_pagamento"));
                    pagamento.setId(rs.getInt("id"));
                    pagamento.setMulta(0.0);
                    pagamento.setJuros(rs.getDouble("juros"));
                    String statusStr = rs.getString("status");
                    for (Pagamento.StatusPagamento s : Pagamento.StatusPagamento.values()) {
                        if (s.getDescricao().equals(statusStr)) {
                            pagamento.setStatus(s);
                            break;
                        }
                    }
                    pagamento.setDataPagamento(rs.getDate("data_pagamento").toLocalDate());
                    pagamento.setComprovante(rs.getString("comprovante"));
                    return pagamento;
                }
            }
        }
        return null;
    }
    
    /**
     * Deleta um pagamento da tabela "pagamentos" com base no ID.
     *
     * @param pagamentoId identificador do pagamento a ser removido.
     * @throws SQLException se ocorrer erro durante a deleção.
     */
    public void deletar(int pagamentoId) throws SQLException {
        String sql = "DELETE FROM pagamentos WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pagamentoId);
            stmt.executeUpdate();
        }
    }
}