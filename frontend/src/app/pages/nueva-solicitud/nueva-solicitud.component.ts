import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { PrestamoService } from '../../core/prestamo.service';

@Component({
  selector: 'app-nueva-solicitud',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './nueva-solicitud.component.html',
  styleUrl: './nueva-solicitud.component.scss'
})
export class NuevaSolicitudComponent {
  private fb = inject(FormBuilder);
  private api = inject(PrestamoService);
  private router = inject(Router);
  auth = inject(AuthService);

  error: string | null = null;
  enviando = false;

  form = this.fb.nonNullable.group({
    monto: [1000000, [Validators.required, Validators.min(500000), Validators.max(50000000)]],
    plazoMeses: [12, [Validators.required, Validators.min(6), Validators.max(60)]]
  });

  submit(): void {
    if (this.auth.esAdmin()) {
      this.error = 'El administrador no pide prestamos con esta cuenta.';
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.enviando = true;
    const { monto, plazoMeses } = this.form.getRawValue();
    this.api.solicitar(monto, plazoMeses).subscribe({
      next: () => this.router.navigate(['/solicitudes']),
      error: (err) => {
        this.enviando = false;
        this.error = err.userMessage ?? 'No se pudo enviar la solicitud';
      }
    });
  }
}
