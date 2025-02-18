package emp.models;

import java.time.LocalDateTime;

/**
 * Representa um usuário do sistema.
 * Contém informações de autenticação e dados do último acesso.
 */
public class Usuario {
    private String id;
    private String nomeUsuario;
    private String email;
    private String senha;
    private String tipoUsuario;
    private LocalDateTime ultimoAcesso;
    private boolean ativo;
    
    /**
     * Construtor básico do usuário.
     *
     * @param id identificador do usuário.
     * @param email email de login.
     * @param senha senha para autenticação.
     */
    public Usuario(String id, String email, String senha) {
        this.id = id;
        this.email = email;
        this.senha = senha;
    }

    /**
     * Retorna o identificador do usuário.
     *
     * @return id do usuário.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome de usuário.
     *
     * @return nome de usuário.
     */
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    /**
     * Retorna o email do usuário.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Retorna o tipo do usuário (ex.: admin, comum).
     *
     * @return tipo de usuário.
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    /**
     * Define o tipo de usuário.
     *
     * @param tipoUsuario novo tipo.
     */
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    /**
     * Retorna a data e hora do último acesso.
     *
     * @return LocalDateTime do último acesso.
     */
    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }
    
    /**
     * Define a data e hora do último acesso.
     *
     * @param ultimoAcesso novo valor para o último acesso.
     */
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
    
    /**
     * Indica se o usuário está ativo.
     *
     * @return true se ativo; caso contrário, false.
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    /**
     * Autentica comparando a senha informada com a armazenada.
     *
     * @param senha senha a ser comparada.
     * @return true se as senhas corresponderem; caso contrário, false.
     */
    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }
}