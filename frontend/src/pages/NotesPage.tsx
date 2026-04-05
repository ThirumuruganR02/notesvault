import { FormEvent, useCallback, useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import * as api from "../api/client";
import { ApiError } from "../api/client";
import type { Note, NoteRequest } from "../types";
import { NoteForm } from "../components/NoteForm";
import { NoteCard } from "../components/NoteCard";

export function NotesPage() {
  const { user, logout } = useAuth();
  const [notes, setNotes] = useState<Note[]>([]);
  const [tagFilter, setTagFilter] = useState("");
  const [search, setSearch] = useState("");
  const [appliedTag, setAppliedTag] = useState("");
  const [appliedSearch, setAppliedSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [formOpen, setFormOpen] = useState(false);
  const [editing, setEditing] = useState<Note | null>(null);

  const load = useCallback(async () => {
    setError(null);
    setLoading(true);
    try {
      const list = await api.fetchNotes(appliedTag || undefined, appliedSearch || undefined);
      setNotes(list);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load notes");
      if (err instanceof ApiError && err.status === 401) {
        logout();
      }
    } finally {
      setLoading(false);
    }
  }, [appliedTag, appliedSearch, logout]);

  useEffect(() => {
    void load();
  }, [load]);

  function applyFilters(e: FormEvent) {
    e.preventDefault();
    setAppliedTag(tagFilter.trim());
    setAppliedSearch(search.trim());
  }

  function clearFilters() {
    setTagFilter("");
    setSearch("");
    setAppliedTag("");
    setAppliedSearch("");
  }

  async function handleSave(request: NoteRequest) {
    if (editing) {
      const updated = await api.updateNote(editing.id, request);
      setNotes((prev) => prev.map((n) => (n.id === updated.id ? updated : n)));
    } else {
      const created = await api.createNote(request);
      setNotes((prev) => [created, ...prev]);
    }
    setFormOpen(false);
    setEditing(null);
  }

  async function handleDelete(id: number) {
    if (!confirm("Delete this note permanently?")) return;
    await api.deleteNote(id);
    setNotes((prev) => prev.filter((n) => n.id !== id));
  }

  function openNew() {
    setEditing(null);
    setFormOpen(true);
  }

  function openEdit(note: Note) {
    setEditing(note);
    setFormOpen(true);
  }

  return (
    <div className="app-shell">
      <header className="top-bar">
        <div className="brand">
          <span className="brand-mark">◆</span>
          <span>NotesVault</span>
        </div>
        <div className="top-bar-meta">
          <span className="user-pill">{user?.email}</span>
          <button type="button" className="btn btn-ghost" onClick={logout}>
            Sign out
          </button>
        </div>
      </header>

      <main className="main">
        <div className="toolbar">
          <form className="filters" onSubmit={applyFilters}>
            <input
              type="text"
              placeholder="Filter by tag"
              value={tagFilter}
              onChange={(e) => setTagFilter(e.target.value)}
              aria-label="Tag filter"
            />
            <input
              type="search"
              placeholder="Search title & content"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              aria-label="Search"
            />
            <button type="submit" className="btn btn-secondary">
              Apply
            </button>
            <button type="button" className="btn btn-ghost" onClick={clearFilters}>
              Clear
            </button>
          </form>
          <button type="button" className="btn btn-primary" onClick={openNew}>
            New note
          </button>
        </div>

        {error && <div className="banner banner-error">{error}</div>}

        {loading ? (
          <p className="muted center-pad">Loading notes…</p>
        ) : notes.length === 0 ? (
          <p className="muted center-pad">No notes yet. Create your first one.</p>
        ) : (
          <ul className="note-grid">
            {notes.map((note) => (
              <li key={note.id}>
                <NoteCard note={note} onEdit={() => openEdit(note)} onDelete={() => handleDelete(note.id)} />
              </li>
            ))}
          </ul>
        )}
      </main>

      {formOpen && (
        <NoteForm
          initial={editing}
          onSave={handleSave}
          onClose={() => {
            setFormOpen(false);
            setEditing(null);
          }}
        />
      )}
    </div>
  );
}
