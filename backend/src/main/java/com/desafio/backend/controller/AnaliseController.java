package com.desafio.backend.controller;

import com.desafio.backend.dto.ResultadoValidacao;
import com.desafio.backend.service.CsvService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/analises")
public class AnaliseController {

    private final CsvService csvService;

    public AnaliseController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping
    public ResponseEntity<?> analisar(
            @RequestParam("arquivo") MultipartFile arquivo
    ) throws IOException {

        try {
            ResultadoValidacao resultado =
                    csvService.validar(arquivo);

            return ResponseEntity.ok(Map.of(
                    "resumo", Map.of(
                            "linhasLidas", resultado.linhasLidas(),
                            "linhasValidas", resultado.leituras().size(),
                            "linhasInvalidas",
                            resultado.linhasInvalidas().size()
                    ),
                    "linhasInvalidas",
                    resultado.linhasInvalidas()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }
}
