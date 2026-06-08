package com.campus.club.repository;

import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByStatus(Club.ClubStatus status);
    List<Club> findByLeader(User leader);
    List<Club> findByNameContaining(String keyword);
    long countByStatus(Club.ClubStatus status);
}
