package com.campus.club.service;

import com.campus.club.entity.Activity;
import com.campus.club.entity.ActivityRegistration;
import com.campus.club.entity.Club;
import com.campus.club.entity.User;
import com.campus.club.repository.ActivityRegistrationRepository;
import com.campus.club.repository.ActivityRepository;
import com.campus.club.repository.ClubRepository;
import com.campus.club.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityRegistrationRepository registrationRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
    }

    public List<Activity> getClubActivities(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));
        return activityRepository.findByClub(club);
    }

    public Activity createActivity(Long clubId, Activity activity) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("社团不存在"));
        activity.setClub(club);
        activity.setCurrentParticipants(0);
        activity.setCreateTime(LocalDateTime.now());
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updated) {
        Activity activity = getActivityById(id);
        activity.setTitle(updated.getTitle());
        activity.setDescription(updated.getDescription());
        activity.setLocation(updated.getLocation());
        activity.setStartTime(updated.getStartTime());
        activity.setEndTime(updated.getEndTime());
        activity.setMaxParticipants(updated.getMaxParticipants());
        activity.setStatus(updated.getStatus());
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    public ActivityRegistration registerActivity(Long userId, Long activityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Activity activity = getActivityById(activityId);

        registrationRepository.findByActivityAndUser(activity, user)
                .ifPresent(r -> { throw new RuntimeException("您已报名此活动"); });

        if (activity.getMaxParticipants() != null
                && activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new RuntimeException("活动名额已满");
        }

        ActivityRegistration registration = new ActivityRegistration();
        registration.setActivity(activity);
        registration.setUser(user);
        registration.setRegisterTime(LocalDateTime.now());

        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activityRepository.save(activity);

        return registrationRepository.save(registration);
    }

    public ActivityRegistration checkIn(Long registrationId) {
        ActivityRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("报名记录不存在"));
        registration.setCheckedIn(true);
        registration.setCheckInTime(LocalDateTime.now());
        return registrationRepository.save(registration);
    }

    public List<ActivityRegistration> getActivityRegistrations(Long activityId) {
        Activity activity = getActivityById(activityId);
        return registrationRepository.findByActivity(activity);
    }

    public List<ActivityRegistration> getMyRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return registrationRepository.findByUser(user);
    }
}
