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

@Controller
@RequestMapping("/")
public class ModifyingLogController {
	
	@Autowired
	private ModifyingLogService modifyingLogService;
	
	@GetMapping("/reservation/{id}/modifyinglogs")
	public String showReservationModifyingLogs(@PathVariable Long id, Map<String, Object> model) {
		List<ModifyingLog> modifyingLogs = modifyingLogService.findByReservationId(id);
		if (modifyingLogs == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.put("modifyingLogs", modifyingLogs);
		return "modifying_logs";
	}
	
	@GetMapping("/preorder/{id}/modifyinglogs")
	public String showPreOrderModifyingLogs(@PathVariable Long id, Map<String, Object> model) {
		List<ModifyingLog> modifyingLogs = modifyingLogService.findByPreOrderId(id);
		if (modifyingLogs == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.put("modifyingLogs", modifyingLogs);
		return "modifying_logs";
	}

}
