export interface AuthResponse {
  token: string;
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface Tag {
  id: number;
  name: string;
}

export interface Note {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  tags?: Tag[];
}

export interface NoteRequest {
  title: string;
  content: string;
  tags: string[];
}
