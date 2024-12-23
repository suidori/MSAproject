package com.management.ma_project.attendance;

import com.management.ma_project.lecture.Lecture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String type;

    private String reason;

    private LocalDate adate;

    private Long useridx;

    @ManyToOne
    @JoinColumn(name = "lectureidx")
    private Lecture lecture;

    private boolean manageraccept;

    private Boolean approval;

}
