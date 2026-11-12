package com.desafio.backend.service;

import com.desafio.backend.dto.Episodio;
import com.desafio.backend.dto.RankingVeiculo;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankingService {

    public List<RankingVeiculo> gerarRanking(
            List<Episodio> episodios) {

        Map<String, List<Episodio>> porVeiculo =
                episodios.stream()
                        .collect(Collectors.groupingBy(
                                Episodio::placa
                        ));

        List<RankingVeiculo> ranking = porVeiculo.entrySet()
                .stream()
                .map(entrada -> {

                    String placa = entrada.getKey();
                    List<Episodio> episodiosVeiculo =
                            entrada.getValue();

                    int totalPontos = episodiosVeiculo.stream()
                            .mapToInt(Episodio::pontos)
                            .sum();

                    BigDecimal velocidadeMaxima =
                            episodiosVeiculo.stream()
                                    .map(Episodio::velocidadeMaxima)
                                    .max(BigDecimal::compareTo)
                                    .orElse(BigDecimal.ZERO);

                    return new RankingVeiculo(
                            placa,
                            totalPontos,
                            velocidadeMaxima,
                            episodiosVeiculo.size()
                    );
                })
                .collect(Collectors.toList());

        ranking.sort(
                Comparator.comparingInt(
                        RankingVeiculo::totalPontos
                ).reversed()
                .thenComparing(
                        RankingVeiculo::velocidadeMaxima,
                        Comparator.reverseOrder()
                )
                .thenComparing(RankingVeiculo::placa)
        );

        return ranking;
    }
}
