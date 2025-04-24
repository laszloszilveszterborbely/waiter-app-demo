package hu.waiter.blsz.service;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import hu.waiter.blsz.model.PreOrder;

public class PreOrderSpecifications {
	
	public static Specification<PreOrder> isBetweenDates(LocalDate start, LocalDate end){
		return (root, cq, cb) -> cb.between(root.get("date"), start, end);
	}
	
	public static Specification<PreOrder> hasName(String name){
	    if (name == null || name.trim().isEmpty()) {
	        return null;
	    }
		return (root, cq, cb) -> cb.like(cb.lower(root.get("customerName")), ("%" + name + "%").toLowerCase());
	}
	
	public static Specification<PreOrder> hasPhoneNumber(Long phoneNumber){
		return (root, cq, cb) -> cb.equal(root.get("phoneNumber"), phoneNumber);
	}
}
