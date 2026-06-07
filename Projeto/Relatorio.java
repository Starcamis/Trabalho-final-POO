package Projeto;

import usuario.Aluno;
import usuario.Professor;

import java.io.Serializable;
import java.time.LocalDate;

import exceptions.ProjetoEncerradoException;

public class Relatorio implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorId = 0;
    
    private final int id;
    private Aluno aluno;
    private Projeto projeto;
    private String conteudo;
    private String dataEnvio;
    private boolean validado;
    private boolean aprovado;
    private String feedback;
    private Professor avaliador;
    
    public Relatorio(Aluno aluno, Projeto projeto, String conteudo) {
        if (projeto.isEncerrado()) {
            throw new ProjetoEncerradoException("Não é possível enviar relatório para projeto encerrado!");
        }
        this.id = ++contadorId;
        this.aluno = aluno;
        this.projeto = projeto;
        this.conteudo = conteudo;
        this.dataEnvio = LocalDate.now().toString();
        this.validado = false;
        this.aprovado = false;
    }
    
    public int getId() { return id; }
    public Aluno getAluno() { return aluno; }
    public Projeto getProjeto() { return projeto; }
    public String getConteudo() { return conteudo; }
    public String getDataEnvio() { return dataEnvio; }
    public boolean isValidado() { return validado; }
    public boolean isAprovado() { return aprovado; }
    public String getFeedback() { return feedback; }
    public Professor getAvaliador() { return avaliador; }
    
    public void validar(Professor professor, boolean aprovado, String feedback) {
        this.validado = true;
        this.aprovado = aprovado;
        this.feedback = feedback;
        this.avaliador = professor;
    }
    
    @Override
    public String toString() {
        return String.format("Relatório #%d - %s - %s - %s", 
                            id, aluno.getNome(), dataEnvio, validado ? (aprovado ? "Aprovado" : "Reprovado") : "Pendente");
    }
}