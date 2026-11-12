package com.desafio.backend.service;

import com.desafio.backend.dto.ResultadoValidacao;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class CsvServiceTest {

    private final CsvService service = new CsvService();

    private MockMultipartFile criarArquivo(String conteudo) {
        return new MockMultipartFile(
                "arquivo",
                "leituras.csv",
                "text/csv",
                conteudo.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void deveAceitarCsvValido() throws Exception {

        String csv = """
                placa;dataHora;velocidadeKmh;limiteKmh
                ABC1D23;2026-10-05T08:00:00;70;60
                DEF2G34;2026-10-05T09:00:00;80;60
                """;

        ResultadoValidacao resultado =
                service.validar(criarArquivo(csv));

        assertEquals(2, resultado.linhasLidas());
        assertEquals(2, resultado.leituras().size());
        assertTrue(resultado.linhasInvalidas().isEmpty());
    }

    @Test
    void deveIgnorarLinhaInvalidaSemInterromperProcessamento()
            throws Exception {

        String csv = """
                placa;dataHora;velocidadeKmh;limiteKmh
                ABC1D23;2026-10-05T08:00:00;70;60
                DEF2G34;data-invalida;80;60
                GHI3J45;2026-10-05T09:00:00;90;60
                """;

        ResultadoValidacao resultado =
                service.validar(criarArquivo(csv));

        assertEquals(3, resultado.linhasLidas());
        assertEquals(2, resultado.leituras().size());
        assertEquals(1, resultado.linhasInvalidas().size());
        assertEquals(3, resultado.linhasInvalidas().get(0).linha());
    }

    @Test
    void deveAceitarVirgulaDecimalENormalizarPlaca()
            throws Exception {

        String csv = """
                placa;dataHora;velocidadeKmh;limiteKmh
                abc1d23;05/10/2026 08:00:00;72,5;60
                """;

        ResultadoValidacao resultado =
                service.validar(criarArquivo(csv));

        assertEquals(1, resultado.leituras().size());
        assertEquals(
                "ABC1D23",
                resultado.leituras().get(0).placa()
        );
        assertEquals(
                0,
                resultado.leituras().get(0).velocidadeKmh()
                        .compareTo(new java.math.BigDecimal("72.5"))
        );
    }

    @Test
    void deveRejeitarCabecalhoInvalido() {

        String csv = """
                placa;velocidade
                ABC1D23;70
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> service.validar(criarArquivo(csv))
        );
    }

    @Test
    void deveRejeitarArquivoVazio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.validar(criarArquivo(""))
        );
    }
}
