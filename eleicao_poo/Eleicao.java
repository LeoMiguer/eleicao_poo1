
package eleicaopoo;

import java.util.*;

public class eleicaopoo {
    public static void main(String[] args) {
        String caminhoArquivo = "C:\\Users\\leomi\\eclipse-workspace\\eleicao_poo\\src\\eleicao_poo\\votos.txt";
        String caminhoSaida = "C:\\Users\\leomi\\eclipse-workspace\\eleicao_poo\\src\\eleicao_poo\\resultado_votos.txt";

        ProcessadorArquivo processador = new ProcessadorArquivo();
        List<Voto> votos = processador.carregarVotos(caminhoArquivo);

        // Mapas de contagem
        Map<String, Integer> contagemVotos = new HashMap<>();
        Map<String, Partido> partidos = new HashMap<>();
        Resultado resultado = new Resultado();

        for (Voto voto : votos) {
            String numeroCandidato = voto.getNumeroCandidato();
            resultado.incrementarVoto(voto.isNulo() ? "nulo" : voto.isBranco() ? "branco" : "valido");

            if (!voto.isNulo() && !voto.isBranco()) {
                contagemVotos.put(numeroCandidato, contagemVotos.getOrDefault(numeroCandidato, 0) + 1);

                // Extrair partido
                String partidoNumero = numeroCandidato.substring(0, 2);
                Partido partido = partidos.getOrDefault(partidoNumero, new Partido(partidoNumero, "Partido " + partidoNumero));
                partido.adicionarCandidato(new Candidato(numeroCandidato, "Candidato " + numeroCandidato));
                partidos.put(partidoNumero, partido);
            }
        }

        // Calculando o Quociente Eleitoral (QE)
        double quocienteEleitoral = (double) resultado.getVotosValidos() / 10;
        System.out.println("Quociente Eleitoral: " + quocienteEleitoral);

        // Calculando o Quociente Partidário (QP)
        for (Partido partido : partidos.values()) {
            double quocientePartido = (double) partido.getTotalVotos() / quocienteEleitoral;
            resultado.adicionarQuocientePartidario(partido.getNumero(), quocientePartido);
        }

        // Definindo os eleitos
        for (Partido partido : partidos.values()) {
            partido.getCandidatos().sort(Comparator.comparingInt(Candidato::getVotos).reversed());
            for (int i = 0; i < 10 && i < partido.getCandidatos().size(); i++) {
                Candidato candidato = partido.getCandidatos().get(i);
                if (candidato.getVotos() >= quocienteEleitoral * 0.1) {
                    resultado.adicionarCandidatoEleito(candidato);
                }
            }
        }

        // Gerando resumo
        resultado.gerarResumo();
    }
}