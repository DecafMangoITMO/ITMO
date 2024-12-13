package com.decafmango.lab_1.user.contoller;

import com.decafmango.lab_1.user.dto.AdminJoinRequestDto;
import com.decafmango.lab_1.user.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/count")
    public Long getADminJoinRequestsCount() {
        return adminService.getAdminJoinRequestsCount();
    }

    @GetMapping
    public List<AdminJoinRequestDto> getAllAdminJoinRequests(@RequestParam int from, @RequestParam int size) {
        return adminService.getAllAdminJoinRequests(from, size);
    }

    @PostMapping
    public void createAdminJoinRequest(HttpServletRequest request) {
        adminService.createAdminJoinRequest(request);
    }

    @PutMapping("/{adminJoinRequestId}")
    public void answerAdminJoinRequest(@PathVariable Long adminJoinRequestId, HttpServletRequest request) {
        adminService.approveOnAdminJoinRequest(adminJoinRequestId, request);
    }

}
