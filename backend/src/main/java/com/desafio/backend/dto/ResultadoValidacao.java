package com.desafio.backend.dto;

import java.util.List;

public record ResultadoValidacao(int linhasLidas, List<Leitura> leituras, List<LinhaInvalida> linhasInvalidas) {

}
