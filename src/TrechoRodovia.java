/**
 * Representa um trecho da rodovia com informações de vegetação.
 */
public class TrechoRodovia {
    private int km;
    protected double alturaVegetacao;
    private TipoClima clima;

    public TrechoRodovia(int km, double alturaVegetacao, TipoClima clima) {
        this.km = km;
        this.alturaVegetacao = alturaVegetacao;
        this.clima = clima;
    }

    /**
     * Simula o crescimento da vegetação baseado no clima local.
     */
    public void crescer() {
        this.alturaVegetacao += 2.0 * clima.getFator();
    }

    public int getKm() { return km; }
    public double getAlturaVegetacao() { return alturaVegetacao; }
}
