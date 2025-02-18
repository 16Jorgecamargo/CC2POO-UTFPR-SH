package emp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe de configuração do banco de dados utilizando o HikariCP para gerenciamento de conexões.
 * <p>
 * Esta classe configura o pool de conexões para acesso a um banco de dados MySQL localizado em:
 * <br>"jdbc:mysql://localhost:3306/sistema_emprestimos"
 * <br>Utilizando as credenciais:
 * <br>Usuário: "root"
 * <br>Senha: "12345678"
 * <br>
 * O pool está configurado para suportar até 10 conexões simultâneas com auto-commit habilitado.
 * <p>
 * A configuração inclui otimizações de performance, como o cache de PreparedStatements.
 * Operações com tabelas do sistema de empréstimos (por exemplo, tabelas de empréstimos, clientes, etc.)
 * deverão ser executadas por outras partes da aplicação.
 */
public class DatabaseConfig {

    /**
     * Pool de conexões HikariCP que gerencia as conexões com o banco de dados.
     */
    private static HikariDataSource dataSource;
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/sistema_emprestimos");
        config.setUsername("root");
        config.setPassword("12345678");
        config.setMaximumPoolSize(10);
        config.setAutoCommit(true);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        dataSource = new HikariDataSource(config);
    }
    
    /**
     * Obtém uma conexão ativa do pool configurado para o banco de dados MySQL.
     *
     * @return {@link Connection} conexão ativa obtida do pool.
     * @throws SQLException Caso ocorra erro ao obter a conexão.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}