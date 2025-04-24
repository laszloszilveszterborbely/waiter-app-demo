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

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PreOrderService preOrderService;

    
    @GetMapping
    public String calendarPage() {
    	return "calendar"; 
    	}

    
    @PostMapping
    public String handleDateSelection(@RequestParam String date, Map<String, Object> model) {
        LocalDate selectedDate = LocalDate.parse(date);

        List<Reservation> reservations = reservationService.findByDate(selectedDate)
                .stream()
                .sorted(Comparator.comparing(Reservation::getIsActive).reversed()
                		.thenComparing(Reservation::getTime))
                .collect(Collectors.toList());

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

    
    @GetMapping("print_to_counter")
    @ResponseBody
    public String printToCounter(@RequestParam String date, Map<String, Object> model) {
    	LocalDate selectedDate = LocalDate.parse(date);
    	
        List<Reservation> reservations = reservationService.findByDate(selectedDate)
                .stream()
                .sorted(Comparator.comparing(Reservation::getTime))
                .collect(Collectors.toList());
        
        model.put("selectedDate", selectedDate);
        
        StringBuilder builder = new StringBuilder();
        reservations.stream()
        		.forEach(r -> builder.append(r.toCounter()));
        
        return builder.toString();
    }
    
    
    @GetMapping("print_to_kitchen")
    @ResponseBody
    public String printToKitchen(@RequestParam String date, Map<String, Object> model) {
    	LocalDate selectedDate = LocalDate.parse(date);
    	
    	List<Reservation> reservations = reservationService.findByDate(selectedDate);
    	List<PreOrder> preOrders = preOrderService.findByDate(selectedDate);

    	List<RestaurantEntity> restaurantEntities = new ArrayList<>();
    	restaurantEntities.addAll(reservations);
    	restaurantEntities.addAll(preOrders);
    	restaurantEntities.sort(Comparator.comparing(RestaurantEntity::getTime));
    	
    	model.put("selectedDate", selectedDate);
    	
    	StringBuilder builder = new StringBuilder();
    	restaurantEntities.stream()
    			.forEach(r -> builder.append(r.toKitchen()));
    	
    	return builder.toString();
    }
}
