package eleicao_poo;

public class Voto {
	private String numeroCandidato;
    private boolean nulo;
    private boolean branco;

    public Voto(String numeroCandidato) {
        this.numeroCandidato = numeroCandidato;
        this.nulo = "0".equals(numeroCandidato);
        this.branco = "1".equals(numeroCandidato);
    }

    public String getNumeroCandidato() {
        return numeroCandidato;
    }

    public boolean isNulo() {
        return nulo;
    }

    public boolean isBranco() {
        return branco;
    }
}
