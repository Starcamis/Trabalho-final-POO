package usuario;

public class Coordenador extends Usuario {
    public Coordenador(String nome, String email, String senha){
        super(nome, email, senha);
    }
    @Override
    public void exibirMenu(){
        System.out.println("Menu do Coordenador");
    }
}
