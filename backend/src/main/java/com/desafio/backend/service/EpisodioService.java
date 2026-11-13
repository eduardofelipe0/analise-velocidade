package com.desafio.backend.service;

import com.desafio.backend.dto.Episodio;
import com.desafio.backend.dto.Leitura;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EpisodioService {

    private static final BigDecimal TOLERANCIA =
            BigDecimal.valueOf(5);

    public List<Episodio> identificar(List<Leitura> leituras) {

        Map<String, List<Leitura>> porVeiculo =
                leituras.stream()
                        .collect(Collectors.groupingBy(
                                Leitura::placa
                        ));

        List<Episodio> episodios = new ArrayList<>();

        for (List<Leitura> grupo : porVeiculo.values()) {

            grupo.sort(Comparator.comparing(
                    Leitura::dataHora
            ));

            List<Leitura> episodioAtual = new ArrayList<>();

            for (Leitura leitura : grupo) {

                boolean excesso = leitura.velocidadeKmh()
                        .compareTo(
                                leitura.limiteKmh().add(TOLERANCIA)
                        ) > 0;

                if (!excesso) {
                    finalizar(episodioAtual, episodios);
                    episodioAtual.clear();
                    continue;
                }

                if (!episodioAtual.isEmpty()) {

                    Leitura anterior = episodioAtual.get(
                            episodioAtual.size() - 1
                    );

                    long intervalo = Duration.between(
                            anterior.dataHora(),
                            leitura.dataHora()
                    ).getSeconds();

                    if (intervalo > 300) {
                        finalizar(episodioAtual, episodios);
                        episodioAtual.clear();
                    }
                }

                episodioAtual.add(leitura);
            }

            finalizar(episodioAtual, episodios);
        }

        episodios.sort(
                Comparator.comparing(Episodio::inicio)
                        .thenComparing(Episodio::placa)
        );

        return episodios;
    }

    private void finalizar(
            List<Leitura> leituras,
            List<Episodio> episodios) {

        if (leituras.isEmpty()) {
            return;
        }

        Leitura primeira = leituras.get(0);
        Leitura ultima = leituras.get(leituras.size() - 1);

        BigDecimal velocidadeMaxima = BigDecimal.ZERO;
        BigDecimal excessoMaximo = BigDecimal.ZERO;

        for (Leitura leitura : leituras) {

            velocidadeMaxima = velocidadeMaxima.max(
                    leitura.velocidadeKmh()
            );

            BigDecimal percentual = leitura.velocidadeKmh()
                .subtract(leitura.limiteKmh())
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        leitura.limiteKmh(),
                        1,
                        RoundingMode.HALF_UP
                );

            excessoMaximo = excessoMaximo.max(percentual);
        }

        String gravidade;
        int pontos;

        if (excessoMaximo.compareTo(
                BigDecimal.valueOf(20)) <= 0) {

            gravidade = "LEVE";
            pontos = 1;

        } else if (excessoMaximo.compareTo(
                BigDecimal.valueOf(50)) <= 0) {

            gravidade = "GRAVE";
            pontos = 3;

        } else {
            gravidade = "GRAVISSIMA";
            pontos = 5;
        }

        long duracao = Duration.between(
                primeira.dataHora(),
                ultima.dataHora()
        ).getSeconds();

        episodios.add(new Episodio(
                primeira.placa(),
                primeira.dataHora(),
                ultima.dataHora(),
                duracao,
                velocidadeMaxima,
                excessoMaximo.setScale(
                        1, RoundingMode.HALF_UP
                ),
                gravidade,
                pontos
        ));
    }
}

