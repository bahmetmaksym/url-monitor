package com.bahmet.urlmonitor.repository.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "monitored_endpoints")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonitoredEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "date_of_last_check")
    private LocalDateTime dateOfLastCheck;

    @Column(name = "monitored_interval")
    private Long monitoredInterval;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "monitoredEndpoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MonitoringResult> monitoringResults;

    @PrePersist
    public void onCreate() {
        dateOfCreation = LocalDateTime.now();
    }
}
