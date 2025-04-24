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

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ModifyingLogMapper modifyingLogMapper;


    @GetMapping("/create")
    public String reservationForm(Map<String, Object> model) {
        model.put("reservation", new ReservationDto());
        return "reservation_create";
    }
    

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
    
    
    @GetMapping("/{id}")
    public String showReservationDetails(@PathVariable long id, Map<String, Object> model) {
        Reservation reservation = reservationService.findById(id);
        if (reservation == null)
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.put("reservation", reservation);
        return "reservation_details";
    }
    
    @GetMapping("/{id}/edit")
    public String showReservationEditor(@PathVariable long id, Map<String, Object> model) {
    	Reservation reservation = reservationService.findById(id);
    	 if (reservation == null)
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	model.put("reservation", reservation);
    	return "reservation_edit";
    }
    
    
    @GetMapping("/{id}/print")
    @ResponseBody
    public String printReservation(@PathVariable long id) {
        Reservation reservation = reservationService.findById(id);
        if (reservation == null)
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return reservation.toPrintAll();
    }
    
    
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
    
    
    @GetMapping("/{id}/cancel")
    public String showCancelReservation(@PathVariable long id, Map<String, Object> model) {
    	Reservation reservation = reservationService.findById(id);
    	if (reservation == null)
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	model.put("reservation", reservation);
    	model.put("modifyingLog", new ModifyingLogDto());
    	return "reservation_cancel";
    }
    
    
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