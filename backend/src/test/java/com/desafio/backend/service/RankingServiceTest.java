package com.desafio.backend.service;

import com.desafio.backend.dto.Episodio;
import com.desafio.backend.dto.RankingVeiculo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RankingServiceTest {

    private final RankingService service =
            new RankingService();

    @Test
    void deveOrdenarPorPontosEDesempatarPorVelocidade() {

        LocalDateTime data =
                LocalDateTime.of(2026, 10, 5, 8, 0);

        Episodio primeiro = new Episodio(
                "AAA1B23", data, data, 0,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(30),
                "GRAVE", 3
        );

        Episodio segundo = new Episodio(
                "BBB2C34", data, data, 0,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(30),
                "GRAVE", 3
        );

        Episodio terceiro = new Episodio(
                "CCC3D45", data, data, 0,
                BigDecimal.valueOf(110),
                BigDecimal.valueOf(60),
                "GRAVISSIMA", 5
        );

        List<RankingVeiculo> ranking =
                service.gerarRanking(
                        List.of(primeiro, segundo, terceiro)
                );

        assertEquals(3, ranking.size());

        assertEquals("CCC3D45", ranking.get(0).placa());
        assertEquals("BBB2C34", ranking.get(1).placa());
        assertEquals("AAA1B23", ranking.get(2).placa());
    }

    @Test
    void deveDesempatarPorPlacaEmOrdemAlfabetica() {

        LocalDateTime data =
                LocalDateTime.of(2026, 10, 5, 8, 0);

        Episodio primeiro = new Episodio(
                "BBB2C34", data, data, 0,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(30),
                "GRAVE", 3
        );

        Episodio segundo = new Episodio(
                "AAA1B23", data, data, 0,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(30),
                "GRAVE", 3
        );

        List<RankingVeiculo> ranking =
                service.gerarRanking(
                        List.of(primeiro, segundo)
                );

        assertEquals("AAA1B23", ranking.get(0).placa());
        assertEquals("BBB2C34", ranking.get(1).placa());
    }
}
