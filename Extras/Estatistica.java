package Extras;

import Projeto.Projeto;
import Projeto.Participacao;
import usuario.Aluno;
import usuario.Professor;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class Estatistica {
    
    public static void exibirEstatisticasGerais(List<Projeto> projetos, List<Aluno> alunos, List<Professor> professores) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== ESTATÍSTICAS GERAIS ==========\n\n");
        sb.append(" TOTAL DE PROJETOS: ").append(projetos.size()).append("\n");
        sb.append(" TOTAL DE ALUNOS: ").append(alunos.size()).append("\n");
        sb.append(" TOTAL DE PROFESSORES: ").append(professores.size()).append("\n");
        
        long projetosAtivos = projetos.stream().filter(p -> !p.isEncerrado()).count();
        long projetosEncerrados = projetos.stream().filter(Projeto::isEncerrado).count();
        
        sb.append("\nPROJETOS ATIVOS: ").append(projetosAtivos).append("\n");
        sb.append(" PROJETOS ENCERRADOS: ").append(projetosEncerrados).append("\n");
        
        // CÓDIGO CORRIGIDO
        int totalParticipacoes = alunos.stream()
            .mapToInt(a -> (int) a.getParticipacoes().stream().filter(Participacao::isAprovado).count())
            .sum();
        
        sb.append(" TOTAL DE PARTICIPAÇÕES APROVADAS: ").append(totalParticipacoes).append("\n");
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    // Restante da classe permanece igual...
    public static void exibirProjetosPorArea(List<Projeto> projetos) {
        Map<String, Long> projetosPorArea = projetos.stream()
            .collect(Collectors.groupingBy(Projeto::getAreaEstudo, Collectors.counting()));
        
        StringBuilder sb = new StringBuilder("========== PROJETOS POR ÁREA ==========\n\n");
        projetosPorArea.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(e -> sb.append(" ").append(e.getKey()).append(": ").append(e.getValue()).append(" projeto(s)\n"));
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    public static void exibirProjetosMaisAtivos(List<Projeto> projetos) {
        List<Projeto> maisAtivos = projetos.stream()
            .sorted((p1, p2) -> Integer.compare(p2.getParticipantes().size(), p1.getParticipantes().size()))
            .limit(5)
            .collect(Collectors.toList());
        
        StringBuilder sb = new StringBuilder("========== PROJETOS MAIS ATIVOS ==========\n\n");
        for (int i = 0; i < maisAtivos.size(); i++) {
            Projeto p = maisAtivos.get(i);
            sb.append((i+1)).append(". ").append(p.getTitulo()).append("\n");
            sb.append("   Área: ").append(p.getAreaEstudo()).append("\n");
            sb.append("   Participantes: ").append(p.getParticipantes().size()).append("\n\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}