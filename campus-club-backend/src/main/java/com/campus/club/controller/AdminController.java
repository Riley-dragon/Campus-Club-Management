package com.campus.club.controller;

import com.campus.club.dto.DashboardDTO;
import com.campus.club.dto.Result;
import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import com.campus.club.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public Result<DashboardDTO> getDashboard() {
        return Result.success(adminService.getDashboard());
    }

    @GetMapping("/clubs")
    public Result<List<Club>> getAllClubs() {
        return Result.success(adminService.getAllClubs());
    }

    @PutMapping("/clubs/{id}/approve")
    public Result<Club> approveClub(@PathVariable Long id, @RequestParam boolean pass) {
        return Result.success("审核完成", adminService.approveClub(id, pass));
    }

    @GetMapping("/users")
    public Result<List<User>> getUsers() {
        return Result.success(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/role")
    public Result<User> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        return Result.success("角色更新成功", adminService.updateUserRole(id, role));
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return Result.success("删除成功", null);
    }

    @GetMapping("/export/members/{clubId}")
    public void exportMembers(@PathVariable Long clubId, HttpServletResponse response) throws IOException {
        byte[] data = adminService.exportMembers(clubId);
        String fileName = URLEncoder.encode("成员列表.xlsx", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.getOutputStream().write(data);
    }
}
