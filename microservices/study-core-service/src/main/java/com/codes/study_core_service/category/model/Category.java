package com.codes.study_core_service.category.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categories_user_name",
                        columnNames = {"user_id", "name"}
                )
        }
)

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false, length = 80)
    private String userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 30)
    private String color;

    @Column(length = 30)
    private String icon;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "create_at",nullable = false)
    private LocalDate createdAt;

    @Column(name= "update_at", nullable = false)
    private LocalDate updatedAt;


    @PrePersist
    void onCreate() {
        LocalDate now = LocalDateTime.now().toLocalDate();

        this.createdAt = now;
        this.updatedAt = now;

        if  (this.active == null)
            this.active = true;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
