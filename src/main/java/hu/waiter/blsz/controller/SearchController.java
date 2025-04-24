package hu.waiter.blsz.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hu.waiter.blsz.model.PreOrder;
import hu.waiter.blsz.model.Reservation;
import hu.waiter.blsz.service.PreOrderService;
import hu.waiter.blsz.service.ReservationService;


@Controller
@RequestMapping("/searching")
public class SearchController {
	
	@Autowired
	ReservationService reservationService;
	@Autowired
	PreOrderService preOrderService;
	
	
	@GetMapping
	public String goToSearch() {
		return "searching";
	}
	
	
	@PostMapping
		public String search(@RequestParam(required = false) String type, @RequestParam(required = false) LocalDate start, @RequestParam(required = false) LocalDate end, @RequestParam(required = false) String name, @RequestParam(required = false) Long phoneNumber, Map<String, Object> model) {

        if (type == null || type.equals("both") || type.equals("reservation")) {
            List<Reservation> reservations = reservationService.findReservationByExample(start, end, name, phoneNumber);
            model.put("reservations", reservations);
        }
        if (type == null || type.equals("both") || type.equals("preorder")) {
            List<PreOrder> preOrders = preOrderService.findPreOrderByExample(start, end, name, phoneNumber);
            model.put("preorders", preOrders);
        }
        return "searching";
	}
}
