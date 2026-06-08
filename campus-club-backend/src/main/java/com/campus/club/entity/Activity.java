package com.campus.club.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String location;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer maxParticipants;
    private Integer currentParticipants = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    @JsonIgnoreProperties({"leader"})
    private Club club;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status = ActivityStatus.UPCOMING;

    private LocalDateTime createTime;

    public enum ActivityStatus {
        UPCOMING, ONGOING, COMPLETED, CANCELLED
    }
}
