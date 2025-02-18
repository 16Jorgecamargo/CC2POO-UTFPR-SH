package emp.dao;

import emp.config.DatabaseConfig;
import emp.models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar operações de persistência para clientes.
 * 
 * Realiza inserção, atualização, busca e exclusão de clientes. Para operações de inserção e exclusão, 
 * esta classe também lida com a tabela de usuários, visto que os clientes estão relacionados a um usuário.
 * 
 * Tabelas utilizadas:
 * - usuários: para salvar nome, email, senha, tipo e status.
 * - clientes: para salvar dados específicos do cliente (CPF, telefone, endereço, score e limite).
 */
public class ClienteDAO {

    /**
     * Insere um novo cliente no banco de dados, realizando a inserção na tabela de usuários e em seguida
     * na tabela de clientes. Utiliza transação para garantir integridade.
     *
     * @param cliente Objeto Cliente a ser inserido.
     * @throws SQLException Caso ocorra erro durante a operação.
     */
    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, tipo_usuario, ativo) VALUES (?, ?, ?, 'CLIENTE', true)";
        String sqlCliente = "INSERT INTO clientes (usuario_id, cpf, telefone, endereco, score_credito, limite_credito) VALUES (?, ?, ?, ?, 500, 5000.00)";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Inserir usuário
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, cliente.getNome());
                    stmt.setString(2, cliente.getEmail());
                    stmt.setString(3, "senha123"); // Senha padrão inicial
                    stmt.executeUpdate();
                    
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        int usuarioId = rs.getInt(1);
                        
                        // Inserir cliente
                        try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                            stmtCliente.setInt(1, usuarioId);
                            stmtCliente.setString(2, cliente.getCpf());
                            stmtCliente.setString(3, cliente.getTelefone());
                            stmtCliente.setString(4, cliente.getEndereco());
                            stmtCliente.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Atualiza os dados de um cliente existente no banco de dados. 
     * Realiza atualização de informações na tabela de usuários e, logo após, na tabela de clientes.
     *
     * @param cliente Objeto Cliente com os dados atualizados.
     * @throws SQLException Caso ocorra erro durante a operação.
     */
    public void atualizar(Cliente cliente) throws SQLException {
        String sqlUsuario = "UPDATE usuarios SET nome = ?, email = ?, ativo = ? WHERE id = (SELECT usuario_id FROM clientes WHERE cpf = ?)";
        String sqlCliente = "UPDATE clientes SET telefone = ?, endereco = ? WHERE cpf = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Atualiza dados do usuário
                try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {
                    stmtUsuario.setString(1, cliente.getNome());
                    stmtUsuario.setString(2, cliente.getEmail());
                    stmtUsuario.setBoolean(3, cliente.isAtivo());
                    stmtUsuario.setString(4, cliente.getCpf());
                    stmtUsuario.executeUpdate();
                }
                
                // Atualiza dados específicos do cliente
                try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                    stmtCliente.setString(1, cliente.getTelefone());
                    stmtCliente.setString(2, cliente.getEndereco());
                    stmtCliente.setString(3, cliente.getCpf());
                    stmtCliente.executeUpdate();
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    /**
     * Busca todos os clientes cadastrados no banco de dados.
     * Realiza uma junção entre as tabelas usuários e clientes para retornar os dados completos.
     *
     * @return Lista de objetos Cliente.
     * @throws SQLException Caso ocorra erro durante a consulta.
     */
    public List<Cliente> buscarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT u.nome, u.email, u.ativo, c.cpf, c.telefone, c.endereco, " +
                    "c.score_credito, c.limite_credito, u.data_criacao " +
                    "FROM usuarios u " +
                    "JOIN clientes c ON c.usuario_id = u.id " +
                    "WHERE u.tipo_usuario = 'CLIENTE'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // ...existing code to criar e popular o objeto Cliente...
                Cliente cliente = new Cliente(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email")
                );
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setAtivo(rs.getBoolean("ativo"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }
    
    /**
     * Busca um cliente específico pelo CPF.
     * Realiza uma consulta utilizando uma junção entre usuários e clientes.
     *
     * @param cpf CPF do cliente.
     * @return Objeto Cliente se encontrado; caso contrário, null.
     * @throws SQLException Caso ocorra erro durante a consulta.
     */
    public Cliente buscarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT u.nome, u.email, u.ativo, c.cpf, c.telefone, c.endereco, " +
                    "c.score_credito, c.limite_credito, u.data_criacao " +
                    "FROM usuarios u " +
                    "JOIN clientes c ON c.usuario_id = u.id " +
                    "WHERE c.cpf = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                    );
                    cliente.setEndereco(rs.getString("endereco"));
                    cliente.setAtivo(rs.getBoolean("ativo"));
                    return cliente;
                }
            }
        }
        return null;
    }
    
    /**
     * Exclui um cliente do banco de dados utilizando o CPF.
     * Efetua a exclusão nas tabelas clientes e usuários de forma transacional.
     *
     * @param cpf CPF do cliente a ser excluído.
     * @throws SQLException Caso ocorra erro durante a exclusão.
     */
    public void excluir(String cpf) throws SQLException {
        String sqlGetUserId = "SELECT usuario_id FROM clientes WHERE cpf = ?";
        String sqlDeleteCliente = "DELETE FROM clientes WHERE cpf = ?";
        String sqlDeleteUsuario = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int usuarioId;
                // Obter o ID do usuário associado ao cliente
                try (PreparedStatement stmtGetId = conn.prepareStatement(sqlGetUserId)) {
                    stmtGetId.setString(1, cpf);
                    ResultSet rs = stmtGetId.executeQuery();
                    if (!rs.next()) {
                        throw new SQLException("Cliente não encontrado");
                    }
                    usuarioId = rs.getInt("usuario_id");
                }
    
                // Excluir da tabela de clientes
                try (PreparedStatement stmtCliente = conn.prepareStatement(sqlDeleteCliente)) {
                    stmtCliente.setString(1, cpf);
                    stmtCliente.executeUpdate();
                }
    
                // Excluir da tabela de usuários
                try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlDeleteUsuario)) {
                    stmtUsuario.setInt(1, usuarioId);
                    stmtUsuario.executeUpdate();
                }
    
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}