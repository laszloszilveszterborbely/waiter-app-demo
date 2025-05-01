package hu.waiter.blsz.controller;

import java.util.Map;

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
import hu.waiter.blsz.dto.ReservationDto;
import hu.waiter.blsz.mapper.ModifyingLogMapper;
import hu.waiter.blsz.mapper.ReservationMapper;
import hu.waiter.blsz.model.ModifyingLog;
import hu.waiter.blsz.model.Reservation;
import hu.waiter.blsz.service.ReservationService;

/**
 * This controller manages reservations.
 * 
 * Features:
 * - Creating, editing, and cancelling reservations.
 * - Displaying and printing reservation details.
 * - Recording modification logs for changes.
 */
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ModifyingLogMapper modifyingLogMapper;

    
	/**
	 * GET request to open the reservation creation page.
	 * 
	 * @param model the Thymeleaf model to populate with an empty ReservationDto.
	 * @return the reservation creation form view.
	 */
    @GetMapping("/create")
    public String reservationForm(Map<String, Object> model) {
        model.put("reservation", new ReservationDto());
        return "reservation_create";
    }
    
    
	/**
	 * POST request to save a new reservation into the database.
	 * 
	 * Adds a log entry for creation.
	 * 
	 * @param reservationDto the filled reservation data from the form.
	 * @param monogram the monogram of the staff member who created the reservation.
	 * @return view that closes the form page.
	 */
    @PostMapping("/create")
    public String saveReservation(@ModelAttribute ReservationDto reservationDto, @RequestParam String monogram) {
    	ModifyingLog modifyingLog = new ModifyingLog(monogram, "létrehozás");
    	    	
    	Reservation reservation = reservationMapper.dtoToReservation(reservationDto);
    	reservation.addModifyingLog(modifyingLog);
    	Reservation savedReservation = reservationService.save(reservation);
    	
    	if (savedReservation == null)
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    	
        return "close_page";
    }
    

	/**
	 * GET request to show the details of a specific reservation.
	 * 
	 * @param id the reservation ID.
	 * @param model the Thymeleaf model to populate.
	 * @return the reservation details view.
	 */
    @GetMapping("/{id}")
    public String showReservationDetails(@PathVariable long id, Map<String, Object> model) {
        Reservation reservation = reservationService.findById(id);
        if (reservation == null)
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.put("reservation", reservation);
        return "reservation_details";
    }
    
	/**
	 * GET request to open the edit page for a specific reservation.
	 * 
	 * @param id the reservation ID.
	 * @param model the Thymeleaf model to populate with the reservation data.
	 * @return the reservation editing form view.
	 */
    @GetMapping("/{id}/edit")
    public String showReservationEditor(@PathVariable long id, Map<String, Object> model) {
    	Reservation reservation = reservationService.findById(id);
    	 if (reservation == null)
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	model.put("reservation", reservation);
    	return "reservation_edit";
    }
    

	/**
	 * GET request to generate a printable version of the reservation details.
	 * 
	 * @param id the reservation ID.
	 * @return a text representation of the reservation (formatted for printing).
	 */
    @GetMapping("/{id}/print")
    @ResponseBody
    public String printReservation(@PathVariable long id) {
        Reservation reservation = reservationService.findById(id);
        if (reservation == null)
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return reservation.toPrintAll();
    }
    

	/**
	 * POST request to update an existing reservation.
	 * 
	 * Adds a log entry for modification.
	 * 
	 * @param reservationDto the updated reservation data.
	 * @param monogram the monogram of the staff member who modified the reservation.
	 * @return redirects to the home page after saving.
	 */
    @PostMapping("/update")
    public String updateReservation(@ModelAttribute ReservationDto reservationDto, @RequestParam String monogram) {
    	
    	Reservation reservation = reservationMapper.dtoToReservation(reservationDto);
    	Reservation originalReservation = reservationService.findById(reservation.getId());
    	if (originalReservation == null)
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	
    	originalReservation.setDate(reservation.getDate());
    	originalReservation.setTime(reservation.getTime());
    	originalReservation.setName(reservation.getName());
    	originalReservation.setPhoneNumber(reservation.getPhoneNumber());
    	originalReservation.setNumberOfPeople(reservation.getNumberOfPeople());
    	originalReservation.setOccasion(reservation.getOccasion());
    	originalReservation.setIsPayDeposit(reservation.getIsPayDeposit());
    	originalReservation.setIsOrderedMeal(reservation.getIsOrderedMeal());
    	originalReservation.setNeedHighChair(reservation.getNeedHighChair());
    	originalReservation.setIsThereCake(reservation.getIsThereCake());
    	originalReservation.setNotes(reservation.getNotes());
    	originalReservation.setCourses(reservation.getCourses());
    	originalReservation.setTableNumber(reservation.getTableNumber());
    	
    	ModifyingLog modifyingLog = new ModifyingLog(monogram, "módosítás");
    	originalReservation.addModifyingLog(modifyingLog);
    	
    	reservationService.update(originalReservation);
    	
    	return "redirect:/";
    }
    
    
	/**
	 * GET request to open the cancel reservation page.
	 * 
	 * @param id the reservation ID.
	 * @param model the Thymeleaf model to populate.
	 * @return the reservation cancel form view.
	 */
    @GetMapping("/{id}/cancel")
    public String showCancelReservation(@PathVariable long id, Map<String, Object> model) {
    	Reservation reservation = reservationService.findById(id);
    	if (reservation == null)
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	model.put("reservation", reservation);
    	model.put("modifyingLog", new ModifyingLogDto());
    	return "reservation_cancel";
    }
    
    
	/**
	 * POST request to cancel a reservation by marking it inactive.
	 * 
	 * Adds a log entry for cancellation.
	 * 
	 * @param id the reservation ID.
	 * @param modifyingLogDto the cancellation reason and staff member monogram.
	 * @return redirects to the home page after canceling.
	 */
    @PostMapping("/{id}/cancel")
    public String cancelReservation(@PathVariable long id, @ModelAttribute ModifyingLogDto modifyingLogDto) {
    	modifyingLogDto.setId(null);
    	Reservation reservation = reservationService.findById(id);
    	if (reservation == null)
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	ModifyingLog modifyingLog = modifyingLogMapper.dtoToModifyingLog(modifyingLogDto);
    	reservation.addModifyingLog(modifyingLog);
    		
    	reservation.setIsActive(false);
    	
    	reservationService.update(reservation);
    	
    	return "redirect:/";
    }
    
}