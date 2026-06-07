package Autenticacao;

import usuario.Usuario;
import java.util.List;

public class Login {
    private String email;
    private String senha;
    private boolean autenticado;
    
    public Login(String email, String senha) {
        this.email = email;
        this.senha = senha;
        this.autenticado = false;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean isAutenticado() { return autenticado; }
    public void setAutenticado(boolean autenticado) { this.autenticado = autenticado; }
    
    public boolean autenticar(List<? extends Usuario> usuarios) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha) && u.isAtivo()) {
                autenticado = true;
                return true;
            }
        }
        autenticado = false;
        return false;
    }
}