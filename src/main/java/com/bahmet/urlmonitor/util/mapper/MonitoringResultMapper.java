package com.bahmet.urlmonitor.util.mapper;

import com.bahmet.urlmonitor.dto.MonitoringResult.MonitoringResultResponseDTO;
import com.bahmet.urlmonitor.repository.model.MonitoringResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MonitoringResultMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateOfCheck", source = "dateOfCheck")
    @Mapping(target = "httpStatusCode", source = "httpStatusCode")
    @Mapping(target = "payload", source = "payload")
    MonitoringResultResponseDTO toResponseDTO(MonitoringResult entity);
}
