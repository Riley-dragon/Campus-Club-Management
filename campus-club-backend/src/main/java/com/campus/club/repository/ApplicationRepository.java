package com.campus.club.repository;

import com.campus.club.entity.Application;
import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(User student);
    List<Application> findByClub(Club club);
    List<Application> findByClubAndStatus(Club club, Application.AppStatus status);
    List<Application> findByStatus(Application.AppStatus status);
    Optional<Application> findByStudentAndClub(User student, Club club);
    long countByStatus(Application.AppStatus status);
}
