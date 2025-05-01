package hu.waiter.blsz.model;

import java.time.LocalTime;

/**
* This class represents a restaurant-related entity (Reservation, PreOrder)
* 
* Purpose:
* - Provides a shared structure for entities that can be printed for the kitchen.
* - Enables handling of different entities in calendar views.
* 
* Features:
* - Common fields: time and isActive flag.
* - Default toKitchen() method (overridden in subclasses).
* 
* TODO:
* A better alternative might be an interface (e.g., PrintableToKitchen).
*/
public class RestaurantEntity {

	private LocalTime time;
	private Boolean isActive;

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String toKitchen() {
		return "Empty entity";
	}

}
