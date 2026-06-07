package Projeto;

import usuario.Professor;
import java.util.List;
import java.util.stream.Collectors;

public class BuscaFiltros {
    
    public static List<Projeto> buscarPorArea(List<Projeto> projetos, String area) {
        return projetos.stream()
            .filter(p -> p.getAreaEstudo().toLowerCase().contains(area.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public static List<Projeto> buscarPorOrientador(List<Projeto> projetos, Professor orientador) {
        return projetos.stream()
            .filter(p -> p.getOrientador().equals(orientador))
            .collect(Collectors.toList());
    }
    
    public static List<Projeto> buscarPorStatus(List<Projeto> projetos, String status) {
        return projetos.stream()
            .filter(p -> p.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }
    
    public static List<Projeto> buscarPorTitulo(List<Projeto> projetos, String titulo) {
        return projetos.stream()
            .filter(p -> p.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public static List<Projeto> buscarPorVagasDisponiveis(List<Projeto> projetos) {
        return projetos.stream()
            .filter(p -> p.getVagasRestantes() > 0 && !p.isEncerrado())
            .collect(Collectors.toList());
    }
    
    public static List<Projeto> buscarMultiplosFiltros(List<Projeto> projetos, String area, 
                                                        String status, Integer vagasMin) {
        return projetos.stream()
            .filter(p -> area == null || p.getAreaEstudo().toLowerCase().contains(area.toLowerCase()))
            .filter(p -> status == null || p.getStatus().equalsIgnoreCase(status))
            .filter(p -> vagasMin == null || p.getVagasRestantes() >= vagasMin)
            .collect(Collectors.toList());
    }
}