package com.bahmet.urlmonitor.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "monitoring_results")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_of_check")
    private LocalDateTime dateOfCheck;

    @Column(name = "http_status_code")
    private Integer httpStatusCode;

    @Column(name = "payload", columnDefinition = "MEDIUMTEXT")
    private String payload;

    @ManyToOne
    @JoinColumn(name = "monitored_endpoint_id")
    private MonitoredEndpoint monitoredEndpoint;
}
