package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tables")
public class Task extends BaseEntity{

    private String taskSubject;
    private String taskDetail;
    @Enumerated(EnumType.STRING)
    private Status taskStatus;
    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedEmployee;
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
