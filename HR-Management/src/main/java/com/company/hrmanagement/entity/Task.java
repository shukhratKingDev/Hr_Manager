package com.company.hrmanagement.entity;

import com.company.hrmanagement.entity.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String caption;
    @Column(nullable = false)
    private Timestamp deadline;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskCurrentType;
    @Column(nullable = false)
    private boolean done;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name ="employee_id")
    private User user;

    @Column(nullable = false,updatable = false)
    @CreatedBy
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false,updatable = false)
    @LastModifiedBy
    @UpdateTimestamp
    private Timestamp updatedAt;
    private String givenBy;// email whose gave task

}
