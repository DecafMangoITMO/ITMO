package com.decafmango.lab_1.user.dao;

import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    List<User> findAllByRole(Role role);
}
