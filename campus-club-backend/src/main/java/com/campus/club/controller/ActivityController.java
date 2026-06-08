package com.campus.club.controller;

import com.campus.club.dto.Result;
import com.campus.club.entity.Activity;
import com.campus.club.entity.ActivityRegistration;
import com.campus.club.service.ActivityService;
import com.campus.club.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public Result<List<Activity>> getAllActivities() {
        return Result.success(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    public Result<Activity> getActivity(@PathVariable Long id) {
        return Result.success(activityService.getActivityById(id));
    }

    @GetMapping("/club/{clubId}")
    public Result<List<Activity>> getClubActivities(@PathVariable Long clubId) {
        return Result.success(activityService.getClubActivities(clubId));
    }

    @PostMapping
    public Result<Activity> createActivity(@RequestParam Long clubId, @RequestBody Activity activity) {
        return Result.success("活动创建成功", activityService.createActivity(clubId, activity));
    }

    @PutMapping("/{id}")
    public Result<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        return Result.success("活动更新成功", activityService.updateActivity(id, activity));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/{id}/register")
    public Result<ActivityRegistration> registerActivity(HttpServletRequest request, @PathVariable Long id) {
        Long userId = getUserId(request);
        return Result.success("报名成功", activityService.registerActivity(userId, id));
    }

    @PutMapping("/registrations/{id}/checkin")
    public Result<ActivityRegistration> checkIn(@PathVariable Long id) {
        return Result.success("签到成功", activityService.checkIn(id));
    }

    @GetMapping("/{id}/registrations")
    public Result<List<ActivityRegistration>> getActivityRegistrations(@PathVariable Long id) {
        return Result.success(activityService.getActivityRegistrations(id));
    }

    @GetMapping("/my-registrations")
    public Result<List<ActivityRegistration>> getMyRegistrations(HttpServletRequest request) {
        Long userId = getUserId(request);
        return Result.success(activityService.getMyRegistrations(userId));
    }

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtUtil.getUserId(token);
    }
}
