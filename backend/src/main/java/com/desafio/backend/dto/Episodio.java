package com.desafio.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Episodio(String placa, LocalDateTime inicio, LocalDateTime fim, long duracaoSegundos, BigDecimal velocidadeMaxima, BigDecimal excessoMaximoPercentual, String gravidade, int pontos) {
    
}