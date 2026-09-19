package main;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.RelatorioPrioridadeDAO;
import dao.TrechoRodoviaDAO;
import db.ConexaoBD;
import model.EquipeManutencao;
import model.IntervencaoOperacionalRegistro;
import model.StatusIntervencao;
import model.TipoClima;
import model.TrechoRodovia;
import model.TrechoRodoviaRegistro;
import service.GeradorRelatorio;

import java.sql.SQLException;
import java.time.LocalDate;

/** Demonstra a conexão, todos os CRUDs e a persistência do relatório. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ConexaoBD conexao = ConexaoBD.getInstancia();
        try {
            conexao.conectar();
            System.out.println("Conexão com o Oracle realizada com sucesso.");
            executarDemonstracao();
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Não foi possível executar a demonstração: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                conexao.desconectar();
                System.out.println("Conexão encerrada.");
            } catch (SQLException e) {
                System.err.println("Erro ao encerrar a conexão: " + e.getMessage());
            }
        }
    }

    private static void executarDemonstracao() throws SQLException {
        EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();
        EquipeManutencao equipe = daoEquipe.inserir(
                new EquipeManutencao(0, "Equipe Demonstração", "Roçada mecanizada", true));
        System.out.println("Equipe inserida: " + equipe);
        System.out.println("Equipe encontrada: " + daoEquipe.buscarPorId(equipe.id()).orElseThrow());
        equipe = new EquipeManutencao(
                equipe.id(), equipe.nome(), "Roçada e pulverização", equipe.ativa());
        System.out.println("Equipe atualizada: " + daoEquipe.atualizar(equipe));
        System.out.println("Equipes cadastradas: " + daoEquipe.listarTodas());

        TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();
        TrechoRodoviaRegistro trecho = daoTrecho.inserir(
                new TrechoRodoviaRegistro(0, 99, 47.5, TipoClima.UMIDO, true));
        System.out.println("Trecho inserido: " + trecho);
        System.out.println("Trecho encontrado: " + daoTrecho.buscarPorId(trecho.id()).orElseThrow());
        trecho = new TrechoRodoviaRegistro(
                trecho.id(), trecho.km(), 52.0, trecho.clima(), trecho.monitorado());
        System.out.println("Trecho atualizado: " + daoTrecho.atualizar(trecho));
        System.out.println("Trechos cadastrados: " + daoTrecho.listarTodas());

        IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();
        IntervencaoOperacionalRegistro intervencao = daoIntervencao.inserir(
                new IntervencaoOperacionalRegistro(
                        0, trecho.id(), equipe.id(), "Roçada Mecanizada",
                        "Intervenção criada pelo Main demonstrativo.",
                        LocalDate.now().plusDays(1), StatusIntervencao.PLANEJADA));
        System.out.println("Intervenção inserida: " + intervencao);
        System.out.println("Intervenção encontrada: "
                + daoIntervencao.buscarPorId(intervencao.id()).orElseThrow());
        intervencao = new IntervencaoOperacionalRegistro(
                intervencao.id(), intervencao.trechoId(), intervencao.equipeId(),
                intervencao.tipo(), intervencao.descricao(), intervencao.dataAgendada(),
                StatusIntervencao.EM_ANDAMENTO);
        System.out.println("Intervenção atualizada: " + daoIntervencao.atualizar(intervencao));
        System.out.println("Intervenções cadastradas: " + daoIntervencao.listarTodas());
        System.out.println("Intervenção deletada: " + daoIntervencao.deletar(intervencao.id()));

        TrechoRodovia[] trechos = daoTrecho.listarTodas().stream()
                .map(TrechoRodoviaRegistro::paraModelo)
                .toArray(TrechoRodovia[]::new);
        new GeradorRelatorio().gerarRelatorio(trechos);

        RelatorioPrioridadeDAO daoRelatorio = new RelatorioPrioridadeDAO();
        System.out.println("Histórico de relatórios:");
        daoRelatorio.listarTodas().forEach(System.out::println);

        System.out.println("Trecho demonstrativo deletado: " + daoTrecho.deletar(trecho.id()));
        System.out.println("Equipe demonstrativa deletada: " + daoEquipe.deletar(equipe.id()));
    }
}
