package com.desafio.backend.service;

import com.desafio.backend.dto.Episodio;
import com.desafio.backend.dto.Leitura;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpisodioServiceTest {

    private final EpisodioService service =
            new EpisodioService();

    @Test
    void deveIgnorarVelocidadeDentroDaTolerancia() {

        Leitura leitura = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 0),
                BigDecimal.valueOf(65),
                BigDecimal.valueOf(60)
        );

        List<Episodio> episodios =
                service.identificar(List.of(leitura));

        assertTrue(episodios.isEmpty());
    }

    @Test
    void deveAgruparLeiturasComIntervaloDeCincoMinutos() {

        Leitura primeira = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 0),
                BigDecimal.valueOf(70),
                BigDecimal.valueOf(60)
        );

        Leitura segunda = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 5),
                BigDecimal.valueOf(75),
                BigDecimal.valueOf(60)
        );

        List<Episodio> episodios =
                service.identificar(List.of(primeira, segunda));

        assertEquals(1, episodios.size());
        assertEquals(300, episodios.get(0).duracaoSegundos());
    }

    @Test
    void deveSepararEpisodiosComIntervaloMaiorQueCincoMinutos() {

        Leitura primeira = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 0),
                BigDecimal.valueOf(70),
                BigDecimal.valueOf(60)
        );

        Leitura segunda = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 5, 1),
                BigDecimal.valueOf(75),
                BigDecimal.valueOf(60)
        );

        List<Episodio> episodios =
                service.identificar(List.of(primeira, segunda));

        assertEquals(2, episodios.size());
    }

    @Test
    void deveClassificarExcessoDeVintePorCentoComoLeve() {

        Leitura leitura = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 0),
                BigDecimal.valueOf(72),
                BigDecimal.valueOf(60)
        );

        List<Episodio> episodios =
                service.identificar(List.of(leitura));

        assertEquals(1, episodios.size());
        assertEquals("LEVE", episodios.get(0).gravidade());
        assertEquals(1, episodios.get(0).pontos());
    }

    @Test
    void deveClassificarExcessoDeCinquentaPorCentoComoGrave() {

        Leitura leitura = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 0),
                BigDecimal.valueOf(90),
                BigDecimal.valueOf(60)
        );

        List<Episodio> episodios =
                service.identificar(List.of(leitura));

        assertEquals(1, episodios.size());
        assertEquals("GRAVE", episodios.get(0).gravidade());
        assertEquals(3, episodios.get(0).pontos());
    }

    @Test
    void deveClassificarExcessoAcimaDeCinquentaPorCentoComoGravissimo() {

        Leitura leitura = new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, 0),
                BigDecimal.valueOf(91),
                BigDecimal.valueOf(60)
        );

        List<Episodio> episodios =
                service.identificar(List.of(leitura));

        assertEquals(1, episodios.size());
        assertEquals("GRAVISSIMA", episodios.get(0).gravidade());
        assertEquals(5, episodios.get(0).pontos());
    }
}
