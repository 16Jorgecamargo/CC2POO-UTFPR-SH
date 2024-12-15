package emp.models;

public class Usuario {
    private String nomeUsuario;
    private String email;
    private String senha;
    
    public Usuario(String nomeUsuario, String email, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public String getEmail() {
        return email;
    }
    
    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }
}