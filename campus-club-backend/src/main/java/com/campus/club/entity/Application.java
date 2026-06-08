package com.campus.club.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"password"})
    private User student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(length = 20)
    private String studentName;

    @Column(length = 30)
    private String studentIdNo;

    @Column(length = 50)
    private String college;

    @Column(length = 50)
    private String major;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppStatus status = AppStatus.PENDING;

    @Column(length = 500)
    private String leaderRemark;

    @Column(length = 500)
    private String adminRemark;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public enum AppStatus {
        PENDING,       // 待初审
        LEADER_PASS,   // 初审通过
        LEADER_REJECT, // 初审拒绝
        ADMIN_PASS,    // 终审通过（正式成为成员）
        ADMIN_REJECT   // 终审拒绝
    }
}
