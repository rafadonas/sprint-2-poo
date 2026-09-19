package service;

import dao.RelatorioPrioridadeDAO;
import model.RelatorioPrioridade;
import model.TipoClima;
import model.TrechoRodovia;

import java.time.LocalDateTime;

/** Teste unitário simples, sem dependências externas e sem acesso ao Oracle. */
public final class GeradorRelatorioTest {
    private GeradorRelatorioTest() {
    }

    public static void main(String[] args) throws Exception {
        RelatorioDAOFake dao = new RelatorioDAOFake();
        GeradorRelatorio gerador = new GeradorRelatorio(dao);
        TrechoRodovia[] trechos = {
            new TrechoRodovia(1, 51.0, TipoClima.NORMAL),
            new TrechoRodovia(2, 41.0, TipoClima.NORMAL),
            new TrechoRodovia(3, 31.0, TipoClima.NORMAL),
            new TrechoRodovia(4, 30.0, TipoClima.NORMAL)
        };

        gerador.gerarRelatorio(trechos);

        assert dao.salvo : "O relatório deveria ser salvo.";
        assert dao.urgente == 1 : "A quantidade urgente está incorreta.";
        assert dao.critica == 1 : "A quantidade crítica está incorreta.";
        assert dao.atencao == 1 : "A quantidade em atenção está incorreta.";
        assert dao.normal == 1 : "A quantidade normal está incorreta.";
        assert dao.resumo.contains("Total: 4") : "O resumo deveria informar o total.";
        System.out.println("GeradorRelatorioTest: todos os testes passaram.");
    }

    private static final class RelatorioDAOFake extends RelatorioPrioridadeDAO {
        private boolean salvo;
        private int urgente;
        private int critica;
        private int atencao;
        private int normal;
        private String resumo;

        @Override
        public RelatorioPrioridade salvarRelatorio(int quantidadeUrgente, int quantidadeCritica,
                                                    int quantidadeAtencao, int quantidadeNormal,
                                                    String resumoRecebido) {
            salvo = true;
            urgente = quantidadeUrgente;
            critica = quantidadeCritica;
            atencao = quantidadeAtencao;
            normal = quantidadeNormal;
            resumo = resumoRecebido;
            return new RelatorioPrioridade(
                    1, LocalDateTime.now(), urgente, critica, atencao, normal, resumo);
        }
    }
}
