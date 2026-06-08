package com.campus.club.service;

import com.campus.club.entity.Application;
import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import com.campus.club.repository.ApplicationRepository;
import com.campus.club.repository.ClubRepository;
import com.campus.club.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public Application submitApplication(Long userId, Long clubId, Application app) {
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));

        applicationRepository.findByStudentAndClub(student, club)
                .ifPresent(a -> { throw new RuntimeException("您已提交过该社团的申请"); });

        Application application = new Application();
        application.setStudent(student);
        application.setClub(club);
        application.setStudentName(app.getStudentName());
        application.setStudentIdNo(app.getStudentIdNo());
        application.setCollege(app.getCollege());
        application.setMajor(app.getMajor());
        application.setPhone(app.getPhone());
        application.setEmail(app.getEmail());
        application.setReason(app.getReason());
        application.setStatus(Application.AppStatus.PENDING);
        application.setCreateTime(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    public List<Application> getMyApplications(Long userId) {
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return applicationRepository.findByStudent(student);
    }

    public List<Application> getPendingApplications(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));
        return applicationRepository.findByClubAndStatus(club, Application.AppStatus.PENDING);
    }

    public List<Application> getClubApplications(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));
        return applicationRepository.findByClub(club);
    }

    public Application leaderReview(Long appId, boolean pass, String remark) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        app.setStatus(pass ? Application.AppStatus.LEADER_PASS : Application.AppStatus.LEADER_REJECT);
        app.setLeaderRemark(remark);
        app.setUpdateTime(LocalDateTime.now());
        return applicationRepository.save(app);
    }

    public List<Application> getLeaderPassedApplications() {
        return applicationRepository.findByStatus(Application.AppStatus.LEADER_PASS);
    }

    public Application adminReview(Long appId, boolean pass, String remark) {
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("申请不存在"));
        app.setStatus(pass ? Application.AppStatus.ADMIN_PASS : Application.AppStatus.ADMIN_REJECT);
        app.setAdminRemark(remark);
        app.setUpdateTime(LocalDateTime.now());
        return applicationRepository.save(app);
    }
}
