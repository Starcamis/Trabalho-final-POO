package usuario;

public class Professor extends Usuario{

    public Professor(String nome, String email, String senha) {
        super(nome, email, senha);
    }
    @Override
    public void exibirMenu(){
        System.out.println("Menu do professor");
    }
}