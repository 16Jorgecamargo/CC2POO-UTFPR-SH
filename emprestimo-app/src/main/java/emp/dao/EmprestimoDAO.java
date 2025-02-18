package emp.dao;

import emp.config.DatabaseConfig;
import emp.models.Cliente;
import emp.models.Emprestimo;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar operações de persistência dos empréstimos.
 * 
 * Opera com a tabela "emprestimos" e suas relações com clientes,
 * possibilitando inserir, aprovar, atualizar e realizar buscas por empréstimos.
 */
public class EmprestimoDAO {

    /**
     * Insere um novo empréstimo na tabela de emprestimos.
     * Neste método, é realizada uma inserção que utiliza uma subconsulta para obter o usuário
     * associado ao cliente, baseado no CPF fornecido.
     *
     * @param emprestimo Objeto Emprestimo a ser inserido.
     * @throws SQLException Caso não seja possível inserir o empréstimo ou obter o ID gerado.
     */
    public void inserir(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO emprestimos (usuario_id, valor_principal, taxa_juros, tipo_juros, prazo, valor_total, data_vencimento) SELECT c.usuario_id, ?, ?, ?, ?, ?, ? FROM clientes c WHERE c.cpf = ?";
    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            stmt.setDouble(1, emprestimo.getValor());
            stmt.setDouble(2, emprestimo.getTaxa());
            stmt.setString(3, emprestimo.getJurosSimples() ? "SIMPLES" : "COMPOSTO");
            stmt.setInt(4, emprestimo.getPrazo());
            stmt.setDouble(5, emprestimo.getTotalAPagar());
            stmt.setDate(6, java.sql.Date.valueOf(emprestimo.getDataVencimento()));
            stmt.setString(7, emprestimo.getCliente().getCpf());
    
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar empréstimo, nenhuma linha afetada.");
            }
    
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprestimo.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao criar empréstimo, nenhum ID obtido.");
                }
            }
        }
    }

    /**
     * Aprova um empréstimo atualizando seu status para 'APROVADO' e definindo a data de aprovação.
     *
     * @param emprestimoId ID do empréstimo a ser aprovado.
     * @throws SQLException Caso ocorra erro ao executar a atualização.
     */
    public void aprovar(int emprestimoId) throws SQLException {
        String sql = "UPDATE emprestimos SET status = 'APROVADO', data_aprovacao = NOW() " +
                "WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimoId);
            stmt.executeUpdate();
        }
    }

    /**
     * Atualiza os dados de um empréstimo, como parcelas pagas, multa e status.
     *
     * @param emprestimo Objeto Emprestimo com dados atualizados.
     * @throws SQLException Caso ocorra erro na atualização no banco de dados.
     */
    public void atualizarEmprestimo(Emprestimo emprestimo) throws SQLException {
        // Certifique-se de que a tabela "emprestimos" possua a coluna "multa"
        String sql = "UPDATE emprestimos SET parcelas_pagas = ?, multa = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getParcelasPagas());
            stmt.setDouble(2, emprestimo.getMulta());
            stmt.setString(3, emprestimo.getStatus());
            stmt.setInt(4, emprestimo.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Busca empréstimos associados a um cliente específico.
     *
     * @param cpf CPF do cliente.
     * @return Lista de objetos Emprestimo vinculados ao CPF informado.
     * @throws SQLException Caso ocorra erro na consulta.
     */
    public List<Emprestimo> buscarPorCliente(String cpf) throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.*, c.cpf, u.nome FROM emprestimos e " +
                "INNER JOIN clientes c ON e.usuario_id = c.usuario_id " +
                "INNER JOIN usuarios u ON u.id = c.usuario_id " +
                "WHERE c.cpf = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            "", // telefone não necessário aqui
                            ""  // email não necessário aqui
                    );
                    Emprestimo emprestimo = new Emprestimo(
                            rs.getDouble("valor_principal"),
                            rs.getInt("prazo"),
                            rs.getDouble("taxa_juros"),
                            cliente);
                    emprestimo.setId(rs.getInt("id"));
                    emprestimo.setStatus(rs.getString("status"));
                    emprestimo.setDataVencimento(rs.getDate("data_vencimento").toLocalDate());

                    emprestimos.add(emprestimo);
                }
            }
        }
        return emprestimos;
    }

    /**
     * Busca todos os empréstimos registrados no banco de dados.
     *
     * @return Lista de todos os objetos Emprestimo.
     * @throws SQLException Caso ocorra erro durante a consulta.
     */
    public List<Emprestimo> buscarTodos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT e.*, u.nome AS cliente_nome, c.cpf, c.telefone, u.email " +
                     "FROM emprestimos e " +
                     "INNER JOIN clientes c ON e.usuario_id = c.usuario_id " +
                     "INNER JOIN usuarios u ON u.id = c.usuario_id";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                double valorPrincipal = rs.getDouble("valor_principal");
                double taxaJuros = rs.getDouble("taxa_juros");
                int prazo = rs.getInt("prazo");
                LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();
                
                String clienteNome = rs.getString("cliente_nome");
                String cpf = rs.getString("cpf");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                
                Cliente cliente = new Cliente(clienteNome, cpf, telefone, email);
                Emprestimo emprestimo = new Emprestimo(valorPrincipal, prazo, taxaJuros, cliente);
                emprestimo.setId(id);
                emprestimo.setDataVencimento(dataVencimento);
                
                // Atualiza os campos atualizados no banco
                emprestimo.setParcelasPagas(rs.getInt("parcelas_pagas"));
                emprestimo.setStatus(rs.getString("status"));
                emprestimo.setMulta(rs.getDouble("multa")); // Certifique-se de que este setter exista
                
                lista.add(emprestimo);
            }
        }
        return lista;
    }
}