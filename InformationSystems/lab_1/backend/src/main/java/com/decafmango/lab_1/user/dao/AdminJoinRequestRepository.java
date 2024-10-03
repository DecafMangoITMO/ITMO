package com.decafmango.lab_1.user.dao;

import com.decafmango.lab_1.user.model.AdminJoinRequest;
import com.decafmango.lab_1.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminJoinRequestRepository extends JpaRepository<AdminJoinRequest, Long> {

    boolean existsByUser(User user);
}
