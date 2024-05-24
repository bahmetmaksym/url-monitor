package com.bahmet.urlmonitor.repository;

import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
    List<MonitoredEndpoint> findByUserId(String userId);

    Optional<MonitoredEndpoint> findByIdAndUserId(Long id, String userId);

    @EntityGraph(attributePaths = "monitoringResults")
    Optional<MonitoredEndpoint> findWithMonitoringResultsByIdAndUserId(Long id, String userId);

    @Query(value = "SELECT * FROM monitored_endpoints e WHERE (e.date_of_last_check IS NULL OR DATE_ADD(e.date_of_last_check, INTERVAL e.monitored_interval SECOND) <= :now) AND (e.id NOT IN (:lockedIds))", nativeQuery = true)
    List<MonitoredEndpoint> findEndpointsToCheck(@Param("now") LocalDateTime now, @Param("lockedIds") List<Long> lockedIds);

    @Query(value = "SELECT * FROM monitored_endpoints e WHERE (e.date_of_last_check IS NULL OR DATE_ADD(e.date_of_last_check, INTERVAL e.monitored_interval SECOND) <= :now)", nativeQuery = true)
    List<MonitoredEndpoint> findEndpointsToCheck(@Param("now") LocalDateTime now);
}
