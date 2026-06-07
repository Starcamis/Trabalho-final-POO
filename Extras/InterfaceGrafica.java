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
        carregarDados();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            salvarDados();
        }));
        
        mostrarMenuPrincipal();
        salvarDados();
    }
    
    private static void carregarDados() {
        List<Aluno> alunosCarregados = GerenciadorDados.carregarAlunos();
        List<Professor> professoresCarregados = GerenciadorDados.carregarProfessores();
        List<Coordenador> coordenadoresCarregados = GerenciadorDados.carregarCoordenadores();
        List<Projeto> projetosCarregados = GerenciadorDados.carregarProjetos();
        
        if (alunosCarregados.isEmpty() && professoresCarregados.isEmpty() && 
            coordenadoresCarregados.isEmpty()) {
            iniciarDadosTeste();
        } else {
            alunos = alunosCarregados;
            professores = professoresCarregados;
            coordenadores = coordenadoresCarregados;
            projetos = projetosCarregados;
        }
    }
    
    private static void salvarDados() {
        GerenciadorDados.salvarTodosDados(alunos, professores, coordenadores, projetos);
    }
    
    private static boolean emailJaExiste(String email) {
        for (Aluno a : alunos) {
            if (a.getEmail().equalsIgnoreCase(email)) return true;
        }
        for (Professor p : professores) {
            if (p.getEmail().equalsIgnoreCase(email)) return true;
        }
        for (Coordenador c : coordenadores) {
            if (c.getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }
    
    private static boolean emailValido(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }
    
    private static boolean matriculaJaExiste(String matricula) {
        for (Aluno a : alunos) {
            if (a.getMatricula().equals(matricula)) return true;
        }
        return false;
    }
    
    private static boolean siapeJaExiste(String siape) {
        for (Professor p : professores) {
            if (p.getSiape().equals(siape)) return true;
        }
        return false;
    }
    
    private static void iniciarDadosTeste() {
        Coordenador coord = new Coordenador("Admin", "admin@ufpa.br", "admin123", "COORD001");
        coordenadores.add(coord);
        
        Professor prof1 = new Professor("Dr. Carlos Silva", "carlos@ufpa.br", "prof123", "123456", "Computacao");
        Professor prof2 = new Professor("Dra. Ana Souza", "ana@ufpa.br", "prof123", "789012", "Matematica");
        professores.add(prof1);
        professores.add(prof2);
        
        Aluno aluno1 = new Aluno("Joao Santos", "joao@ufpa.br", "aluno123", "2021001", "Ciencia da Computacao");
        Aluno aluno2 = new Aluno("Maria Oliveira", "maria@ufpa.br", "aluno123", "2021002", "Matematica");
        alunos.add(aluno1);
        alunos.add(aluno2);
        
        Projeto p1 = new Projeto("Inteligencia Artificial na Educacao", "Inteligencia Artificial", 
            "Projeto de pesquisa sobre aplicacoes de IA na educacao", prof1, "01/01/2024", "31/12/2024", 3);
        Projeto p2 = new Projeto("Big Data Analytics", "Banco de Dados", 
            "Analise de grandes volumes de dados", prof1, "01/02/2024", "30/11/2024", 2);
        Projeto p3 = new Projeto("Criptografia Quantica", "Seguranca", 
            "Estudo avancado de criptografia quantica", prof2, "01/03/2024", "31/10/2024", 2);
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
                "=== SISTEMA DE GERENCIAMENTO DE PROJETOS ===\nUniversidade Federal do Ceara\n\nEscolha uma opcao:",
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
        
        if (!emailValido(email)) {
            JOptionPane.showMessageDialog(null, "Erro: Email invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (emailJaExiste(email)) {
            JOptionPane.showMessageDialog(null, "Erro: Este email ja esta cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String senha = JOptionPane.showInputDialog("Senha:");
        if (senha == null) return;
        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(null, "Erro: A senha deve ter pelo menos 6 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String matricula = JOptionPane.showInputDialog("Matricula:");
        if (matricula == null) return;
        if (matriculaJaExiste(matricula)) {
            JOptionPane.showMessageDialog(null, "Erro: Esta matricula ja esta cadastrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String curso = JOptionPane.showInputDialog("Curso:");
        if (curso == null) return;
        
        Aluno aluno = new Aluno(nome, email, senha, matricula, curso);
        alunos.add(aluno);
        GerenciadorDados.salvarAlunos(alunos);
        JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
    }
    
    private static void cadastrarProfessor() {
        String nome = JOptionPane.showInputDialog("Nome completo:");
        if (nome == null) return;
        
        String email = JOptionPane.showInputDialog("Email:");
        if (email == null) return;
        
        if (!emailValido(email)) {
            JOptionPane.showMessageDialog(null, "Erro: Email invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (emailJaExiste(email)) {
            JOptionPane.showMessageDialog(null, "Erro: Este email ja esta cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String senha = JOptionPane.showInputDialog("Senha:");
        if (senha == null) return;
        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(null, "Erro: A senha deve ter pelo menos 6 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String siape = JOptionPane.showInputDialog("SIAPE:");
        if (siape == null) return;
        if (siapeJaExiste(siape)) {
            JOptionPane.showMessageDialog(null, "Erro: Este SIAPE ja esta cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String departamento = JOptionPane.showInputDialog("Departamento:");
        if (departamento == null) return;
        
        Professor professor = new Professor(nome, email, senha, siape, departamento);
        professores.add(professor);
        GerenciadorDados.salvarProfessores(professores);
        JOptionPane.showMessageDialog(null, "Professor cadastrado com sucesso!");
    }
    
    private static void menuAluno(Aluno aluno) {
        String[] opcoes = {"Ver Projetos Disponiveis", "Solicitar Participacao", "Meus Projetos", 
                           "Enviar Relatorio", "Ver Notificacoes", "Logout"};
        
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
            JOptionPane.showMessageDialog(null, "Nenhum projeto disponivel no momento.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== PROJETOS DISPONIVEIS ===\n\n");
        for (Projeto p : disponiveis) {
            sb.append(p).append("\n");
            sb.append("Descricao: ").append(p.getDescricao()).append("\n");
            sb.append("Orientador: ").append(p.getOrientador().getNome()).append("\n");
            sb.append("Prazo: ").append(p.getPrazo()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
    
    private static void solicitarParticipacao(Aluno aluno) {
        List<Projeto> disponiveis = BuscaFiltros.buscarPorVagasDisponiveis(projetos);
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum projeto disponivel.");
            return;
        }
        
        String[] opcoes = disponiveis.stream().map(Projeto::toString).toArray(String[]::new);
        int escolha = JOptionPane.showOptionDialog(null, "Selecione um projeto:", "Solicitar Participacao",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha >= 0) {
            Projeto projeto = disponiveis.get(escolha);
            aluno.solicitarParticipacao(projeto);
            notificador.enviarNotificacao(aluno.getEmail(), "Solicitacao enviada", 
                "Sua solicitacao para o projeto " + projeto.getTitulo() + " foi enviada!");
            notificador.enviarNotificacao(projeto.getOrientador().getEmail(), "Nova solicitacao", 
                "O aluno " + aluno.getNome() + " solicitou participacao no projeto " + projeto.getTitulo());
            JOptionPane.showMessageDialog(null, "Solicitacao enviada com sucesso!");
        }
    }
    
    private static void verMeusProjetos(Aluno aluno) {
        List<Projeto> meusProjetos = aluno.getProjetosAtivos();
        if (meusProjetos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Voce nao esta participando de nenhum projeto.");
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
            JOptionPane.showMessageDialog(null, "Voce nao esta em nenhum projeto para enviar relatorio.");
            return;
        }
        
        String[] opcoes = meusProjetos.stream().map(Projeto::toString).toArray(String[]::new);
        int escolha = JOptionPane.showOptionDialog(null, "Selecione o projeto:", "Enviar Relatorio",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        
        if (escolha >= 0) {
            String conteudo = JOptionPane.showInputDialog("Digite o conteudo do relatorio:");
            if (conteudo != null) {
                Projeto projeto = meusProjetos.get(escolha);
                try {
                    aluno.enviarRelatorio(projeto, conteudo);
                    notificador.enviarNotificacao(projeto.getOrientador().getEmail(), "Novo relatorio", 
                        "O aluno " + aluno.getNome() + " enviou um relatorio para o projeto " + projeto.getTitulo());
                    JOptionPane.showMessageDialog(null, "Relatorio enviado com sucesso!");
                } catch (ProjetoEncerradoException e) {
                    JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private static void menuProfessor(Professor professor) {
        String[] opcoes = {"Criar Projeto", "Meus Projetos", "Validar Relatorios", 
                           "Ver Solicitacoes", "Ver Notificacoes", "Estatisticas", "Logout"};
        
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
        String titulo = JOptionPane.showInputDialog("Titulo do projeto:");
        if (titulo == null) return;
        
        String area = JOptionPane.showInputDialog("Area de estudo:");
        if (area == null) return;
        
        String descricao = JOptionPane.showInputDialog("Descricao:");
        if (descricao == null) return;
        
        String dataInicio = JOptionPane.showInputDialog("Data de inicio (dd/MM/yyyy):");
        if (dataInicio == null) return;

        if (!dataValida(dataInicio)) {
            JOptionPane.showMessageDialog(null,
                    "Erro: Data de início inválida ou no passado!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String prazo = JOptionPane.showInputDialog("Prazo (dd/MM/yyyy):");
        if (prazo == null) return;

        if (!dataValida(prazo)) {
            JOptionPane.showMessageDialog(null,
                    "Erro: Prazo inválido ou no passado!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            java.time.format.DateTimeFormatter fmt =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate inicio = java.time.LocalDate.parse(dataInicio, fmt);
            java.time.LocalDate fim = java.time.LocalDate.parse(prazo, fmt);

            if (!fim.isAfter(inicio)) {
                JOptionPane.showMessageDialog(null,
                        "Erro: O prazo deve ser posterior à data de início!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: Formato de data inválido! Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String vagasStr = JOptionPane.showInputDialog("Numero de vagas:");
        if (vagasStr == null) return;
        
        try {
            int vagas = Integer.parseInt(vagasStr);
            Projeto projeto = new Projeto(titulo, area, descricao, professor, dataInicio, prazo, vagas);
            projetos.add(projeto);
            professor.getProjetosCoordenados().add(projeto);
            
            GerenciadorDados.salvarProjetos(projetos);
            GerenciadorDados.salvarProfessores(professores);
            
            JOptionPane.showMessageDialog(null, "Projeto criado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Numero de vagas invalido! Digite um numero inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void verMeusProjetosProfessor(Professor professor) {
        List<Projeto> meusProjetos = professor.getProjetosCoordenados();
        if (meusProjetos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Voce nao possui projetos cadastrados.");
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
            JOptionPane.showMessageDialog(null, "Nao ha relatorios pendentes.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== RELATORIOS PENDENTES ===\n\n");
        for (int i = 0; i < pendentes.size(); i++) {
            Relatorio r = pendentes.get(i);
            sb.append(i+1).append(". Projeto: ").append(r.getProjeto().getTitulo()).append("\n");
            sb.append("   Aluno: ").append(r.getAluno().getNome()).append("\n");
            sb.append("   Data: ").append(r.getDataEnvio()).append("\n");
            sb.append("   Conteudo: ").append(r.getConteudo()).append("\n\n");
        }
        
        String input = JOptionPane.showInputDialog(sb.toString() + "\nDigite o numero do relatorio para validar:");
        if (input == null) return;
        
        try {
            int escolha = Integer.parseInt(input);
            if (escolha >= 1 && escolha <= pendentes.size()) {
                Relatorio r = pendentes.get(escolha - 1);
                String[] opcoes = {"Aprovar", "Reprovar"};
                int decisao = JOptionPane.showOptionDialog(null, "Decisao sobre o relatorio:", "Validar",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
                
                String feedback = JOptionPane.showInputDialog("Feedback para o aluno:");
                if (feedback == null) return;
                
                boolean aprovado = (decisao == 0);
                professor.validarRelatorio(r, aprovado, feedback);
                notificador.enviarNotificacao(r.getAluno().getEmail(), "Relatorio " + (aprovado ? "Aprovado" : "Reprovado"), 
                    "Seu relatorio foi " + (aprovado ? "aprovado" : "reprovado") + ".\nFeedback: " + feedback);
                JOptionPane.showMessageDialog(null, "Relatorio validado com sucesso!");
                
                GerenciadorDados.salvarProfessores(professores);
                GerenciadorDados.salvarProjetos(projetos);
            } else {
                JOptionPane.showMessageDialog(null, "Numero invalido!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada invalida!");
        }
    }
    
    private static void verSolicitacoes(Professor professor) {
        List<Projeto> meusProjetos = professor.getProjetosCoordenados();
        
        List<Participacao> pendentes = new ArrayList<>();
        for (Projeto p : meusProjetos) {
            pendentes.addAll(p.getSolicitacoesPendentes());
        }
        
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nao ha solicitacoes pendentes.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("=== SOLICITACOES PENDENTES ===\n\n");
        for (int i = 0; i < pendentes.size(); i++) {
            Participacao part = pendentes.get(i);
            sb.append(i+1).append(". Aluno: ").append(part.getAluno().getNome()).append("\n");
            sb.append("   Projeto: ").append(part.getProjeto().getTitulo()).append("\n");
            sb.append("   Data: ").append(part.getDataSolicitacao()).append("\n\n");
        }
        
        String input = JOptionPane.showInputDialog(sb.toString() + "\nDigite o numero da solicitacao para avaliar:");
        if (input == null) return;
        
        try {
            int escolha = Integer.parseInt(input);
            if (escolha >= 1 && escolha <= pendentes.size()) {
                Participacao part = pendentes.get(escolha - 1);
                String[] opcoes = {"Aprovar", "Reprovar"};
                int decisao = JOptionPane.showOptionDialog(null, "Decisao sobre a solicitacao:", "Avaliar",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
                
                if (decisao == 0) {
                    part.getProjeto().aprovarParticipante(part);
                    notificador.enviarNotificacao(part.getAluno().getEmail(), "Solicitacao Aprovada", 
                        "Sua solicitacao para o projeto " + part.getProjeto().getTitulo() + " foi aprovada!");
                    JOptionPane.showMessageDialog(null, "Solicitacao aprovada!");
                } else if (decisao == 1) {
                    part.getProjeto().reprovarParticipante(part);
                    notificador.enviarNotificacao(part.getAluno().getEmail(), "Solicitacao Reprovada", 
                        "Sua solicitacao para o projeto " + part.getProjeto().getTitulo() + " foi reprovada.");
                    JOptionPane.showMessageDialog(null, "Solicitacao reprovada!");
                }
                
                GerenciadorDados.salvarProjetos(projetos);
                GerenciadorDados.salvarAlunos(alunos);
            } else {
                JOptionPane.showMessageDialog(null, "Numero invalido!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada invalida!");
        }
    }
    
    private static void menuCoordenador(Coordenador coordenador) {
        String[] opcoes = {"Gerenciar Projetos", "Gerenciar Usuarios", "Ver Estatisticas", 
                           "Ver Notificacoes", "Logout"};
        
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

    private static boolean dataValida(String data) {
        try {
            java.time.format.DateTimeFormatter fmt =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate dataInformada = java.time.LocalDate.parse(data, fmt);
            return !dataInformada.isBefore(java.time.LocalDate.now());
        } catch (Exception e) {
            return false;
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
            Professor[] profArray = professores.toArray(new Professor[0]);
            Professor selected = (Professor) JOptionPane.showInputDialog(null, "Selecione o orientador:", 
                "Novo Projeto", JOptionPane.QUESTION_MESSAGE, null, profArray, profArray[0]);
            if (selected != null) {
                String titulo = JOptionPane.showInputDialog("Titulo:");
                String area = JOptionPane.showInputDialog("Area:");
                String desc = JOptionPane.showInputDialog("Descricao:");
                String dataIni = JOptionPane.showInputDialog("Data inicio (dd/MM/yyyy):");
                String prazo = JOptionPane.showInputDialog("Prazo (dd/MM/yyyy):");

                if (!dataValida(dataIni)) {
                    JOptionPane.showMessageDialog(null, "Erro: Data de início inválida ou no passado!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!dataValida(prazo)) {
                    JOptionPane.showMessageDialog(null, "Erro: Prazo inválido ou no passado!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String vagasStr = JOptionPane.showInputDialog("Vagas:");
                if (vagasStr != null) {
                    try {
                        int vagas = Integer.parseInt(vagasStr);
                        Projeto p = new Projeto(titulo, area, desc, selected, dataIni, prazo, vagas);
                        projetos.add(p);
                        selected.getProjetosCoordenados().add(p);
                        GerenciadorDados.salvarProjetos(projetos);
                        GerenciadorDados.salvarProfessores(professores);
                        JOptionPane.showMessageDialog(null, "Projeto adicionado!");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Numero de vagas invalido!");
                    }
                }
            }
        } else if (escolha == 2) {
            Projeto[] projArray = projetos.toArray(new Projeto[0]);
            Projeto selected = (Projeto) JOptionPane.showInputDialog(null, "Selecione o projeto:", 
                "Editar Projeto", JOptionPane.QUESTION_MESSAGE, null, projArray, projArray[0]);
            if (selected != null) {
                String novoTitulo = JOptionPane.showInputDialog("Novo titulo:", selected.getTitulo());
                String novaArea = JOptionPane.showInputDialog("Nova area:", selected.getAreaEstudo());
                String novaDesc = JOptionPane.showInputDialog("Nova descricao:", selected.getDescricao());
                String novaDataIni = JOptionPane.showInputDialog("Nova data inicio:", selected.getDataInicio());
                String novoPrazo = JOptionPane.showInputDialog("Novo prazo:", selected.getPrazo());

                if (!dataValida(novaDataIni)) {
                    JOptionPane.showMessageDialog(null, "Erro: Data de início inválida ou no passado!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!dataValida(novoPrazo)) {
                    JOptionPane.showMessageDialog(null, "Erro: Prazo inválido ou no passado!",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String novasVagasStr = JOptionPane.showInputDialog("Novas vagas:", selected.getVagas());
                if (novasVagasStr != null) {
                    try {
                        int novasVagas = Integer.parseInt(novasVagasStr);
                        coordenador.editarProjeto(selected, novoTitulo, novaArea, novaDesc, novaDataIni, novoPrazo, novasVagas);
                        GerenciadorDados.salvarProjetos(projetos);
                        JOptionPane.showMessageDialog(null, "Projeto editado!");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Numero de vagas invalido!");
                    }
                }
            }
        } else if (escolha == 3) {
            Projeto[] projArray = projetos.toArray(new Projeto[0]);
            Projeto selected = (Projeto) JOptionPane.showInputDialog(null, "Selecione o projeto para remover:", 
                "Remover Projeto", JOptionPane.QUESTION_MESSAGE, null, projArray, projArray[0]);
            if (selected != null) {
                coordenador.removerProjeto(selected, projetos);
                GerenciadorDados.salvarProjetos(projetos);
                JOptionPane.showMessageDialog(null, "Projeto removido!");
            }
        }
    }
    
    private static void gerenciarUsuarios(Coordenador coordenador) {
        String[] opcoes = {"Listar Alunos", "Listar Professores", "Ativar/Desativar Usuario"};
        int escolha = JOptionPane.showOptionDialog(null, "Gerenciar Usuarios", "Coordenador",
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
            Usuario selected = (Usuario) JOptionPane.showInputDialog(null, "Selecione o usuario:", 
                "Ativar/Desativar", JOptionPane.QUESTION_MESSAGE, null, userArray, userArray[0]);
            if (selected != null) {
                boolean novoStatus = !selected.isAtivo();
                coordenador.gerenciarUsuario(selected, novoStatus);
                
                if (selected instanceof Aluno) {
                    GerenciadorDados.salvarAlunos(alunos);
                } else if (selected instanceof Professor) {
                    GerenciadorDados.salvarProfessores(professores);
                } else if (selected instanceof Coordenador) {
                    GerenciadorDados.salvarCoordenadores(coordenadores);
                }
                
                JOptionPane.showMessageDialog(null, "Usuario " + selected.getNome() + " agora esta " + 
                    (novoStatus ? "ATIVO" : "INATIVO"));
            }
        }
    }
    
    private static void mostrarEstatisticas() {
        String[] opcoes = {"Estatisticas Gerais", "Projetos por Area", "Projetos Mais Ativos"};
        int escolha = JOptionPane.showOptionDialog(null, "Estatisticas", "Relatorios",
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