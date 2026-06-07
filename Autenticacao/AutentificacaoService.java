package Autenticacao;

import usuario.Usuario;
import exceptions.LoginInvalidoException;
import java.util.List;

public class AutenticacaoService {
    private static AutenticacaoService instance;
    private Usuario usuarioLogado;
    
    private AutenticacaoService() {}
    
    public static AutenticacaoService getInstance() {
        if (instance == null) {
            instance = new AutenticacaoService();
        }
        return instance;
    }
    
    public Usuario login(List<? extends Usuario> usuarios, String email, String senha) 
            throws LoginInvalidoException {
        
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.getSenha().equals(senha) && u.isAtivo()) {
                usuarioLogado = u;
                return u;
            }
        }
        throw new LoginInvalidoException("Email ou senha inválidos, ou usuário inativo!");
    }
    
    public void logout() {
        usuarioLogado = null;
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    public boolean isLogado() {
        return usuarioLogado != null;
    }
}