package com.campus.club.repository;

import com.campus.club.entity.Activity;
import com.campus.club.entity.ActivityRegistration;
import com.campus.club.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Long> {
    List<ActivityRegistration> findByActivity(Activity activity);
    List<ActivityRegistration> findByUser(User user);
    Optional<ActivityRegistration> findByActivityAndUser(Activity activity, User user);
}
