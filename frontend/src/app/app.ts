import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { AnaliseService } from './services/analise.service';
import { ResultadoAnalise } from './models/analise.model';

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

  selecionarArquivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    const arquivo = input.files?.[0] ?? null;

    this.arquivoSelecionado.set(arquivo);
    this.resultado.set(null);
    this.erro.set('');
  }

  analisar(): void {
    const arquivo = this.arquivoSelecionado();

    if (!arquivo || this.carregando()) {
      return;
    }

    this.carregando.set(true);
    this.erro.set('');
    this.resultado.set(null);

    this.analiseService.analisar(arquivo).subscribe({
      next: (resposta) => {
        this.resultado.set(resposta);
        this.carregando.set(false);
      },
      error: (erro: HttpErrorResponse) => {
        this.erro.set(
          erro.error?.erro ??
          'Não foi possível analisar o arquivo.'
        );

        this.carregando.set(false);
      }
    });
  }
}
