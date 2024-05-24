package com.bahmet.urlmonitor.util.mapper;

//import com.bahmet.urlmonitor.dto.MonitoredEndpointDto;
import com.bahmet.urlmonitor.dto.MonitoredEndpoint.MonitoredEndpointRequestDTO;
import com.bahmet.urlmonitor.dto.MonitoredEndpoint.MonitoredEndpointResponseDTO;
import com.bahmet.urlmonitor.dto.MonitoredEndpoint.MonitoredEndpointWithResultsResponseDTO;
import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = MonitoringResultMapper.class)
public interface MonitoredEndpointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfCreation", ignore = true)
    @Mapping(target = "dateOfLastCheck", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "monitoringResults", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "monitoredInterval", source = "monitoredInterval")
    MonitoredEndpoint toEntity(MonitoredEndpointRequestDTO dto);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "dateOfCreation", source = "dateOfCreation")
    @Mapping(target = "dateOfLastCheck", source = "dateOfLastCheck")
    @Mapping(target = "monitoredInterval", source = "monitoredInterval")
    MonitoredEndpointResponseDTO toResponseDTO(MonitoredEndpoint entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "dateOfCreation", source = "dateOfCreation")
    @Mapping(target = "dateOfLastCheck", source = "dateOfLastCheck")
    @Mapping(target = "monitoredInterval", source = "monitoredInterval")
    @Mapping(target = "monitoringResults", source = "monitoringResults")
    MonitoredEndpointWithResultsResponseDTO toResponseDTOWithResults(MonitoredEndpoint entity);


    List<MonitoredEndpoint> toEntity(List<MonitoredEndpointRequestDTO> dtos);


    List<MonitoredEndpointResponseDTO> toResponseDTO(List<MonitoredEndpoint> entities);


    MonitoredEndpoint toEntity(MonitoredEndpointResponseDTO monitoredEndpointResponseDTO);
}
