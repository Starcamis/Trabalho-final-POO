package usuario;

import Projeto.Participacao;
import Projeto.Projeto;
import Projeto.Relatorio;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Aluno extends Usuario {
    private String matricula;
    private String curso;
    private List<Participacao> participacoes;
    private List<Projeto> projetosConcluidos;
    private List<String> areasInteresse;
    
    public Aluno(String nome, String email, String senha, String matricula, String curso) {
        super(nome, email, senha);
        this.matricula = matricula;
        this.curso = curso;
        this.participacoes = new ArrayList<>();
        this.projetosConcluidos = new ArrayList<>();
        this.areasInteresse = new ArrayList<>();
    }
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public List<Participacao> getParticipacoes() { return participacoes; }
    public List<Projeto> getProjetosConcluidos() { return projetosConcluidos; }
    public List<String> getAreasInteresse() { return areasInteresse; }
    
    public void addAreaInteresse(String area) {
        if (!areasInteresse.contains(area)) {
            areasInteresse.add(area);
        }
    }
    
    public void solicitarParticipacao(Projeto projeto) {
        Participacao participacao = new Participacao(this, projeto);
        participacoes.add(participacao);
        projeto.adicionarSolicitacao(participacao);
    }
    
    public void enviarRelatorio(Projeto projeto, String conteudo) {
        Participacao participacao = getParticipacaoPorProjeto(projeto);
        if (participacao != null && participacao.isAprovado()) {
            Relatorio relatorio = new Relatorio(this, projeto, conteudo);
            participacao.adicionarRelatorio(relatorio);
        }
    }
    
    public void concluirProjeto(Projeto projeto) {
        Participacao participacao = getParticipacaoPorProjeto(projeto);
        if (participacao != null && participacao.isAprovado()) {
            projetosConcluidos.add(projeto);
        }
    }
    
    private Participacao getParticipacaoPorProjeto(Projeto projeto) {
        return participacoes.stream()
            .filter(p -> p.getProjeto().equals(projeto))
            .findFirst()
            .orElse(null);
    }
    
    public List<Projeto> getProjetosAtivos() {
        return participacoes.stream()
            .filter(p -> p.isAprovado() && !p.getProjeto().isEncerrado())
            .map(Participacao::getProjeto)
            .collect(Collectors.toList());
    }
    
    @Override
    public String getTipo() {
        return "Aluno";
    }
    
    @Override
    public String toString() {
        return super.toString() + " - " + matricula + " - " + curso;
    }
}