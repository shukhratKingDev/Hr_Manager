package com.company.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turnstile {
    @Id
    @GeneratedValue
    private UUID id;
    private Boolean inside=null;
    @Column(updatable = false)
    private Timestamp enteredAt;
    @Column(updatable = false)
    private Timestamp leftAt;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private User user;
    @CreationTimestamp
    @Column(updatable = false)
    @CreatedBy
    private Timestamp createdAt;
    private boolean available;
}
