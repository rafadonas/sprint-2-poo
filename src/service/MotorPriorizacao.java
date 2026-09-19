package service;

import model.TrechoRodovia;

import java.sql.SQLException;
import java.util.List;

/** Fachada de compatibilidade com o motor criado na Sprint 2. */
public class MotorPriorizacao {
    private final GeradorRelatorio geradorRelatorio = new GeradorRelatorio();

    public void gerarRelatorio(List<TrechoRodovia> trechos) throws SQLException {
        geradorRelatorio.gerarRelatorio(trechos.toArray(TrechoRodovia[]::new));
    }
}
