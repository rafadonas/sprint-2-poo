import java.util.Arrays;
import java.util.List;

/**
 * Classe principal para execução do sistema.
 */
public class Main {
    public static void main(String[] args) {
        // Criando alguns trechos de exemplo
        List<TrechoRodovia> trechos = Arrays.asList(
            new TrechoRodovia(10, 20.0, TipoClima.NORMAL),
            new TrechoRodovia(15, 45.0, TipoClima.SECO),
            new TrechoRodoviaMonitorado(22, 55.0, TipoClima.UMIDO)
        );

        // Simulando o crescimento da vegetação
        for (TrechoRodovia t : trechos) {
            t.crescer();
        }

        // Gerando o relatório de prioridade
        MotorPriorizacao motor = new MotorPriorizacao();
        motor.gerarRelatorio(trechos);
    }
}
