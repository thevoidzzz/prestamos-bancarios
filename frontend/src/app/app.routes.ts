import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SolicitudesComponent } from './pages/solicitudes/solicitudes.component';
import { AdminComponent } from './pages/admin/admin.component';
import { authGuard } from './core/auth/auth.guard';
import { adminGuard } from './core/auth/admin.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'solicitudes', component: SolicitudesComponent, canActivate: [authGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [authGuard, adminGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'solicitudes' },
  { path: '**', redirectTo: 'solicitudes' }
];
