import { Component, OnInit, inject } from '@angular/core';
import { Prestamo } from '../../core/models';
import { AuthService } from '../../core/auth.service';
import { PrestamoService } from '../../core/prestamo.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent implements OnInit {
  private api = inject(PrestamoService);
  auth = inject(AuthService);

  prestamos: Prestamo[] = [];
  error: string | null = null;

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.api.listar().subscribe({
      next: (data) => (this.prestamos = data),
      error: (err) => (this.error = err.userMessage ?? 'No se pudo cargar el listado')
    });
  }

  aprobar(item: Prestamo): void {
    this.api.decidir(item.id, true, 'Aprobado').subscribe({
      next: () => this.cargar(),
      error: (err) => (this.error = err.userMessage)
    });
  }

  rechazar(item: Prestamo): void {
    this.api.decidir(item.id, false, 'Rechazado').subscribe({
      next: () => this.cargar(),
      error: (err) => (this.error = err.userMessage)
    });
  }

  etiqueta(estado: string): string {
    return estado.toLowerCase();
  }

  dinero(monto: number): string {
    return '$' + Number(monto).toLocaleString('en-US', { maximumFractionDigits: 0 });
  }
}
