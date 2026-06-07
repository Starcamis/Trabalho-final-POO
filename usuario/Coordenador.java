package usuario;

import Projeto.Projeto;
import java.util.List;

public class Coordenador extends Usuario {
    private String codigoCoordenacao;
    
    public Coordenador(String nome, String email, String senha, String codigoCoordenacao) {
        super(nome, email, senha);
        this.codigoCoordenacao = codigoCoordenacao;
    }
    
    public String getCodigoCoordenacao() { return codigoCoordenacao; }
    public void setCodigoCoordenacao(String codigoCoordenacao) { this.codigoCoordenacao = codigoCoordenacao; }
    
    public void cadastrarProjeto(Projeto projeto, List<Projeto> listaProjetos) {
        listaProjetos.add(projeto);
    }
    
    public void editarProjeto(Projeto projeto, String novoTitulo, String novaArea, 
                              String novaDescricao, String novaDataInicio, 
                              String novoPrazo, int novasVagas) {
        projeto.setTitulo(novoTitulo);
        projeto.setAreaEstudo(novaArea);
        projeto.setDescricao(novaDescricao);
        projeto.setDataInicio(novaDataInicio);
        projeto.setPrazo(novoPrazo);
        projeto.setVagas(novasVagas);
    }
    
    public void removerProjeto(Projeto projeto, List<Projeto> listaProjetos) {
        listaProjetos.remove(projeto);
    }
    
    public void gerenciarUsuario(Usuario usuario, boolean ativar) {
        usuario.setAtivo(ativar);
    }
    
    @Override
    public String getTipo() {
        return "Coordenador";
    }
}