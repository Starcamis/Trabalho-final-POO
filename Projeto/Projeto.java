package Projeto;

import usuario.Professor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Projeto implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorId = 0;
    private static final int PRAZO_MAXIMO_MESES = 24;
    
    private final int id;
    private String titulo;
    private String areaEstudo;
    private String descricao;
    private Professor orientador;
    private String dataInicio;
    private String prazo;
    private int vagas;
    private boolean encerrado;
    private String status;
    private List<Participacao> solicitacoes;
    private List<Participacao> participantes;
    private List<String> palavrasChave;
    
    public Projeto(String titulo, String areaEstudo, String descricao, Professor orientador,
                   String dataInicio, String prazo, int vagas) {
        this.id = ++contadorId;
        this.titulo = titulo;
        this.areaEstudo = areaEstudo;
        this.descricao = descricao;
        this.orientador = orientador;
        this.dataInicio = dataInicio;
        this.prazo = prazo;
        this.vagas = vagas;
        this.encerrado = false;
        this.status = "ATIVO";
        this.solicitacoes = new ArrayList<>();
        this.participantes = new ArrayList<>();
        this.palavrasChave = new ArrayList<>();
    }
    
    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAreaEstudo() { return areaEstudo; }
    public void setAreaEstudo(String areaEstudo) { this.areaEstudo = areaEstudo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Professor getOrientador() { return orientador; }
    public void setOrientador(Professor orientador) { this.orientador = orientador; }
    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    public String getPrazo() { return prazo; }
    public void setPrazo(String prazo) { this.prazo = prazo; }
    public int getVagas() { return vagas; }
    public void setVagas(int vagas) { this.vagas = vagas; }
    public boolean isEncerrado() { return encerrado; }
    public List<Participacao> getParticipantes() { return participantes; }
    public List<Participacao> getSolicitacoes() { return solicitacoes; }
    public List<String> getPalavrasChave() { return palavrasChave; }
    
    public void setEncerrado(boolean encerrado) {
        this.encerrado = encerrado;
        this.status = encerrado ? "ENCERRADO" : "ATIVO";
    }
    
    public void addPalavraChave(String palavra) {
        palavrasChave.add(palavra);
    }
    
    public int getVagasRestantes() {
        return vagas - participantes.size();
    }
    
    public void adicionarSolicitacao(Participacao participacao) {
        solicitacoes.add(participacao);
    }
    
    // CORRIGIDO: método para adicionar participante aprovado
    public void addParticipanteAprovado(Participacao participacao) {
        if (getVagasRestantes() > 0 && !participantes.contains(participacao)) {
            participantes.add(participacao);
        }
    }
    
    public void aprovarParticipante(Participacao participacao) {
        if (solicitacoes.contains(participacao) && getVagasRestantes() > 0) {
            solicitacoes.remove(participacao);
            participacao.aprovar();
            participantes.add(participacao);
        }
    }
    
    public void reprovarParticipante(Participacao participacao) {
        solicitacoes.remove(participacao);
        participacao.reprovar();
    }
    
    public void removerParticipante(Participacao participacao) {
        participantes.remove(participacao);
    }
    
    public List<Relatorio> getRelatoriosPendentes() {
        return participantes.stream()
            .flatMap(p -> p.getRelatorios().stream())
            .filter(r -> !r.isValidado())
            .collect(Collectors.toList());
    }
    
    public List<Participacao> getSolicitacoesPendentes() {
        return solicitacoes.stream()
            .filter(p -> !p.isAprovado())
            .collect(Collectors.toList());
    }
    
    public String getStatus() {
        if (encerrado) return "ENCERRADO";
        if (participantes.size() >= vagas) return "LOTADO";
        return "ATIVO";
    }
    
    public static int getPrazoMaximoMeses() {
        return PRAZO_MAXIMO_MESES;
    }
    
    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%s) - Vagas: %d/%d", 
                            id, titulo, areaEstudo, getStatus(), participantes.size(), vagas);
    }
}