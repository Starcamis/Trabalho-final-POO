package usuario;

public class Aluno extends Usuario{
    public Aluno(String nome, String email, String senha){
        super(nome, email, senha);
    }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do aluno");
    }
}
