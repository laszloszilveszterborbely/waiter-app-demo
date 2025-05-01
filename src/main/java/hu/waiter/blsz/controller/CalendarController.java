package hu.waiter.blsz.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import hu.waiter.blsz.dto.ModifyingLogDto;
import hu.waiter.blsz.dto.PreOrderDto;
import hu.waiter.blsz.dto.ReservationDto;
import hu.waiter.blsz.mapper.ModifyingLogMapper;
import hu.waiter.blsz.mapper.PreOrderMapper;
import hu.waiter.blsz.mapper.ReservationMapper;
import hu.waiter.blsz.model.ModifyingLog;
import hu.waiter.blsz.model.PreOrder;
import hu.waiter.blsz.model.Reservation;
import hu.waiter.blsz.model.RestaurantEntity;
import hu.waiter.blsz.repository.PreOrderRepository;
import hu.waiter.blsz.repository.ReservationRepository;
import hu.waiter.blsz.service.ModifyingLogService;
import hu.waiter.blsz.service.PreOrderService;
import hu.waiter.blsz.service.ReservationService;

/**
 * This controller handles the calendar view.
 * 
 * Features:
 * - Displays reservations and pre-orders for a selected date.
 * - Provides printable lists for counter and kitchen.
 */
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PreOrderService preOrderService;

    
	/**
	 * Default GET request to open the calendar page. 
	 * Only returns the calendar template (calendar.html).
	 */
   @GetMapping
    public String calendarPage() {
    	return "calendar"; 
    	}

    
	/**
	 * POST request for selecting a date. Loads all reservations and pre-orders for the given day into the model.
	 *
	 * @param date  The selected date (in String format)
	 * @param model The Thymeleaf model to pass data to the view
	 * @return The calendar.html template
	 */
    @PostMapping
    public String handleDateSelection(@RequestParam String date, Map<String, Object> model) {
        LocalDate selectedDate = LocalDate.parse(date);

        // Load and sort reservations: active ones first, then by time
        List<Reservation> reservations = reservationService.findByDate(selectedDate)
                .stream()
                .sorted(Comparator.comparing(Reservation::getIsActive).reversed()
                		.thenComparing(Reservation::getTime))
                .collect(Collectors.toList());

        // Load and sort pre-orders: active ones first, then by time
        List<PreOrder> preOrders = preOrderService.findByDate(selectedDate)
                .stream()
                .sorted(Comparator.comparing(PreOrder::getIsActive).reversed()
                		.thenComparing(PreOrder::getTime))
                .collect(Collectors.toList());

        model.put("selectedDate", selectedDate);
        model.put("reservations", reservations);
        model.put("preOrders", preOrders);

        return "calendar";
    }

	/**
	 * GET request to generate the printable list for the counter. 
	 * Only includes reservations (no pre-orders).
	 *
	 * @param date  The selected date
	 * @return The generated text for the counter
	 */
    @GetMapping("print_to_counter")
    @ResponseBody
    public String printToCounter(@RequestParam String date) {
    	LocalDate selectedDate = LocalDate.parse(date);
    	
        List<Reservation> reservations = reservationService.findByDate(selectedDate)
                .stream()
                .sorted(Comparator.comparing(Reservation::getTime))
                .collect(Collectors.toList());
        
        // Build the text with StringBuilder
        StringBuilder builder = new StringBuilder();
        reservations.stream()
        		.forEach(r -> builder.append(r.toCounter())); // toCounter(): generates formatted text for the counter
        
        return builder.toString();
    }
    
	/**
	 * GET request to generate the printable list for the kitchen. 
	 * Includes both reservations and pre-orders.
	 *
	 * @param date  The selected date
	 * @return The generated text for the kitchen
	 */
    @GetMapping("print_to_kitchen")
    @ResponseBody
    public String printToKitchen(@RequestParam String date) {
    	LocalDate selectedDate = LocalDate.parse(date);
    	
    	List<Reservation> reservations = reservationService.findByDate(selectedDate);
    	List<PreOrder> preOrders = preOrderService.findByDate(selectedDate);

    	List<RestaurantEntity> restaurantEntities = new ArrayList<>();
    	restaurantEntities.addAll(reservations);
    	restaurantEntities.addAll(preOrders);
    	
    	// Sort all entities by time
    	restaurantEntities.sort(Comparator.comparing(RestaurantEntity::getTime));

        // Build the text with StringBuilder
    	StringBuilder builder = new StringBuilder();
    	restaurantEntities.stream()
    			.forEach(r -> builder.append(r.toKitchen())); // toKitchen(): generates formatted text for the kitchen
    	
    	return builder.toString();
    }
}
