import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EstadoConsulta, Prestamo } from './models';

@Injectable({ providedIn: 'root' })
export class PrestamoService {
  constructor(private http: HttpClient) {}

  listar() {
    return this.http.get<Prestamo[]>('/api/prestamos');
  }

  solicitar(monto: number, plazoMeses: number) {
    return this.http.post<Prestamo>('/api/prestamos', { monto, plazoMeses });
  }

  estado(id: number) {
    return this.http.get<EstadoConsulta>(`/api/prestamos/${id}/estado`);
  }

  decidir(id: number, aprobar: boolean, comentario?: string) {
    return this.http.put<Prestamo>(`/api/prestamos/${id}/decision`, { aprobar, comentario });
  }
}
