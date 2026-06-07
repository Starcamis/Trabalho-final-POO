package Projeto;

import usuario.Aluno;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Participacao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Aluno aluno;
    private Projeto projeto;
    private boolean aprovado;
    private String dataSolicitacao;
    private List<Relatorio> relatorios;
    
    public Participacao(Aluno aluno, Projeto projeto) {
        this.aluno = aluno;
        this.projeto = projeto;
        this.aprovado = false;
        this.dataSolicitacao = java.time.LocalDate.now().toString();
        this.relatorios = new ArrayList<>();
    }
    
    public Aluno getAluno() { return aluno; }
    public Projeto getProjeto() { return projeto; }
    public boolean isAprovado() { return aprovado; }
    public String getDataSolicitacao() { return dataSolicitacao; }
    public List<Relatorio> getRelatorios() { return relatorios; }
    
    public void aprovar() {
        this.aprovado = true;
    }
    
    public void adicionarRelatorio(Relatorio relatorio) {
        relatorios.add(relatorio);
    }
    
    @Override
    public String toString() {
        return aluno.getNome() + " - " + projeto.getTitulo() + " - " + (aprovado ? "Aprovado" : "Pendente");
    }
}