export interface Episodio {
  placa: string;
  inicio: string;
  fim: string;
  duracaoSegundos: number;
  velocidadeMaxima: number;
  excessoMaximoPercentual: number;
  gravidade: string;
  pontos: number;
}

export interface RankingVeiculo {
  placa: string;
  totalPontos: number;
  velocidadeMaxima: number;
  totalEpisodios: number;
}

export interface LinhaInvalida {
  linha: number;
  motivo: string;
}

export interface ResultadoAnalise {
  resumo: {
    linhasLidas: number;
    linhasValidas: number;
    linhasInvalidas: number;
    totalEpisodios: number;
  };
  episodios: Episodio[];
  ranking: RankingVeiculo[];
  linhasInvalidas: LinhaInvalida[];
}
