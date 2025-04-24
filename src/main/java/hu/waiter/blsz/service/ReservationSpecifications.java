package hu.waiter.blsz.service;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import hu.waiter.blsz.model.Reservation;

public class ReservationSpecifications {
	
	public static Specification<Reservation> isBetweenDates(LocalDate start, LocalDate end){
		return (root, cq, cb) -> cb.between(root.get("date"), start, end);
	}
	
	public static Specification<Reservation> hasName(String name){
	    if (name == null || name.trim().isEmpty()) {
	        return null;
	    }
		return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), ("%" + name + "%").toLowerCase());
	}
	
	public static Specification<Reservation> hasPhoneNumber(Long phoneNumber){
		try{
			return (root, cq, cb) -> cb.equal(root.get("phoneNumber"), phoneNumber);
		} catch (Exception e) {
			return null;
		}
	}
}