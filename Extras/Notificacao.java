package Extras;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notificacao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String titulo;
    private String mensagem;
    private LocalDateTime data;
    private boolean lida;
    private String destinatarioEmail;
    
    public Notificacao(String titulo, String mensagem, String destinatarioEmail) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.destinatarioEmail = destinatarioEmail;
        this.data = LocalDateTime.now();
        this.lida = false;
    }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDateTime getData() { return data; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }
    public String getDestinatarioEmail() { return destinatarioEmail; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", data.toString(), titulo, mensagem);
    }
}