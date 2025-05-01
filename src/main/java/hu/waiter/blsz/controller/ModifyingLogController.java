package hu.waiter.blsz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import hu.waiter.blsz.dto.ModifyingLogDto;
import hu.waiter.blsz.mapper.ModifyingLogMapper;
import hu.waiter.blsz.model.ModifyingLog;
import hu.waiter.blsz.service.ModifyingLogService;

/**
 * This controller displays modifying logs.
 * 
 * Features:
 * - Showing modification history for reservations and pre-orders.
 */
@Controller
@RequestMapping("/")
public class ModifyingLogController {
	
	@Autowired
	private ModifyingLogService modifyingLogService;
	
	
	/**
	 * GET request to show the modification history of a specific reservation.
	 * 
	 * @param id the reservation ID.
	 * @param model the Thymeleaf model to populate.
	 * @return the modifying logs view.
	 */
	@GetMapping("/reservation/{id}/modifyinglogs")
	public String showReservationModifyingLogs(@PathVariable Long id, Map<String, Object> model) {
		List<ModifyingLog> modifyingLogs = modifyingLogService.findByReservationId(id);
		if (modifyingLogs == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.put("modifyingLogs", modifyingLogs);
		return "modifying_logs";
	}
	
	/**
	 * GET request to show the modification history of a specific pre-order. 
	 * 
	 * @param id the pre-order ID.
	 * @param model the Thymeleaf model to populate.
	 * @return the modifying logs view.
	 */
	@GetMapping("/preorder/{id}/modifyinglogs")
	public String showPreOrderModifyingLogs(@PathVariable Long id, Map<String, Object> model) {
		List<ModifyingLog> modifyingLogs = modifyingLogService.findByPreOrderId(id);
		if (modifyingLogs == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.put("modifyingLogs", modifyingLogs);
		return "modifying_logs";
	}

}
