package com.desafio.backend.service;

import com.desafio.backend.dto.Leitura;
import com.desafio.backend.dto.LinhaInvalida;
import com.desafio.backend.dto.ResultadoValidacao;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CsvService {

    private static final String CABECALHO =
            "placa;dataHora;velocidadeKmh;limiteKmh";

    private static final DateTimeFormatter DATA_BR =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss")
                    .withResolverStyle(
                            java.time.format.ResolverStyle.STRICT
                    );

    public ResultadoValidacao validar(MultipartFile arquivo)
            throws IOException {

        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "O arquivo está vazio."
            );
        }

        List<Leitura> leituras = new ArrayList<>();
        List<LinhaInvalida> invalidas = new ArrayList<>();

        int linhasLidas = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        arquivo.getInputStream(),
                        StandardCharsets.UTF_8
                ))) {

            String cabecalho = reader.readLine();

            if (cabecalho == null ||
                    !cabecalho.replace("\uFEFF", "")
                            .trim().equals(CABECALHO)) {

                throw new IllegalArgumentException(
                        "Cabeçalho CSV inválido."
                );
            }

            String linha;
            int numeroLinha = 1;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                linhasLidas++;

                try {
                    leituras.add(processarLinha(linha));
                } catch (IllegalArgumentException e) {
                    invalidas.add(new LinhaInvalida(
                            numeroLinha,
                            e.getMessage()
                    ));
                }
            }
        }

        return new ResultadoValidacao(
                linhasLidas,
                leituras,
                invalidas
        );
    }

    private Leitura processarLinha(String linha) {

        if (linha.isBlank()) {
            throw new IllegalArgumentException(
                    "Linha em branco."
            );
        }

        String[] campos = linha.split(";", -1);

        if (campos.length != 4) {
            throw new IllegalArgumentException(
                    "Quantidade de campos inválida."
            );
        }

        String placa = campos[0].trim()
                .toUpperCase(Locale.ROOT);

        if (placa.isEmpty()) {
            throw new IllegalArgumentException(
                    "Placa não informada."
            );
        }

        LocalDateTime dataHora =
                converterData(campos[1].trim());

        BigDecimal velocidade =
                converterNumero(campos[2], "Velocidade");

        BigDecimal limite =
                converterNumero(campos[3], "Limite");

        if (velocidade.signum() < 0) {
            throw new IllegalArgumentException(
                    "Velocidade negativa."
            );
        }

        if (limite.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Limite deve ser maior que zero."
            );
        }

        return new Leitura(
                placa,
                dataHora,
                velocidade,
                limite
        );
    }

    private LocalDateTime converterData(String texto) {

        try {
            return LocalDateTime.parse(texto);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(texto, DATA_BR);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException(
                        "Data inválida."
                );
            }
        }
    }

    private BigDecimal converterNumero(
            String texto, String campo) {

        try {
            return new BigDecimal(
                    texto.trim().replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    campo + " contém um valor inválido."
            );
        }
    }
}

