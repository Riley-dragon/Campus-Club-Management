package com.campus.club.controller;

import com.campus.club.dto.Result;
import com.campus.club.entity.Application;
import com.campus.club.service.ApplicationService;
import com.campus.club.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public Result<Application> submitApplication(HttpServletRequest request,
                                                  @RequestParam Long clubId,
                                                  @RequestBody Application app) {
        Long userId = getUserId(request);
        return Result.success("申请提交成功", applicationService.submitApplication(userId, clubId, app));
    }

    @GetMapping("/my")
    public Result<List<Application>> getMyApplications(HttpServletRequest request) {
        Long userId = getUserId(request);
        return Result.success(applicationService.getMyApplications(userId));
    }

    @GetMapping("/pending/{clubId}")
    public Result<List<Application>> getPendingApplications(@PathVariable Long clubId) {
        return Result.success(applicationService.getPendingApplications(clubId));
    }

    @GetMapping("/club/{clubId}")
    public Result<List<Application>> getClubApplications(@PathVariable Long clubId) {
        return Result.success(applicationService.getClubApplications(clubId));
    }

    @PutMapping("/{id}/leader-review")
    public Result<Application> leaderReview(@PathVariable Long id,
                                            @RequestParam boolean pass,
                                            @RequestParam(defaultValue = "") String remark) {
        return Result.success("审核完成", applicationService.leaderReview(id, pass, remark));
    }

    @GetMapping("/leader-passed")
    public Result<List<Application>> getLeaderPassedApplications() {
        return Result.success(applicationService.getLeaderPassedApplications());
    }

    @PutMapping("/{id}/admin-review")
    public Result<Application> adminReview(@PathVariable Long id,
                                           @RequestParam boolean pass,
                                           @RequestParam(defaultValue = "") String remark) {
        return Result.success("审核完成", applicationService.adminReview(id, pass, remark));
    }

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtUtil.getUserId(token);
    }
}
