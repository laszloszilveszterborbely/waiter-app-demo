package hu.waiter.blsz.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.waiter.blsz.model.Reservation;
import hu.waiter.blsz.repository.ReservationRepository;

/**
 * This service handles reservation-related operations.
 * 
 * Features:
 * - Saving, updating, and finding reservations.
 * - Searching reservations by date or multiple criteria (name, phone number, date range).
 */
@Service
public class ReservationService {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Transactional
	public Reservation save(Reservation reservation) {
		return reservationRepository.save(reservation);
	}
	
	public Reservation findById(long id) {
		return reservationRepository.findById(id).orElse(null);
	}
	
	public List<Reservation> findByDate(LocalDate date){
		return reservationRepository.findByDate(date);
	}
	
	public List<Reservation> findReservationByExample(LocalDate start, LocalDate end, String name, Long phoneNumber){
		Specification<Reservation> spec = Specification.where(null);
		
		if (start != null && end != null)
			spec = spec.and(ReservationSpecifications.isBetweenDates(start, end));
		if (StringUtils.hasText(name))
			spec = spec.and(ReservationSpecifications.hasName(name));
		if (phoneNumber != null)
			spec = spec.and(ReservationSpecifications.hasPhoneNumber(phoneNumber));
		
		return reservationRepository.findAll(spec, Sort.by("date"));
	}
	
	@Transactional
	public Reservation create(Reservation reservation) {
		if (findById(reservation.getId()) != null)
			return null;
		return save(reservation);
	}
	
	@Transactional
	public Reservation update(Reservation reservation) {
		if (findById(reservation.getId()) == null)
			return null;
		return save(reservation);
	}
}