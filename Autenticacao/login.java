package Autenticacao;

import usuario.Usuario;

public class login {
    private String email;
    private String senha;
    private boolean autenticado;
    
    public login(String email, String senha) {
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
}