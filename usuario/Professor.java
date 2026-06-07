package usuario;

import Projeto.Projeto;
import Projeto.Relatorio;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Professor extends Usuario {
    private String siape;
    private String departamento;
    private List<Projeto> projetosCoordenados;
    
    public Professor(String nome, String email, String senha, String siape, String departamento) {
        super(nome, email, senha);
        this.siape = siape;
        this.departamento = departamento;
        this.projetosCoordenados = new ArrayList<>();
    }
    
    public String getSiape() { return siape; }
    public void setSiape(String siape) { this.siape = siape; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public List<Projeto> getProjetosCoordenados() { return projetosCoordenados; }
    
    public void criarProjeto(String titulo, String areaEstudo, String descricao, 
                             String dataInicio, String prazo, int vagas) {
        Projeto projeto = new Projeto(titulo, areaEstudo, descricao, this, dataInicio, prazo, vagas);
        projetosCoordenados.add(projeto);
    }
    
    public void validarRelatorio(Relatorio relatorio, boolean aprovado, String feedback) {
        relatorio.validar(this, aprovado, feedback);
    }
    
    public List<Relatorio> getRelatoriosPendentes() {
        return projetosCoordenados.stream()
            .flatMap(p -> p.getRelatoriosPendentes().stream())
            .collect(Collectors.toList());
    }
    
    @Override
    public String getTipo() {
        return "Professor";
    }
    
    @Override
    public String toString() {
        return super.toString() + " - " + siape + " - " + departamento;
    }
}