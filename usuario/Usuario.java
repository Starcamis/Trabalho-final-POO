package usuario;

import java.io.Serializable;
import java.util.Objects;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorId = 0;
    
    private final int id;
    private String nome;
    private String email;
    private String senha;
    private boolean ativo;
    
    public Usuario(String nome, String email, String senha) {
        this.id = ++contadorId;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public static int getContadorId() { return contadorId; }
    public static void resetContadorId() { contadorId = 0; }
    
    public abstract String getTipo();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return nome + " (" + getTipo() + ")";
    }
}