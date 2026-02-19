package com.codes.study_core_service.session.model;

import com.codes.study_core_service.session.enumerator.EStudySession;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "study_session",
        indexes = {
                @Index(
                        name = "idx_sessions_user_status",
                        columnList = "user_id, status"
                ),
                @Index(
                        name = "idx_sessions_user_start",
                        columnList = "user_id, start_time"
                )

        }
)
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 80)
    private String userId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EStudySession status;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    // puede ser nullable porque el la session puede iniciar pero aun no cuenta en el momento
    @Column(name = "total_seconds")
    private Long totalSeconds;

    @Column(length = 200)
    private String note;


    /**
     * Funcion que nos permite en el momento que se realice una instancia
     * se estableza los valores de forma dinamica
     */
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = EStudySession.RUNNING;
        }
        if (this.startTime == null) {
            this.startTime = LocalDateTime.now();
        }
    }
}
