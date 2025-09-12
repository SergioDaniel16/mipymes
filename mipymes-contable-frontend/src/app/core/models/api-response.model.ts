// Modelo para respuestas de la API
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface ErrorResponse {
  status: number;
  error: string;
  message: string;
  timestamp: string;
} 
