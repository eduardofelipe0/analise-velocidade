import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ResultadoAnalise } from '../models/analise.model';

@Injectable({
  providedIn: 'root'
})
export class AnaliseService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/analises';

  analisar(arquivo: File): Observable<ResultadoAnalise> {

    const formData = new FormData();
    formData.append('arquivo', arquivo);

    return this.http.post<ResultadoAnalise>(
      this.apiUrl,
      formData
    );
  }
}
