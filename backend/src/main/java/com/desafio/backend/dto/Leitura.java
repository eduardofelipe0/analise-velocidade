package com.desafio.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Leitura(String placa, LocalDateTime dataHora, BigDecimal velocidadeKmh, BigDecimal limiteKmh) {
    
}
