import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  error: string | null = null;
  enviando = false;

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.enviando = true;
    this.error = null;
    const { email, password } = this.form.getRawValue();
    this.auth.login(email, password).subscribe({
      next: (sesion) => {
        this.enviando = false;
        this.router.navigate([sesion.rol === 'ADMIN' ? '/admin' : '/solicitudes']);
      },
      error: (err) => {
        this.enviando = false;
        this.error = err.userMessage ?? 'No se pudo entrar';
      }
    });
  }
}
