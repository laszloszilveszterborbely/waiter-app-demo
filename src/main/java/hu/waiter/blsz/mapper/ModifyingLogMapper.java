package hu.waiter.blsz.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.waiter.blsz.dto.ModifyingLogDto;
import hu.waiter.blsz.model.ModifyingLog;

@Mapper(componentModel = "spring")
public interface ModifyingLogMapper {
	
	@Mapping(target = "reservationDto", ignore = true)
	@Mapping(target = "preOrderDto", ignore = true)
	public ModifyingLogDto modifyingLogToDto(ModifyingLog modifyingLog);
	
	public List<ModifyingLogDto> modifyingLogsToDtos(List<ModifyingLog> modifyingLogs);
	
	@Mapping(target = "reservation", ignore = true)
	@Mapping(target = "preOrder", ignore = true)
	public ModifyingLog dtoToModifyingLog(ModifyingLogDto modifyingLogDto);
	
	public List<ModifyingLog> modifyingLogs(List<ModifyingLogDto> modifyingLogDtos);

}
