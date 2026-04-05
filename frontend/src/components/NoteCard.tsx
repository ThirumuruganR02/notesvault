import type { Note } from "../types";

type Props = {
  note: Note;
  onEdit: () => void;
  onDelete: () => void;
};

export function NoteCard({ note, onEdit, onDelete }: Props) {
  const preview =
    note.content.length > 220 ? `${note.content.slice(0, 220).trim()}…` : note.content;

  return (
    <article className="note-card">
      <div className="note-card-head">
        <h2>{note.title}</h2>
        <div className="note-card-actions">
          <button type="button" className="btn btn-ghost btn-sm" onClick={onEdit}>
            Edit
          </button>
          <button type="button" className="btn btn-ghost btn-sm danger" onClick={onDelete}>
            Delete
          </button>
        </div>
      </div>
      {note.tags && note.tags.length > 0 && (
        <div className="tag-row">
          {note.tags.map((t) => (
            <span key={t.id} className="tag">
              {t.name}
            </span>
          ))}
        </div>
      )}
      <p className="note-preview">{preview || <em className="muted">Empty body</em>}</p>
      <time className="note-time" dateTime={note.updatedAt}>
        Updated {new Date(note.updatedAt).toLocaleString()}
      </time>
    </article>
  );
}
