package hu.waiter.blsz.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.waiter.blsz.dto.PreOrderDto;
import hu.waiter.blsz.model.PreOrder;

@Mapper(componentModel = "spring", uses = ModifyingLogMapper.class)
public interface PreOrderMapper {
	
	public PreOrderDto preOrderToDto(PreOrder preOrder);
	
	public List<PreOrderDto> preOrdersToDtos(List<PreOrder> preOrders);
	
	public PreOrder dtoToPreOrder(PreOrderDto preOrderDto);
	
	public List<PreOrder> dtosToPreOrders(List<PreOrderDto> preOrderDtos);

}
