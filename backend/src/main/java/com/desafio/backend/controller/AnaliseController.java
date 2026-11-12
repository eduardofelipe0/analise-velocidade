package com.desafio.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/analises")
public class AnaliseController {

    @PostMapping
    public ResponseEntity<?> analisar(
            @RequestParam("arquivo") MultipartFile arquivo
    ) throws IOException {

        if (arquivo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O arquivo está vazio."));
        }

        String conteudo = new String(
                arquivo.getBytes(),
                StandardCharsets.UTF_8
        );

        String cabecalhoEsperado =
                "placa;dataHora;velocidadeKmh;limiteKmh";

        String[] linhas = conteudo
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .split("\n");

        String cabecalho = linhas[0].replace("\uFEFF", "");

        if (!cabecalhoEsperado.equals(cabecalho.trim())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Cabeçalho CSV inválido."));
        }

        return ResponseEntity.ok(Map.of(
                "mensagem", "Arquivo recebido com sucesso!",
                "linhasRecebidas", linhas.length - 1
        ));
    }
}
