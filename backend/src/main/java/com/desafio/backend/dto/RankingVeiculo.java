package com.desafio.backend.dto;

import java.math.BigDecimal;

public record RankingVeiculo(String placa, int totalPontos, BigDecimal velocidadeMaxima, long totalEpisodios) {
    
}
