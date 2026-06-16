import java.util.List;

/**
 * Motor de regras que analisa os trechos e gera o relatório de prioridades.
 */
public class MotorPriorizacao {
    /**
     * Varre a lista de trechos e indica qual serviço é necessário em cada um.
     */
    public void gerarRelatorio(List<TrechoRodovia> trechos) {
        System.out.println("--- RELATÓRIO DE PRIORIDADE AUTOMÁTICO ---");
        for (TrechoRodovia t : trechos) {
            String acao = "Apenas Monitorar";
            
            if (t.getAlturaVegetacao() > 50) {
                acao = "Roçada Mecanizada (URGENTE)";
            } else if (t.getAlturaVegetacao() > 30) {
                acao = "Roçada Manual (AGENDAR)";
            }
            
            System.out.println("KM: " + t.getKm() + " | Altura: " + t.getAlturaVegetacao() + "cm | Ação: " + acao);
        }
    }
}
