package com.decafmango.lab_1.user.service;

import com.decafmango.lab_1.Pagination;
import com.decafmango.lab_1.exception.exceptions.AdminJoinRequestAlreadyExistsException;
import com.decafmango.lab_1.exception.exceptions.AdminJoinRequestNotFoundException;
import com.decafmango.lab_1.exception.exceptions.UserIsAlreadyAdminException;
import com.decafmango.lab_1.security.JwtService;
import com.decafmango.lab_1.user.dao.AdminJoinRequestRepository;
import com.decafmango.lab_1.user.dao.UserRepository;
import com.decafmango.lab_1.user.dto.AdminJoinRequestDto;
import com.decafmango.lab_1.user.model.AdminJoinRequest;
import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AdminJoinRequestRepository adminJoinRequestRepository;
    private final JwtService jwtService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public List<AdminJoinRequestDto> getAllAdminJoinRequests(int from, int size) {
        Pageable page = Pagination.createPageTemplate(from, size);
        List<AdminJoinRequest> adminJoinRequests = adminJoinRequestRepository.findAll(page).getContent();
        return adminJoinRequests
                .stream()
                .map(adminJoinRequest -> new AdminJoinRequestDto(
                        adminJoinRequest.getId(),
                        adminJoinRequest.getUser().getUsername()
                )).toList();
    }

    public void createAdminJoinRequest(HttpServletRequest request) {
        User fromUser = findUserByRequest(request);

        if (userRepository.findAllByRole(Role.ADMIN).isEmpty()) {
            fromUser.setRole(Role.ADMIN);
            userRepository.save(fromUser);
            simpMessagingTemplate.convertAndSend("/topic", "New admin created");
            return;
        }

        if (fromUser.getRole() == Role.ADMIN)
            throw new UserIsAlreadyAdminException(
                    String.format("User %s is already an admin", fromUser.getUsername())
            );

        if (adminJoinRequestRepository.existsByUser(fromUser))
            throw new AdminJoinRequestAlreadyExistsException(
                    String.format("Admin join request already exists from user %s", fromUser.getUsername())
            );

        AdminJoinRequest adminJoinRequest = new AdminJoinRequest(
                null,
                fromUser
        );
        adminJoinRequestRepository.save(adminJoinRequest);
        simpMessagingTemplate.convertAndSend("/topic", "Admin join request created");
    }

    public void approveOnAdminJoinRequest(Long adminJoinRequestId, HttpServletRequest request) {
        AdminJoinRequest adminJoinRequest = adminJoinRequestRepository.findById(adminJoinRequestId)
                .orElseThrow(() -> new AdminJoinRequestNotFoundException(
                        String.format("Admin join request not found %d", adminJoinRequestId)
                ));

        User fromUser = adminJoinRequest.getUser();
        fromUser.setRole(Role.ADMIN);
        userRepository.save(fromUser);

        adminJoinRequestRepository.delete(adminJoinRequest);
        simpMessagingTemplate.convertAndSend("/topic", "Admin join request approved");
    }

    private User findUserByRequest(HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        return userRepository.findByUsername(username).get();
    }


}
