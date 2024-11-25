package eleicao_poo;

import java.io.*;
import java.util.*;

public class Eleicao {
    public static void main(String[] args) {
        String caminhoArquivo = "C:\\Users\\leomi\\eclipse-workspace\\eleicao_poo\\src\\eleicao_poo\\votos.txt";
        String caminhoSaida = "C:\\Users\\leomi\\eclipse-workspace\\eleicao_poo\\src\\eleicao_poo\\resultado_votos.txt";

        Map<String, Integer> contagemVotos = new HashMap<>();
        Map<String, Integer> votosPorPartido = new HashMap<>();
        Map<String, List<String>> candidatosPorPartido = new HashMap<>();
        int votosTotais = 0;
        int votosValidos = 0;
        int votosNulos = 0;
        int votosBrancos = 0;
        int numeroCadeiras = 10;

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    String numeroCandidato = partes[1].trim();

                    votosTotais++;

                    if (numeroCandidato.equals("0")) {
                        votosNulos++;
                    } else if (numeroCandidato.equals("1")) {
                        votosBrancos++;
                    } else {
                        votosValidos++;
                        contagemVotos.put(numeroCandidato, contagemVotos.getOrDefault(numeroCandidato, 0) + 1);

                        // Extrair partido (assume os dois primeiros dígitos como identificador)
                        // Separa o txt do ";", separando o partido do candidato
                        String partido = numeroCandidato.substring(0, 2);
                        votosPorPartido.put(partido, votosPorPartido.getOrDefault(partido, 0) + 1);

                        // Associa o candidato ao partido (list)
                        candidatosPorPartido.putIfAbsent(partido, new ArrayList<>());
                        candidatosPorPartido.get(partido).add(numeroCandidato);
                    }
                }
            }

            //Quociente Eleitoral (QE)
            double quocienteEleitoral = (double) votosValidos / numeroCadeiras;
            double limiteDesempenho = quocienteEleitoral * 0.1;

            //Quociente Partidário (QP)
            Map<String, Integer> cadeirasPorPartido = new HashMap<>();
            for (Map.Entry<String, Integer> entrada : votosPorPartido.entrySet()) {
                String partido = entrada.getKey();
                int votosDoPartido = entrada.getValue();
                int cadeiras = (int) (votosDoPartido / quocienteEleitoral);
                cadeirasPorPartido.put(partido, cadeiras);
            }

            // Cadeiras para canditados mais votados
            List<String> todosCandidatosOrdenados = new ArrayList<>(contagemVotos.keySet());
            todosCandidatosOrdenados.sort((c1, c2) -> contagemVotos.get(c2).compareTo(contagemVotos.get(c1)));

            List<String> eleitos = new ArrayList<>();
            Map<String, List<String>> eleitosPorPartido = new HashMap<>();
            for (Map.Entry<String, Integer> entrada : cadeirasPorPartido.entrySet()) {
                String partido = entrada.getKey();
                int cadeiras = entrada.getValue();

                if (candidatosPorPartido.containsKey(partido)) {
                    // Ordenando Candidatos do partido por votos
                    List<String> candidatos = new ArrayList<>(candidatosPorPartido.get(partido));
                    candidatos.sort((c1, c2) -> contagemVotos.get(c2).compareTo(contagemVotos.get(c1)));

                    // Alocar os mais votados até preencher as cadeiras
                    // (Definindo os mais votados às cadeiras)
                    List<String> eleitosPartido = new ArrayList<>();
                    for (String candidato : candidatos) {
                        if (eleitosPartido.size() < cadeiras &&
                                contagemVotos.get(candidato) >= limiteDesempenho) {
                            eleitosPartido.add(candidato);
                            eleitos.add(candidato);
                        }
                    }
                    eleitosPorPartido.put(partido, eleitosPartido);
                }
            }

            // Resultados (resultado_votos.txt)
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoSaida))) {
                // 1. Cálculo do Quociente Eleitoral (QE)
                bw.write("1. Quociente Eleitoral (QE): " + String.format("%.2f", quocienteEleitoral) + "\n");

                // 2. Cálculo do Quociente Partidário (QP)
                bw.write("\n"+"2. Quociente Partidário (QP):\n");
                for (String partido : votosPorPartido.keySet()) {
                    int votosDoPartido = votosPorPartido.getOrDefault(partido, 0);
                    double quocientePartidario = votosDoPartido / quocienteEleitoral;
                    bw.write("   Partido " + partido + ": " + String.format("%.2f", quocientePartidario) + " cadeiras\n");
                }

                // 3. Resumo de votos por urna, total de votos, votos por candidato, brancos e nulos
                bw.write("\n"+"3. Resumo:\n");
                bw.write("   Votos válidos: " + votosValidos + "\n");
                bw.write("   Votos nulos: " + votosNulos + "\n");
                bw.write("   Votos em branco: " + votosBrancos + "\n");

                // 4. Total de votos
                bw.write("\n"+"4. Total de votos: " + votosTotais + "\n");

                // 5. Total e percentual de votos em branco
                bw.write("\n"+"5. Votos em branco: " + votosBrancos + " (" +
                        String.format("%.2f", ((double) votosBrancos / votosTotais) * 100) + "%)\n");

                // 6. Total e percentual de votos nulos
                bw.write("\n"+"6. Votos nulos: " + votosNulos + " (" +
                        String.format("%.2f", ((double) votosNulos / votosTotais) * 100) + "%)\n");

                // 7. Total e percentual de votos válidos
                bw.write("\n"+"7. Votos válidos: " + votosValidos + " (" +
                        String.format("%.2f", ((double) votosValidos / votosTotais) * 100) + "%)\n");

                // 8. Votos totais e percentuais por legenda
                bw.write("\n"+"8. Votos totais e percentuais por legenda:\n");
                for (String partido : votosPorPartido.keySet()) {
                    int votosDoPartido = votosPorPartido.getOrDefault(partido, 0);
                    bw.write("   Partido " + partido + ": " + votosDoPartido + " (" +
                            String.format("%.2f", ((double) votosDoPartido / votosTotais) * 100) + "%)\n");
                }

                // 9. Lista dos eleitos e demais candidatos
                bw.write("\n"+"9. Lista dos eleitos:\n");
                for (int i = 0; i < Math.min(10, eleitos.size()); i++) {
                    String candidato = eleitos.get(i);
                    bw.write("   Candidato " + candidato + " com " + contagemVotos.get(candidato) + " votos\n");
                }

                bw.write("   Outros Canditados:\n");
                for (String candidato : todosCandidatosOrdenados) {
                    if (!eleitos.contains(candidato)) {
                        bw.write("   Candidato " + candidato + " com " + contagemVotos.get(candidato) + " votos\n");
                    }
                }
            }

            System.out.println("Contagem de votos concluída! Resultados salvos em: " + caminhoSaida);

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }
    }
}