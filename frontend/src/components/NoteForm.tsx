import { FormEvent, useState } from "react";
import type { Note, NoteRequest } from "../types";

type Props = {
  initial: Note | null;
  onSave: (body: NoteRequest) => Promise<void>;
  onClose: () => void;
};

function tagsToInput(note: Note | null): string {
  if (!note?.tags?.length) return "";
  return note.tags.map((t) => t.name).join(", ");
}

function parseTags(raw: string): string[] {
  return raw
    .split(/[,]+/)
    .map((s) => s.trim())
    .filter(Boolean);
}

export function NoteForm({ initial, onSave, onClose }: Props) {
  const [title, setTitle] = useState(initial?.title ?? "");
  const [content, setContent] = useState(initial?.content ?? "");
  const [tagsInput, setTagsInput] = useState(tagsToInput(initial));
  const [error, setError] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    const body: NoteRequest = {
      title: title.trim(),
      content,
      tags: parseTags(tagsInput),
    };
    if (!body.title) {
      setError("Title is required.");
      return;
    }
    setSaving(true);
    try {
      await onSave(body);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Save failed");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="modal-backdrop" role="dialog" aria-modal="true" aria-labelledby="note-form-title">
      <div className="modal">
        <div className="modal-header">
          <h2 id="note-form-title">{initial ? "Edit note" : "New note"}</h2>
          <button type="button" className="btn btn-ghost btn-sm" onClick={onClose} aria-label="Close">
            ✕
          </button>
        </div>
        <form onSubmit={onSubmit} className="form modal-form">
          {error && <div className="banner banner-error">{error}</div>}
          <label>
            Title
            <input value={title} onChange={(e) => setTitle(e.target.value)} required maxLength={200} />
          </label>
          <label>
            Tags <span className="hint">comma-separated</span>
            <input
              value={tagsInput}
              onChange={(e) => setTagsInput(e.target.value)}
              placeholder="work, ideas"
            />
          </label>
          <label>
            Content
            <textarea value={content} onChange={(e) => setContent(e.target.value)} rows={12} />
          </label>
          <div className="modal-actions">
            <button type="button" className="btn btn-ghost" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary" disabled={saving}>
              {saving ? "Saving…" : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
