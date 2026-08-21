export type Rol = 'CLIENTE' | 'ADMIN';
export type EstadoPrestamo = 'PENDIENTE' | 'APROBADO' | 'RECHAZADO';

export interface Sesion {
  token: string;
  username: string;
  nombreCompleto: string;
  rol: Rol;
}

export interface Prestamo {
  id: number;
  solicitante: string;
  monto: number;
  plazoMeses: number;
  estado: EstadoPrestamo;
  fechaSolicitud: string;
  fechaDecision: string | null;
  comentarioDecision: string | null;
}

export interface EstadoConsulta {
  id: number;
  estado: EstadoPrestamo;
  comentarioDecision: string | null;
}

export interface ApiError {
  codigo: string;
  mensaje: string;
  fecha: string;
}
