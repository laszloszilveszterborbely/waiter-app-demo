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


/**
 * This controller handles the search function.
 * 
 * Features:
 * - Allows searching reservations and pre-orders by multiple criteria (date range, name, phone number).
 * - Supports filtering by entity type (reservation, pre-order, or both).
 * - Displays search results on the search page.
 */
@Controller
@RequestMapping("/searching")
public class SearchController {
	
	@Autowired
	ReservationService reservationService;
	@Autowired
	PreOrderService preOrderService;
	
	
	/**
	 * GET request to open search page.
	 * 
	 * @return the search view.
	 */
	@GetMapping
	public String goToSearch() {
		return "searching";
	}
	
	/**
	 * POST request to search for reservations or pre-orders.
     * 
     * Filters results by the given parameters:
     * - Type: reservation, pre-order, or both
     * - Date range: start and end
     * - Customer name
     * - Phone number
     * 
     * Results are added to the model and displayed on the search page.
     * 
	 * @param type search target ("reservation", "preorder", "both")
	 * @param start start date (optional)
	 * @param end end date (optional)
	 * @param name customer name (optional)
	 * @param phoneNumber customer phone number (optional)
	 * @param model the Thymeleaf model to pass results to the view.
	 * @return the same search view with the filled results.
	 */
	@PostMapping
		public String search(
				@RequestParam(required = false) String type, 
				@RequestParam(required = false) LocalDate start, 
				@RequestParam(required = false) LocalDate end, 
				@RequestParam(required = false) String name, 
				@RequestParam(required = false) Long phoneNumber, 
				Map<String, Object> model) {

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
