package hu.waiter.blsz.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.waiter.blsz.dto.ModifyingLogDto;
import hu.waiter.blsz.model.ModifyingLog;

@Mapper(componentModel = "spring")
public interface ModifyingLogMapper {
	
	public ModifyingLogDto modifyingLogToDto(ModifyingLog modifyingLog);
	
	public List<ModifyingLogDto> modifyingLogsToDtos(List<ModifyingLog> modifyingLogs);
	
	public ModifyingLog dtoToModifyingLog(ModifyingLogDto modifyingLogDto);
	
	public List<ModifyingLog> modifyingLogs(List<ModifyingLogDto> modifyingLogDtos);

}
