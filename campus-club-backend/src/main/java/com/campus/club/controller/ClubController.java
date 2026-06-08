package com.campus.club.controller;

import com.campus.club.dto.Result;
import com.campus.club.entity.Club;
import com.campus.club.service.ClubService;
import com.campus.club.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public Result<List<Club>> getClubs(@RequestParam(required = false) String keyword) {
        return Result.success(clubService.getApprovedClubs(keyword));
    }

    @GetMapping("/{id}")
    public Result<Club> getClub(@PathVariable Long id) {
        return Result.success(clubService.getClubById(id));
    }

    @GetMapping("/my")
    public Result<List<Club>> getMyClubs(HttpServletRequest request) {
        Long userId = getUserId(request);
        return Result.success(clubService.getMyClubs(userId));
    }

    @PostMapping
    public Result<Club> createClub(HttpServletRequest request, @RequestBody Club club) {
        Long userId = getUserId(request);
        return Result.success("创建成功", clubService.createClub(userId, club));
    }

    @PutMapping("/{id}")
    public Result<Club> updateClub(@PathVariable Long id, HttpServletRequest request, @RequestBody Club club) {
        Long userId = getUserId(request);
        return Result.success("更新成功", clubService.updateClub(id, userId, club));
    }

    private Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtUtil.getUserId(token);
    }
}
