package com.campus.club.dto;

import lombok.Data;

@Data
public class DashboardDTO {
    private long totalClubs;
    private long totalStudents;
    private long totalApplications;
    private long pendingApplications;
    private long totalActivities;
    private long totalMembers;
}
