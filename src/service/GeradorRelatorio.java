package service;

import dao.RelatorioPrioridadeDAO;
import model.TrechoRodovia;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;

/** Gera o relatório no console e salva seu resumo no Oracle. */
public class GeradorRelatorio {
    private static final double LIMITE_URGENTE = 50.0;
    private static final double LIMITE_CRITICO = 40.0;
    private static final double LIMITE_ATENCAO = 30.0;

    private final RelatorioPrioridadeDAO relatorioDAO;

    public GeradorRelatorio() {
        this(new RelatorioPrioridadeDAO());
    }

    public GeradorRelatorio(RelatorioPrioridadeDAO relatorioDAO) {
        this.relatorioDAO = Objects.requireNonNull(relatorioDAO);
    }

    public void gerarRelatorio(TrechoRodovia[] trechos) throws SQLException {
        Objects.requireNonNull(trechos, "A lista de trechos é obrigatória.");
        int quantidadeUrgente = 0;
        int quantidadeCritica = 0;
        int quantidadeAtencao = 0;
        int quantidadeNormal = 0;

        imprimirCabecalho();
        for (TrechoRodovia trecho : trechos) {
            Objects.requireNonNull(trecho, "A lista não pode conter trechos nulos.");
            Prioridade prioridade = classificar(trecho.getAlturaVegetacao());
            switch (prioridade) {
                case URGENTE -> quantidadeUrgente++;
                case CRITICO -> quantidadeCritica++;
                case ATENCAO -> quantidadeAtencao++;
                case NORMAL -> quantidadeNormal++;
            }

            System.out.printf(
                    Locale.ROOT,
                    "KM: %d | Altura: %.2f cm | Prioridade: %s | Ação: %s%n",
                    trecho.getKm(), trecho.getAlturaVegetacao(),
                    prioridade.rotulo, prioridade.acao);
        }

        String resumo = String.format(
                Locale.ROOT,
                "Total: %d | Urgente: %d | Crítico: %d | Atenção: %d | Normal: %d",
                trechos.length, quantidadeUrgente, quantidadeCritica,
                quantidadeAtencao, quantidadeNormal);
        System.out.println(resumo);

        relatorioDAO.salvarRelatorio(
                quantidadeUrgente, quantidadeCritica,
                quantidadeAtencao, quantidadeNormal, resumo);
        System.out.println("Relatório salvo no banco de dados.");
    }

    private void imprimirCabecalho() {
        System.out.println("--- RELATÓRIO DE PRIORIDADE AUTOMÁTICO ---");
    }

    private Prioridade classificar(double alturaVegetacao) {
        if (alturaVegetacao > LIMITE_URGENTE) {
            return Prioridade.URGENTE;
        }
        if (alturaVegetacao > LIMITE_CRITICO) {
            return Prioridade.CRITICO;
        }
        if (alturaVegetacao > LIMITE_ATENCAO) {
            return Prioridade.ATENCAO;
        }
        return Prioridade.NORMAL;
    }

    private enum Prioridade {
        URGENTE("URGENTE", "Roçada mecanizada imediata"),
        CRITICO("CRÍTICO", "Agendar roçada mecanizada"),
        ATENCAO("ATENÇÃO", "Programar vistoria e roçada manual"),
        NORMAL("NORMAL", "Apenas monitorar");

        private final String rotulo;
        private final String acao;

        Prioridade(String rotulo, String acao) {
            this.rotulo = rotulo;
            this.acao = acao;
        }
    }
}
