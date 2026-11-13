package com.desafio.backend.service;

import com.desafio.backend.dto.Episodio;
import com.desafio.backend.dto.Leitura;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpisodioServiceCasosExtrasTest {

    private final EpisodioService service =
            new EpisodioService();

    private Leitura leitura(
            int minuto, String velocidade, String limite) {

        return new Leitura(
                "ABC1D23",
                LocalDateTime.of(2026, 10, 5, 8, minuto),
                new BigDecimal(velocidade),
                new BigDecimal(limite)
        );
    }

    @Test
    void deveOrdenarLeiturasAntesDeAgrupar() {

        List<Episodio> episodios = service.identificar(
                List.of(
                        leitura(4, "72", "60"),
                        leitura(0, "70", "60"),
                        leitura(2, "75", "60")
                )
        );

        assertEquals(1, episodios.size());
        assertEquals(240, episodios.get(0).duracaoSegundos());
    }

    @Test
    void leituraSemExcessoInterrompeEpisodioMesmoDentroDeCincoMinutos() {

        List<Episodio> episodios = service.identificar(
                List.of(
                        leitura(0, "70", "60"),
                        leitura(1, "65", "60"),
                        leitura(2, "75", "60")
                )
        );

        assertEquals(2, episodios.size());
    }

    @Test
    void maiorPercentualNemSempreCorrespondeAMaiorVelocidade() {

        List<Episodio> episodios = service.identificar(
                List.of(
                        leitura(0, "80", "60"),
                        leitura(1, "62", "40")
                )
        );

        assertEquals(1, episodios.size());

        Episodio episodio = episodios.get(0);

        assertEquals(
                0,
                episodio.velocidadeMaxima()
                        .compareTo(new BigDecimal("80"))
        );

        assertEquals(
                0,
                episodio.excessoMaximoPercentual()
                        .compareTo(new BigDecimal("55.0"))
        );

        assertEquals("GRAVISSIMA", episodio.gravidade());
    }
}
