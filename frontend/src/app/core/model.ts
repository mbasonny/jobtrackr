export type ApplicationStatus = 'POSTULE' | 'ENTRETIEN' | 'REFUSE' | 'OFFRE';

export const STATUS_LABELS: Record<ApplicationStatus, string> = {
  POSTULE: 'Postulé',
  ENTRETIEN: 'Entretien',
  REFUSE: 'Refusé',
  OFFRE: 'Offre'
};

export interface AuthResponse {
  token: string;
  email: string;
  fullName: string;
}

export interface RegisterPayload {
  fullName: string;
  email: string;
  password: string;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface JobApplication {
  id: string;
  company: string;
  position: string;
  url?: string;
  notes?: string;
  status: ApplicationStatus;
  appliedDate: string; //ISO yyy-MM-dd
}

export interface CreateApplicationPayload {
  company: string;
  position: string;
  url?: string;
  notes?: string;
  appliedDate: string;
}

export interface UpdateApplicationPayload {
  company?: string;
  position?: string;
  url?: string;
  notes?: string;
  status?: ApplicationStatus;
  appliedDate?: string;
}

export interface Stats {
  postule: number;
  entretien: number;
  refuse: number;
  offre: number;
  total: number;
}
