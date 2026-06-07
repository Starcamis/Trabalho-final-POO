package Extras;

import usuario.*;
import Projeto.*;
import java.io.*;
import java.util.*;

public class GerenciadorDados {
    private static final String DIRETORIO = "dados";
    private static final String ARQUIVO_ALUNOS = DIRETORIO + "/alunos.ser";
    private static final String ARQUIVO_PROFESSORES = DIRETORIO + "/professores.ser";
    private static final String ARQUIVO_COORDENADORES = DIRETORIO + "/coordenadores.ser";
    private static final String ARQUIVO_PROJETOS = DIRETORIO + "/projetos.ser";
    
    static {
        File dir = new File(DIRETORIO);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public static void salvarAlunos(List<Aluno> alunos) {
        salvarLista(alunos, ARQUIVO_ALUNOS);
    }
    
    public static void salvarProfessores(List<Professor> professores) {
        salvarLista(professores, ARQUIVO_PROFESSORES);
    }
    
    public static void salvarCoordenadores(List<Coordenador> coordenadores) {
        salvarLista(coordenadores, ARQUIVO_COORDENADORES);
    }
    
    public static void salvarProjetos(List<Projeto> projetos) {
        salvarLista(projetos, ARQUIVO_PROJETOS);
    }
    
    public static void salvarTodosDados(List<Aluno> alunos, List<Professor> professores, 
                                        List<Coordenador> coordenadores, List<Projeto> projetos) {
        salvarAlunos(alunos);
        salvarProfessores(professores);
        salvarCoordenadores(coordenadores);
        salvarProjetos(projetos);
    }
    
    private static void salvarLista(List<?> lista, String arquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            // Erro silencioso - nao mostra no terminal
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Aluno> carregarAlunos() {
        return (List<Aluno>) carregarLista(ARQUIVO_ALUNOS);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Professor> carregarProfessores() {
        return (List<Professor>) carregarLista(ARQUIVO_PROFESSORES);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Coordenador> carregarCoordenadores() {
        return (List<Coordenador>) carregarLista(ARQUIVO_COORDENADORES);
    }
    
    @SuppressWarnings("unchecked")
    public static List<Projeto> carregarProjetos() {
        return (List<Projeto>) carregarLista(ARQUIVO_PROJETOS);
    }
    
    private static List<?> carregarLista(String arquivo) {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<?>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
}