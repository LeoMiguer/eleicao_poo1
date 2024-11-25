package eleicao_poo;

import java.io.*;
import java.util.*;

public class ProcessadorArquivo {
    public List<Voto> carregarVotos(String caminhoArquivo) {
        List<Voto> votos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    votos.add(new Voto(partes[1].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo: " + e.getMessage());
        }
        return votos;
    }
}