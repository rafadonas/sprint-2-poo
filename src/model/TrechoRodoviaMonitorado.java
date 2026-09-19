package model;

/** Versão do trecho que possui monitoramento automático por IoT. */
public class TrechoRodoviaMonitorado extends TrechoRodovia implements MonitoravelViaIoT {
    public TrechoRodoviaMonitorado(int km, double alturaVegetacao, TipoClima clima) {
        this(0, km, alturaVegetacao, clima);
    }

    public TrechoRodoviaMonitorado(long id, int km, double alturaVegetacao, TipoClima clima) {
        super(id, km, alturaVegetacao, clima, true);
    }

    @Override
    public void transmitirDadosSensor() {
        System.out.println("[IoT] KM " + getKm() + ": transmitindo dados...");
    }
}
