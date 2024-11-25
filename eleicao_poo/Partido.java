package eleicao_poo;

import java.util.*;

public class Partido {
    private String numero;
    private String nome;
    private List<Candidato> candidatos;

    public Partido(String numero, String nome) {
        this.numero = numero;
        this.nome = nome;
        this.candidatos = new ArrayList<>();
    }

    public void adicionarCandidato(Candidato candidato) {
        candidatos.add(candidato);
    }

    public String getNumero() {
        return numero;
    }

    public List<Candidato> getCandidatos() {
        return candidatos;
    }

    public int getTotalVotos() {
        int totalVotos = 0;
        for (Candidato candidato : candidatos) {
            totalVotos += candidato.getVotos();
        }
        return totalVotos;
    }

    public String getNome() {
        return nome;
    }
}