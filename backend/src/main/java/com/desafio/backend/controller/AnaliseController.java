package com.desafio.backend.controller;

import com.desafio.backend.dto.ResultadoValidacao;
import com.desafio.backend.service.CsvService;
import com.desafio.backend.service.EpisodioService;
import com.desafio.backend.dto.Episodio;
import com.desafio.backend.dto.RankingVeiculo;
import com.desafio.backend.service.RankingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/analises")
public class AnaliseController {

    private final CsvService csvService;
    private final EpisodioService episodioService;
    private final RankingService rankingService;

    public AnaliseController(CsvService csvService, EpisodioService episodioService, RankingService rankingService) {
        this.csvService = csvService;
        this.episodioService = episodioService;
        this.rankingService = rankingService;
    }

    @PostMapping
    public ResponseEntity<?> analisar(@RequestParam("arquivo") MultipartFile arquivo) throws IOException {

        try {
            // 1. Validar as leituras do CSV
            ResultadoValidacao resultado =
                    csvService.validar(arquivo);

            // 2. Identificar os episódios
            List<Episodio> episodios =
                    episodioService.identificar(
                            resultado.leituras()
                    );

            // 3. Gerar o ranking de veículos
            List<RankingVeiculo> ranking =
                    rankingService.gerarRanking(episodios);

            // 4. Construir a resposta da API
            return ResponseEntity.ok(Map.of(
                    "resumo", Map.of(
                            "linhasLidas", resultado.linhasLidas(),
                            "linhasValidas",
                            resultado.leituras().size(),
                            "linhasInvalidas",
                            resultado.linhasInvalidas().size(),
                            "totalEpisodios", episodios.size()
                    ),
                    "episodios", episodios,
                    "ranking", ranking,
                    "linhasInvalidas",
                    resultado.linhasInvalidas()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "erro", e.getMessage()
                    ));
        }
    }
}
