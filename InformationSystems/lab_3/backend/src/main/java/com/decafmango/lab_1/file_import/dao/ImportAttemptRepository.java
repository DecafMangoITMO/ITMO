package com.decafmango.lab_1.file_import.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.decafmango.lab_1.file_import.model.ImportAttempt;
import com.decafmango.lab_1.user.model.User;

@Repository
public interface ImportAttemptRepository extends JpaRepository<ImportAttempt, Long> {
    
    List<ImportAttempt> findAllByUser(User user, Pageable page);
    Long countByUser(User user);

    Optional<ImportAttempt> findByFilename(String filename);
}
