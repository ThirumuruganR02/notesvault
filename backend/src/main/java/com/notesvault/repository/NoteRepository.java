package com.notesvault.repository;

import com.notesvault.model.Note;
import com.notesvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByOwnerOrderByUpdatedAtDesc(User owner);

    Optional<Note> findByIdAndOwner(Long id, User owner);

    boolean existsByIdAndOwner(Long id, User owner);
}
