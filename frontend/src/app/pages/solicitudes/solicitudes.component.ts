import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Prestamo } from '../../core/models';
import { AuthService } from '../../core/auth.service';
import { PrestamoService } from '../../core/prestamo.service';

@Component({
  selector: 'app-solicitudes',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './solicitudes.component.html',
  styleUrl: './solicitudes.component.scss'
})
export class SolicitudesComponent implements OnInit {
  private api = inject(PrestamoService);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  auth = inject(AuthService);

  prestamos: Prestamo[] = [];
  error: string | null = null;
  enviando = false;

  form = this.fb.nonNullable.group({
    monto: ['', [Validators.required, Validators.min(1000), Validators.max(50000000)]]
  });

  ngOnInit(): void {
    if (this.auth.esAdmin()) {
      this.router.navigate(['/admin']);
      return;
    }
    this.cargar();
  }

  cargar(): void {
    this.api.listar().subscribe({
      next: (data) => (this.prestamos = data),
      error: (err) => (this.error = err.userMessage ?? 'No se pudieron cargar los prestamos')
    });
  }

  solicitar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.enviando = true;
    this.error = null;
    const monto = Number(this.form.controls.monto.value);
    this.api.solicitar(monto, 12).subscribe({
      next: () => {
        this.enviando = false;
        this.form.reset();
        this.cargar();
      },
      error: (err) => {
        this.enviando = false;
        this.error = err.userMessage ?? 'No se pudo enviar la solicitud';
      }
    });
  }

  etiqueta(estado: string): string {
    return estado.toLowerCase();
  }

  dinero(monto: number): string {
    return '$' + Number(monto).toLocaleString('en-US', { maximumFractionDigits: 0 });
  }
}
