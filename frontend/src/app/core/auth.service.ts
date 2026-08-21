import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Sesion } from './models';

const STORAGE_KEY = 'prestamos_sesion';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly sesion$ = new BehaviorSubject<Sesion | null>(this.leerStorage());

  constructor(private http: HttpClient, private router: Router) {}

  sesion() {
    return this.sesion$.asObservable();
  }

  actual(): Sesion | null {
    return this.sesion$.value;
  }

  estaLogueado(): boolean {
    return !!this.sesion$.value;
  }

  esAdmin(): boolean {
    return this.sesion$.value?.rol === 'ADMIN';
  }

  login(username: string, password: string) {
    return this.http.post<Sesion>('/api/auth/login', { username, password }).pipe(
      tap((sesion) => {
        sessionStorage.setItem(STORAGE_KEY, JSON.stringify(sesion));
        this.sesion$.next(sesion);
      })
    );
  }

  logout(): void {
    sessionStorage.removeItem(STORAGE_KEY);
    this.sesion$.next(null);
    this.router.navigate(['/login']);
  }

  private leerStorage(): Sesion | null {
    const raw = sessionStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as Sesion;
    } catch {
      return null;
    }
  }
}
