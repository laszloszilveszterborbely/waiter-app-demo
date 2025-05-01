package hu.waiter.blsz.dto;

import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for representing restaurant-related entity data in views and forms.
 * 
 * This DTO mirrors the PreOrder entity structure.
 */
public class RestaurantEntityDto {

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
