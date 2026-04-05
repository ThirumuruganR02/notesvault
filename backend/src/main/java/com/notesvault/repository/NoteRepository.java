package com.notesvault.repository;

import com.notesvault.model.Note;
import com.notesvault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query(
            "select distinct n from Note n left join fetch n.tags where n.owner = :owner order by n.updatedAt desc")
    List<Note> findAllByOwnerOrderByUpdatedAtDesc(@Param("owner") User owner);

    @Query(
            """
                    select distinct n from Note n
                    inner join n.tags filterTag
                    left join fetch n.tags
                    where n.owner = :owner and filterTag.name = :tagName
                    order by n.updatedAt desc
                    """)
    List<Note> findAllByOwnerAndTagNameOrderByUpdatedAtDesc(
            @Param("owner") User owner, @Param("tagName") String tagName);

    @Query("select distinct n from Note n left join fetch n.tags where n.id = :id and n.owner = :owner")
    Optional<Note> findByIdAndOwner(@Param("id") Long id, @Param("owner") User owner);

    boolean existsByIdAndOwner(Long id, User owner);
}
