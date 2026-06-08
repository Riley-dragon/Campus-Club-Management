package com.campus.club.repository;

import com.campus.club.entity.Activity;
import com.campus.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByClub(Club club);
    List<Activity> findByStatus(Activity.ActivityStatus status);
}
