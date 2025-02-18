package emp.dao;

import emp.config.DatabaseConfig;
import emp.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * DAO responsável pelas operações de autenticação e atualização de acesso dos usuários na tabela "usuarios".
 */
public class UsuarioDAO {

    /**
     * Autentica o usuário consultando a tabela "usuarios" e atualiza o último acesso em caso de sucesso.
     *
     * @param email email do usuário.
     * @param senha senha do usuário.
     * @return objeto Usuario se a autenticação for bem-sucedida; caso contrário, retorna null.
     * @throws SQLException se ocorrer erro durante a consulta.
     */
    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT id, nome, email, tipo_usuario FROM usuarios WHERE email = ? AND senha = ? AND ativo = true";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                        rs.getString("id"),
                        rs.getString("email"),
                        senha
                    );
                    atualizarUltimoAcesso(rs.getString("id"), conn);
                    return usuario;
                }
            }
        }
        return null;
    }
    
    /**
     * Atualiza o campo "ultimo_acesso" na tabela "usuarios" para o usuário especificado.
     *
     * @param usuarioId identificador do usuário.
     * @param conn conexão ativa com o banco de dados.
     * @throws SQLException se ocorrer erro durante a atualização.
     */
    private void atualizarUltimoAcesso(String usuarioId, Connection conn) throws SQLException {
        String sql = "UPDATE usuarios SET ultimo_acesso = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, LocalDateTime.now());
            stmt.setString(2, usuarioId);
            stmt.executeUpdate();
        }
    }
}