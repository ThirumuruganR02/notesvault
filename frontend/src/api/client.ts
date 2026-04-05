import type { AuthResponse, Note, NoteRequest } from "../types";

/** Empty in dev (Vite proxy). Set `VITE_API_BASE_URL` when the UI is hosted separately from the API. */
const API_BASE = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.replace(/\/$/, "") ?? "";

const TOKEN_KEY = "notesvault_token";
const USER_KEY = "notesvault_user";

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY);
}

export function getStoredUser(): Pick<AuthResponse, "username" | "email" | "id"> | null {
  const raw = localStorage.getItem(USER_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as Pick<AuthResponse, "username" | "email" | "id">;
  } catch {
    return null;
  }
}

export function persistSession(auth: AuthResponse): void {
  localStorage.setItem(TOKEN_KEY, auth.token);
  localStorage.setItem(
    USER_KEY,
    JSON.stringify({ id: auth.id, username: auth.username, email: auth.email }),
  );
}

export function clearSession(): void {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}

export class ApiError extends Error {
  readonly status: number;

  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

async function parseError(res: Response): Promise<string> {
  const text = await res.text();
  try {
    const j = JSON.parse(text) as {
      message?: string;
      error?: string;
      fieldErrors?: Record<string, string>;
    };
    if (j.fieldErrors && Object.keys(j.fieldErrors).length > 0) {
      return Object.entries(j.fieldErrors)
        .map(([k, v]) => `${k}: ${v}`)
        .join("; ");
    }
    return j.message ?? j.error ?? (text || res.statusText);
  } catch {
    return text || res.statusText;
  }
}

async function api<T>(path: string, options: RequestInit = {}, withAuth = true): Promise<T> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...((options.headers as Record<string, string>) || {}),
  };
  if (withAuth) {
    const token = getToken();
    if (token) headers.Authorization = `Bearer ${token}`;
  }
  const url = `${API_BASE}${path}`;
  const res = await fetch(url, { ...options, headers });
  if (!res.ok) {
    throw new ApiError(await parseError(res), res.status);
  }
  if (res.status === 204) {
    return undefined as T;
  }
  return res.json() as Promise<T>;
}

export async function register(body: {
  username: string;
  email: string;
  password: string;
}): Promise<AuthResponse> {
  return api<AuthResponse>("/api/auth/register", { method: "POST", body: JSON.stringify(body) }, false);
}

export async function login(body: { email: string; password: string }): Promise<AuthResponse> {
  return api<AuthResponse>("/api/auth/login", { method: "POST", body: JSON.stringify(body) }, false);
}

export function notesQuery(tag?: string, search?: string): string {
  const p = new URLSearchParams();
  if (tag?.trim()) p.set("tag", tag.trim());
  if (search?.trim()) p.set("search", search.trim());
  const q = p.toString();
  return q ? `/notes?${q}` : "/notes";
}

export async function fetchNotes(tag?: string, search?: string): Promise<Note[]> {
  return api<Note[]>(notesQuery(tag, search));
}

export async function createNote(body: NoteRequest): Promise<Note> {
  return api<Note>("/notes", { method: "POST", body: JSON.stringify(body) });
}

export async function updateNote(id: number, body: NoteRequest): Promise<Note> {
  return api<Note>(`/notes/${id}`, { method: "PUT", body: JSON.stringify(body) });
}

export async function deleteNote(id: number): Promise<void> {
  return api<void>(`/notes/${id}`, { method: "DELETE" });
}
