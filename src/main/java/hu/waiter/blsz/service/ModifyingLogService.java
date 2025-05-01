package hu.waiter.blsz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.waiter.blsz.model.ModifyingLog;
import hu.waiter.blsz.repository.ModifyingLogRepository;

/**
 * This service handles modifying logs.
 * 
 * Features:
 * - Return logs related to reservations or pre-orders.
 * - Delegates database operations to the repository.
 */
@Service
public class ModifyingLogService {
	
	@Autowired
	private ModifyingLogRepository modifyingLogRepository;
	
	public List<ModifyingLog> findByReservationId(long id){
		List<ModifyingLog> modifyingLogs = modifyingLogRepository.findByReservationId(id);
		return modifyingLogs;
	}
	
	public List<ModifyingLog> findByPreOrderId(long id){
		List<ModifyingLog> modifyingLogs = modifyingLogRepository.findByPreOrderId(id);
		return modifyingLogs;
	}

}
