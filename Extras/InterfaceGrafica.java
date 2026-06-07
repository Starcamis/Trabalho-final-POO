package Extras;

import usuario.*;
import Projeto.*;
import Autenticacao.*;
import exceptions.*;

import javax.swing.*;
import java.util.*;

public class InterfaceGrafica {
    private static List<Aluno> alunos = new ArrayList<>();
    private static List<Professor> professores = new ArrayList<>();
    private static List<Coordenador> coordenadores = new ArrayList<>();
    private static List<Projeto> projetos = new ArrayList<>();
    private static AutenticacaoService auth = AutenticacaoService.getInstance();
    private static GerenciadorNotificacoes notificador = GerenciadorNotificacoes.getInstance();
    
    public static void main(String[] args) {
        iniciarDadosTeste();
        mostrarMenuPrincipal();
    }
    
    private static void iniciarDadosTeste() {
        Coordenador coord = new Coordenador("Admin", "admin@ufpa.br", "admin123", "COORD001");
        coordenadores.add(coord);
        
        Professor prof1 = new Professor("Dr. Carlos Silva", "carlos@ufpa.br", "prof123", "123456", "Computação");
        Professor prof2 = new Professor("Dra. Ana Souza", "ana@ufpa.br", "prof123", "789012", "Matemática");
        professores.add(prof1);
        professores.add(prof2);
        
        Aluno aluno1 = new Aluno("João Santos", "joao@ufpa.br", "aluno123", "2021001", "Ciência da Computação");
        Aluno aluno2 = new Aluno("Maria Oliveira", "maria@ufpa.br", "aluno123", "2021002", "Matemática");
        alunos.add(aluno1);
        alunos.add(aluno2);
        
        Projeto p1 = new Projeto("Inteligência Artificial na Educação", "Inteligência Artificial", 
            "Projeto de pesquisa sobre aplicações de IA na educação", prof1, "01/01/2024", "31/12/2024", 3);
        Projeto p2 = new Projeto("Big Data Analytics", "Banco de Dados", 
            "Análise de grandes volumes de dados", prof1, "01/02/2024", "30/11/2024", 2);
        Projeto p3 = new Projeto("Criptografia Quântica", "Segurança", 
            "Estudo avançado de criptografia quântica", prof2, "01/03/2024", "31/10/2024", 2);
        projetos.add(p1);
        projetos.add(p2);
        projetos.add(p3);
    }
    
    private static List<Usuario> obterTodosUsuarios() {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(alunos);
        todos.addAll(professores);
        todos.addAll(coordenadores);
        return todos;
    }
    
    private static void mostrarMenuPrincipal() {
        while (true) {
            String[] opcoes = {"Login", "Cadastrar Aluno", "Cadastrar Professor", "Sair"};
            int escolha = JOptionPane.showOptionDialog(null, 
                "=== SISTEMA DE GERENCIAMENTO DE PROJETOS ===\nUniversidade Federal do Ceará\n\nEscolha uma opção:",
                "Sistema de Projetos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opcoes, opcoes[0]);
            
            if (escolha == 0) {
                fazerLogin();
            } else if (escolha == 1) {
                cadastrarAluno();
            } else if (escolha == 2) {
                cadastrarProfessor();
            } else {
                System.exit(0);
            }
        }
    }
    
    private static void fazerLogin() {
        String email = JOptionPane.showInputDialog("Email:");
        if (email == null) return;
        
        String senha = JOptionPane.showInputDialog("Senha:");
        if (senha == null) return;
        
        try {
            Usuario user = auth.login(obterTodosUsuarios(), email, senha);
            JOptionPane.showMessageDialog(null, "Bem-vindo(a), " + user.getNome() + "!");
            
            if (user instanceof Aluno) {
                menuAluno((Aluno) user);
            } else if (user instanceof Professor) {
                menuProfessor((Professor) user);
            } else if (user instanceof Coordenador) {
                menuCoordenador((Coordenador) user);
            }
        } catch (LoginInvalidoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void cadastrarAluno() {
        String nome = JOptionPane.showInputDialog("Nome completo:");
        if (nome == null) return;
        String email = JOptionPane.showInputDialog("Email:");
        if (email == null) return;
        String senha = JOptionPane.showInputDialog("Senha:");
        if (senha == null) return;
        String matricula = JOptionPane.showInputDialog("Matrícula:");
        if (matricula == null) return;
        String curso = JOptionPane.showInputDialog("Curso:");
        if (curso == null) return;
        
        Aluno aluno = new Aluno(nome, email, senha, matricula, curso);
        alunos.add(aluno);
        JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
    }
    
    private static void cadastrarProfessor() {
        String nome = JOptionPane.showInputDialog("Nome completo:");
        if (nome == null) return;
        String email = JOptionPane.showInputDialog("Email:");
        if (email == null) return;
        String senha = JOptionPane.showInputDialog("Senha:");
        if (senha == null) return;
        String siape = JOptionPane.showInputDialog("SIAPE:");
        if (siape == null) return;
        String departamento = JOptionPane.showInputDialog("Departamento:");
        if (departamento == null) return;
        
        Professor professor = new Professor(nome, email, senha, siape, departamento);
        professores.add(professor);
        JOptionPane.showMessageDialog(null, "Professor cadastrado com sucesso!");
    }
    
    private static void menuAluno(Aluno aluno) {
        String[] opcoes = {"Ver Projetos Disponíveis", "Solicitar Participação", "Meus Projetos", 
                           "Enviar Relatório", "Ver Notificações", "Logout"};
        
        while (true) {
            int escolha = JOptionPane.showOptionDialog(null, "Menu do Aluno - " + aluno.getNome(),
                "Sistema de Projetos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opcoes, opcoes[0]);
            
            if (escolha == 0) {
                verProjetosDisponiveis(aluno);
            } else if (escolha == 1) {
                solicitarParticipacao(aluno);
            } else if (escolha == 2) {
                verMeusProjetos(aluno);
            } else if (escolha == 3) {
                enviarRelatorio(aluno);
            } else if (escolha == 4) {
                notificador.mostrarNotificacoes(aluno.getEmail());
            } else {
                auth.logout();
                break;
            }
        }
    }
    
    private static void verProjetosDisponiveis(Aluno aluno) {
        List<Projeto> disponiveis = BuscaFiltros.buscarPorVagasDisponiveis(projetos);
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum projeto disponível no momento.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== PROJETOS DISPONÍVEIS ===\n\n");
        for (Projeto p : disponiveis) {
            sb.append(p).append("\n");
            sb.append("Descrição: ").append(p.getDescricao()).append("\n");
            sb.append("Orientador: ").append(p.getOrientador().getNome()).append("\n");
            sb.append("Prazo: ").append(p.getPrazo()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private static void solicitarParticipacao(Aluno aluno) {
        List<Projeto> disponiveis = BuscaFiltros.buscarPorVagasDisponiveis(projetos);
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum projeto disponível.");
            return;
        }
        
        String[] opcoes = disponiveis.stream().map(Projeto::toString).toArray(String[]::new);
        int escolha = JOptionPane.showOptionDialog(null, "Selecione um projeto:", "Solicitar Participação",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha >= 0) {
            Projeto projeto = disponiveis.get(escolha);
            aluno.solicitarParticipacao(projeto);
            notificador.enviarNotificacao(aluno.getEmail(), "Solicitação enviada", 
                "Sua solicitação para o projeto " + projeto.getTitulo() + " foi enviada!");
            notificador.enviarNotificacao(projeto.getOrientador().getEmail(), "Nova solicitação", 
                "O aluno " + aluno.getNome() + " solicitou participação no projeto " + projeto.getTitulo());
            JOptionPane.showMessageDialog(null, "Solicitação enviada com sucesso!");
        }
    }
    
    private static void verMeusProjetos(Aluno aluno) {
        List<Projeto> meusProjetos = aluno.getProjetosAtivos();
        if (meusProjetos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você não está participando de nenhum projeto.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== MEUS PROJETOS ===\n\n");
        for (Projeto p : meusProjetos) {
            sb.append(p).append("\n");
            sb.append("Orientador: ").append(p.getOrientador().getNome()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private static void enviarRelatorio(Aluno aluno) {
        List<Projeto> meusProjetos = aluno.getProjetosAtivos();
        if (meusProjetos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você não está em nenhum projeto para enviar relatório.");
            return;
        }
        
        String[] opcoes = meusProjetos.stream().map(Projeto::toString).toArray(String[]::new);
        int escolha = JOptionPane.showOptionDialog(null, "Selecione o projeto:", "Enviar Relatório",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha >= 0) {
            String conteudo = JOptionPane.showInputDialog("Digite o conteúdo do relatório:");
            if (conteudo != null) {
                Projeto projeto = meusProjetos.get(escolha);
                try {
                    aluno.enviarRelatorio(projeto, conteudo);
                    notificador.enviarNotificacao(projeto.getOrientador().getEmail(), "Novo relatório", 
                        "O aluno " + aluno.getNome() + " enviou um relatório para o projeto " + projeto.getTitulo());
                    JOptionPane.showMessageDialog(null, "Relatório enviado com sucesso!");
                } catch (ProjetoEncerradoException e) {
                    JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private static void menuProfessor(Professor professor) {
        String[] opcoes = {"Criar Projeto", "Meus Projetos", "Validar Relatórios", 
                           "Ver Solicitações", "Ver Notificações", "Estatísticas", "Logout"};
        
        while (true) {
            int escolha = JOptionPane.showOptionDialog(null, "Menu do Professor - " + professor.getNome(),
                "Sistema de Projetos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opcoes, opcoes[0]);
            
            if (escolha == 0) {
                criarProjeto(professor);
            } else if (escolha == 1) {
                verMeusProjetosProfessor(professor);
            } else if (escolha == 2) {
                validarRelatorios(professor);
            } else if (escolha == 3) {
                verSolicitacoes(professor);
            } else if (escolha == 4) {
                notificador.mostrarNotificacoes(professor.getEmail());
            } else if (escolha == 5) {
                mostrarEstatisticas();
            } else {
                auth.logout();
                break;
            }
        }
    }
    
    private static void criarProjeto(Professor professor) {
        String titulo = JOptionPane.showInputDialog("Título do projeto:");
        if (titulo == null) return;
        String area = JOptionPane.showInputDialog("Área de estudo:");
        if (area == null) return;
        String descricao = JOptionPane.showInputDialog("Descrição:");
        if (descricao == null) return;
        String dataInicio = JOptionPane.showInputDialog("Data de início (dd/MM/yyyy):");
        if (dataInicio == null) return;
        String prazo = JOptionPane.showInputDialog("Prazo (dd/MM/yyyy):");
        if (prazo == null) return;
        String vagasStr = JOptionPane.showInputDialog("Número de vagas:");
        if (vagasStr == null) return;
        
        int vagas = Integer.parseInt(vagasStr);
        Projeto projeto = new Projeto(titulo, area, descricao, professor, dataInicio, prazo, vagas);
        projetos.add(projeto);
        professor.getProjetosCoordenados().add(projeto);
        JOptionPane.showMessageDialog(null, "Projeto criado com sucesso!");
    }
    
    private static void verMeusProjetosProfessor(Professor professor) {
        List<Projeto> meusProjetos = professor.getProjetosCoordenados();
        if (meusProjetos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você não possui projetos cadastrados.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== MEUS PROJETOS ===\n\n");
        for (Projeto p : meusProjetos) {
            sb.append(p).append("\n");
            sb.append("Participantes: ").append(p.getParticipantes().size()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private static void validarRelatorios(Professor professor) {
        List<Relatorio> pendentes = professor.getRelatoriosPendentes();
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há relatórios pendentes.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== RELATÓRIOS PENDENTES ===\n\n");
        for (int i = 0; i < pendentes.size(); i++) {
            Relatorio r = pendentes.get(i);
            sb.append(i+1).append(". Projeto: ").append(r.getProjeto().getTitulo()).append("\n");
            sb.append("   Aluno: ").append(r.getAluno().getNome()).append("\n");
            sb.append("   Data: ").append(r.getDataEnvio()).append("\n");
            sb.append("   Conteúdo: ").append(r.getConteudo()).append("\n\n");
        }
        
        int escolha = Integer.parseInt(JOptionPane.showInputDialog(sb.toString() + "\nDigite o número do relatório para validar:"));
        if (escolha >= 1 && escolha <= pendentes.size()) {
            Relatorio r = pendentes.get(escolha - 1);
            String[] opcoes = {"Aprovar", "Reprovar"};
            int decisao = JOptionPane.showOptionDialog(null, "Decisão sobre o relatório:", "Validar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
            
            String feedback = JOptionPane.showInputDialog("Feedback para o aluno:");
            boolean aprovado = (decisao == 0);
            professor.validarRelatorio(r, aprovado, feedback);
            notificador.enviarNotificacao(r.getAluno().getEmail(), "Relatório " + (aprovado ? "Aprovado" : "Reprovado"), 
                "Seu relatório foi " + (aprovado ? "aprovado" : "reprovado") + ".\nFeedback: " + feedback);
            JOptionPane.showMessageDialog(null, "Relatório validado com sucesso!");
        }
    }
    
    private static void verSolicitacoes(Professor professor) {
        List<Projeto> meusProjetos = professor.getProjetosCoordenados();
        List<Participacao> todasSolicitacoes = new ArrayList<>();
        for (Projeto p : meusProjetos) {
            todasSolicitacoes.addAll(p.getParticipantes()); // solicitacoes não exposto, ajuste
        }
        
        // Buscar solicitações pendentes (não aprovadas)
        List<Participacao> pendentes = new ArrayList<>();
        for (Projeto p : meusProjetos) {
            for (Participacao part : p.getSolicitacoes()) {
                if (!part.isAprovado()) {
                    pendentes.add(part);
                }
            }
        }
        
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há solicitações pendentes.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== SOLICITAÇÕES PENDENTES ===\n\n");
        for (int i = 0; i < pendentes.size(); i++) {
            Participacao part = pendentes.get(i);
            sb.append(i+1).append(". Aluno: ").append(part.getAluno().getNome()).append("\n");
            sb.append("   Projeto: ").append(part.getProjeto().getTitulo()).append("\n");
            sb.append("   Data: ").append(part.getDataSolicitacao()).append("\n\n");
        }
        
        int escolha = Integer.parseInt(JOptionPane.showInputDialog(sb.toString() + "\nDigite o número da solicitação para avaliar:"));
        if (escolha >= 1 && escolha <= pendentes.size()) {
            Participacao part = pendentes.get(escolha - 1);
            String[] opcoes = {"Aprovar", "Reprovar"};
            int decisao = JOptionPane.showOptionDialog(null, "Decisão sobre a solicitação:", "Avaliar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
            
            if (decisao == 0) {
                part.getProjeto().aprovarParticipante(part);
                notificador.enviarNotificacao(part.getAluno().getEmail(), "Solicitação Aprovada", 
                    "Sua solicitação para o projeto " + part.getProjeto().getTitulo() + " foi aprovada!");
                JOptionPane.showMessageDialog(null, "Solicitação aprovada!");
            } else {
                part.getProjeto().reprovarParticipante(part);
                notificador.enviarNotificacao(part.getAluno().getEmail(), "Solicitação Reprovada", 
                    "Sua solicitação para o projeto " + part.getProjeto().getTitulo() + " foi reprovada.");
                JOptionPane.showMessageDialog(null, "Solicitação reprovada!");
            }
        }
    }
    
    private static void menuCoordenador(Coordenador coordenador) {
        String[] opcoes = {"Gerenciar Projetos", "Gerenciar Usuários", "Ver Estatísticas", 
                           "Ver Notificações", "Logout"};
        
        while (true) {
            int escolha = JOptionPane.showOptionDialog(null, "Menu do Coordenador - " + coordenador.getNome(),
                "Sistema de Projetos", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opcoes, opcoes[0]);
            
            if (escolha == 0) {
                gerenciarProjetos(coordenador);
            } else if (escolha == 1) {
                gerenciarUsuarios(coordenador);
            } else if (escolha == 2) {
                mostrarEstatisticas();
            } else if (escolha == 3) {
                notificador.mostrarNotificacoes(coordenador.getEmail());
            } else {
                auth.logout();
                break;
            }
        }
    }
    
    private static void gerenciarProjetos(Coordenador coordenador) {
        String[] opcoes = {"Listar Projetos", "Adicionar Projeto", "Editar Projeto", "Remover Projeto"};
        int escolha = JOptionPane.showOptionDialog(null, "Gerenciar Projetos", "Coordenador",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha == 0) {
            StringBuilder sb = new StringBuilder("=== TODOS OS PROJETOS ===\n\n");
            for (Projeto p : projetos) {
                sb.append(p).append("\n");
                sb.append("Orientador: ").append(p.getOrientador().getNome()).append("\n\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } else if (escolha == 1) {
            // Selecionar professor orientador
            Professor[] profArray = professores.toArray(new Professor[0]);
            Professor selected = (Professor) JOptionPane.showInputDialog(null, "Selecione o orientador:", 
                "Novo Projeto", JOptionPane.QUESTION_MESSAGE, null, profArray, profArray[0]);
            if (selected != null) {
                String titulo = JOptionPane.showInputDialog("Título:");
                String area = JOptionPane.showInputDialog("Área:");
                String desc = JOptionPane.showInputDialog("Descrição:");
                String dataIni = JOptionPane.showInputDialog("Data início:");
                String prazo = JOptionPane.showInputDialog("Prazo:");
                int vagas = Integer.parseInt(JOptionPane.showInputDialog("Vagas:"));
                Projeto p = new Projeto(titulo, area, desc, selected, dataIni, prazo, vagas);
                projetos.add(p);
                selected.getProjetosCoordenados().add(p);
                JOptionPane.showMessageDialog(null, "Projeto adicionado!");
            }
        } else if (escolha == 2) {
            Projeto[] projArray = projetos.toArray(new Projeto[0]);
            Projeto selected = (Projeto) JOptionPane.showInputDialog(null, "Selecione o projeto:", 
                "Editar Projeto", JOptionPane.QUESTION_MESSAGE, null, projArray, projArray[0]);
            if (selected != null) {
                String novoTitulo = JOptionPane.showInputDialog("Novo título:", selected.getTitulo());
                String novaArea = JOptionPane.showInputDialog("Nova área:", selected.getAreaEstudo());
                String novaDesc = JOptionPane.showInputDialog("Nova descrição:", selected.getDescricao());
                String novaDataIni = JOptionPane.showInputDialog("Nova data início:", selected.getDataInicio());
                String novoPrazo = JOptionPane.showInputDialog("Novo prazo:", selected.getPrazo());
                int novasVagas = Integer.parseInt(JOptionPane.showInputDialog("Novas vagas:", selected.getVagas()));
                coordenador.editarProjeto(selected, novoTitulo, novaArea, novaDesc, novaDataIni, novoPrazo, novasVagas);
                JOptionPane.showMessageDialog(null, "Projeto editado!");
            }
        } else if (escolha == 3) {
            Projeto[] projArray = projetos.toArray(new Projeto[0]);
            Projeto selected = (Projeto) JOptionPane.showInputDialog(null, "Selecione o projeto para remover:", 
                "Remover Projeto", JOptionPane.QUESTION_MESSAGE, null, projArray, projArray[0]);
            if (selected != null) {
                coordenador.removerProjeto(selected, projetos);
                JOptionPane.showMessageDialog(null, "Projeto removido!");
            }
        }
    }
    
    private static void gerenciarUsuarios(Coordenador coordenador) {
        String[] opcoes = {"Listar Alunos", "Listar Professores", "Ativar/Desativar Usuário"};
        int escolha = JOptionPane.showOptionDialog(null, "Gerenciar Usuários", "Coordenador",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha == 0) {
            StringBuilder sb = new StringBuilder("=== ALUNOS ===\n\n");
            for (Aluno a : alunos) {
                sb.append(a).append(" - ").append(a.isAtivo() ? "Ativo" : "Inativo").append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } else if (escolha == 1) {
            StringBuilder sb = new StringBuilder("=== PROFESSORES ===\n\n");
            for (Professor p : professores) {
                sb.append(p).append(" - ").append(p.isAtivo() ? "Ativo" : "Inativo").append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } else if (escolha == 2) {
            List<Usuario> todos = obterTodosUsuarios();
            Usuario[] userArray = todos.toArray(new Usuario[0]);
            Usuario selected = (Usuario) JOptionPane.showInputDialog(null, "Selecione o usuário:", 
                "Ativar/Desativar", JOptionPane.QUESTION_MESSAGE, null, userArray, userArray[0]);
            if (selected != null) {
                boolean novoStatus = !selected.isAtivo();
                coordenador.gerenciarUsuario(selected, novoStatus);
                JOptionPane.showMessageDialog(null, "Usuário " + selected.getNome() + " agora está " + 
                    (novoStatus ? "ATIVO" : "INATIVO"));
            }
        }
    }
    
    private static void mostrarEstatisticas() {
        String[] opcoes = {"Estatísticas Gerais", "Projetos por Área", "Projetos Mais Ativos"};
        int escolha = JOptionPane.showOptionDialog(null, "Estatísticas", "Relatórios",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha == 0) {
            Estatistica.exibirEstatisticasGerais(projetos, alunos, professores);
        } else if (escolha == 1) {
            Estatistica.exibirProjetosPorArea(projetos);
        } else if (escolha == 2) {
            Estatistica.exibirProjetosMaisAtivos(projetos);
        }
    }
}