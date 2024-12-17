package com.management.user_service.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String userid;

    private String password;

    private String name;

    private String phoneNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean accept;

    private Long enrolledLectureId;

}
