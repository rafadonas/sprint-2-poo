/**
 * Versão tecnológica do trecho, que implementa monitoramento automático.
 */
public class TrechoRodoviaMonitorado extends TrechoRodovia implements MonitoravelViaIoT {
    public TrechoRodoviaMonitorado(int km, double alturaVegetacao, TipoClima clima) {
        super(km, alturaVegetacao, clima);
    }

    @Override
    public void transmitirDadosSensor() {
        System.out.println("[IoT] KM " + getKm() + ": Transmitindo dados...");
    }
}
