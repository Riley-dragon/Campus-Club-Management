package com.campus.club.service;

import com.campus.club.dto.DashboardDTO;
import com.campus.club.entity.Application;
import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import com.campus.club.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ApplicationRepository applicationRepository;
    private final ActivityRepository activityRepository;

    public DashboardDTO getDashboard() {
        DashboardDTO dto = new DashboardDTO();
        dto.setTotalClubs(clubRepository.countByStatus(Club.ClubStatus.APPROVED));
        dto.setTotalStudents(userRepository.countByRole(User.Role.STUDENT));
        dto.setTotalApplications(applicationRepository.count());
        dto.setPendingApplications(applicationRepository.countByStatus(Application.AppStatus.PENDING));
        dto.setTotalActivities(activityRepository.count());
        dto.setTotalMembers(applicationRepository.countByStatus(Application.AppStatus.ADMIN_PASS));
        return dto;
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club approveClub(Long clubId, boolean pass) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));
        club.setStatus(pass ? Club.ClubStatus.APPROVED : Club.ClubStatus.REJECTED);
        return clubRepository.save(club);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setRole(User.Role.valueOf(role));
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public byte[] exportMembers(Long clubId) throws IOException {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));

        List<Application> members = applicationRepository.findByClubAndStatus(club, Application.AppStatus.ADMIN_PASS);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("成员列表");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("姓名");
        header.createCell(1).setCellValue("学号");
        header.createCell(2).setCellValue("学院");
        header.createCell(3).setCellValue("专业");
        header.createCell(4).setCellValue("电话");
        header.createCell(5).setCellValue("邮箱");

        for (int i = 0; i < members.size(); i++) {
            Application app = members.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(app.getStudentName());
            row.createCell(1).setCellValue(app.getStudentIdNo());
            row.createCell(2).setCellValue(app.getCollege());
            row.createCell(3).setCellValue(app.getMajor());
            row.createCell(4).setCellValue(app.getPhone());
            row.createCell(5).setCellValue(app.getEmail());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
