package Extras;

import java.util.*;
import javax.swing.JOptionPane;

public class GerenciadorNotificacoes {
    private static GerenciadorNotificacoes instance;
    private Map<String, List<Notificacao>> notificacoesPorUsuario;
    
    private GerenciadorNotificacoes() {
        notificacoesPorUsuario = new HashMap<>();
    }
    
    public static GerenciadorNotificacoes getInstance() {
        if (instance == null) {
            instance = new GerenciadorNotificacoes();
        }
        return instance;
    }
    
    public void enviarNotificacao(String emailDestino, String titulo, String mensagem) {
        Notificacao notificacao = new Notificacao(titulo, mensagem, emailDestino);
        notificacoesPorUsuario.computeIfAbsent(emailDestino, k -> new ArrayList<>()).add(notificacao);
    }
    
    public List<Notificacao> getNotificacoes(String email) {
        return notificacoesPorUsuario.getOrDefault(email, new ArrayList<>());
    }
    
    public void mostrarNotificacoes(String email) {
        List<Notificacao> notifs = getNotificacoes(email);
        if (notifs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você não tem notificações.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== SUAS NOTIFICAÇÕES ===\n\n");
        for (Notificacao n : notifs) {
            sb.append("📢 ").append(n.getTitulo()).append("\n");
            sb.append("   ").append(n.getMensagem()).append("\n");
            sb.append("   📅 ").append(n.getData()).append("\n\n");
            n.setLida(true);
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}