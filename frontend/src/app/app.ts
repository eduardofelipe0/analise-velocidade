
import { Component, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AnaliseService } from './services/analise.service';
import { ResultadoAnalise } from './models/analise.model';

type GravidadeFiltro = 'TODAS' | 'LEVE' | 'GRAVE' | 'GRAVISSIMA';

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly analiseService = inject(AnaliseService);

  arquivoSelecionado = signal<File | null>(null);
  carregando = signal(false);
  erro = signal('');
  resultado = signal<ResultadoAnalise | null>(null);
  gravidadeSelecionada = signal<GravidadeFiltro>('TODAS');

  episodiosFiltrados = computed(() => {
    const episodios = this.resultado()?.episodios ?? [];
    const gravidade = this.gravidadeSelecionada();

    return gravidade === 'TODAS'
      ? episodios
      : episodios.filter(e => e.gravidade === gravidade);
  });

  selecionarArquivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    const arquivo = input.files?.[0] ?? null;

    this.erro.set('');
    this.resultado.set(null);
    this.gravidadeSelecionada.set('TODAS');

    if (arquivo && !arquivo.name.toLowerCase().endsWith('.csv')) {
      this.arquivoSelecionado.set(null);
      this.erro.set('Selecione um arquivo com extensão .csv.');
      input.value = '';
      return;
    }

    this.arquivoSelecionado.set(arquivo);
  }

  alterarGravidade(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.gravidadeSelecionada.set(select.value as GravidadeFiltro);
  }

  analisar(): void {
    const arquivo = this.arquivoSelecionado();

    if (!arquivo || this.carregando()) return;

    if (arquivo.size === 0) {
      this.erro.set('O arquivo selecionado está vazio.');
      return;
    }

    this.carregando.set(true);
    this.erro.set('');
    this.resultado.set(null);
    this.gravidadeSelecionada.set('TODAS');

    this.analiseService.analisar(arquivo).subscribe({
      next: resposta => {
        this.resultado.set(resposta);
        this.carregando.set(false);
      },
      error: (falha: HttpErrorResponse) => {
        const mensagemApi =
          falha.error &&
          typeof falha.error === 'object' &&
          typeof falha.error.erro === 'string'
            ? falha.error.erro
            : null;

        this.erro.set(mensagemApi ?? (
          falha.status === 0
            ? 'Não foi possível conectar ao servidor. Verifique se o backend está em execução.'
            : 'Não foi possível analisar o arquivo. Tente novamente.'
        ));

        this.carregando.set(false);
      }
    });
  }

  formatarData(data: string): string {
    const correspondencia =
      /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})/.exec(data);

    if (!correspondencia) return data;

    const [, ano, mes, dia, hora, minuto, segundo] = correspondencia;

    return `${dia}/${mes}/${ano} ${hora}:${minuto}:${segundo}`;
  }

  formatarDuracao(segundos: number): string {
    const horas = Math.floor(segundos / 3600);
    const minutos = Math.floor((segundos % 3600) / 60);
    const restante = segundos % 60;

    return [
      horas ? `${horas} h` : '',
      minutos ? `${minutos} min` : '',
      restante || (!horas && !minutos) ? `${restante} s` : ''
    ].filter(Boolean).join(' ');
  }

  formatarNumero(valor: number, casas = 1): string {
    return new Intl.NumberFormat('pt-BR', {
      minimumFractionDigits: casas,
      maximumFractionDigits: casas
    }).format(valor);
  }
}
