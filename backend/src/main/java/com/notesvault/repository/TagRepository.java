package com.notesvault.repository;

import com.notesvault.model.Tag;
import com.notesvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByOwnerAndName(User owner, String name);
}
