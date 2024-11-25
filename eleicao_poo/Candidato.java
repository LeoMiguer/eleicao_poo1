package eleicao_poo;

public class Candidato {
    private String numero;
    private String nome;
    private int votos;

    public Candidato(String numero, String nome) {
        this.numero = numero;
        this.nome = nome;
        this.votos = 0;
    }

    public void adicionarVoto() {
        this.votos++;
    }

    public String getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public int getVotos() {
        return votos;
    }
}
