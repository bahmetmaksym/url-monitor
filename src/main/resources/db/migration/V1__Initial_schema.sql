-- Creating table for MonitoredEndpoints
CREATE TABLE monitored_endpoints (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     url VARCHAR(255) NOT NULL,
                                     date_of_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     date_of_last_check DATETIME,
                                     monitored_interval BIGINT,
                                     user_id VARCHAR(255) NOT NULL
);

-- Creating table for MonitoringResults
CREATE TABLE monitoring_results (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    date_of_check DATETIME(6),
                                    http_status_code INT,
                                    payload MEDIUMTEXT,
                                    monitored_endpoint_id BIGINT,
                                    CONSTRAINT fk_monitored_endpoint
                                        FOREIGN KEY (monitored_endpoint_id)
                                            REFERENCES monitored_endpoints (id)
                                            ON DELETE CASCADE
);
