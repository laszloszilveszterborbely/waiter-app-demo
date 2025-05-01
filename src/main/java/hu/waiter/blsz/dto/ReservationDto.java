package hu.waiter.blsz.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.waiter.blsz.model.ModifyingLog;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

/**
 * Data Transfer Object (DTO) for representing reservation data in views and forms.
 * 
 * This DTO mirrors the Reservation entity structure.
 */
public class ReservationDto extends RestaurantEntityDto {

	private Long id;
	private LocalDate date;
	private LocalTime time;
	private String name;
	private Long phoneNumber;
	private Integer numberOfPeople;
	private String occasion;
	private Boolean isPayDeposit;
	private Boolean isOrderedMeal;
	private Boolean needHighChair;
	private Boolean isThereCake;
	private String notes;
	private String courses;
	private String tableNumber;
	private Boolean isActive;
	
    private List<ModifyingLogDto> modifyingLogs = new ArrayList<>();

	public ReservationDto() {
	}

	public ReservationDto(LocalDate date, LocalTime time, String name, Long phoneNumber, Integer numberOfPeople,
			String notes) {
		super();
		this.date = date;
		this.time = time;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.numberOfPeople = numberOfPeople;
		this.isPayDeposit = false;
		this.notes = notes;
		this.isActive = true;
	}

	public ReservationDto(LocalDate date, LocalTime time, String name, Long phoneNumber, Integer numberOfPeople,
			String occasion, Boolean isPayDeposit, Boolean isOrderedMeal, Boolean needHighChair, Boolean isThereCake,
			String notes, String courses, String tableNumber) {
		super();
		this.date = date;
		this.time = time;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.numberOfPeople = numberOfPeople;
		this.occasion = occasion;
		this.isPayDeposit = isPayDeposit;
		this.isOrderedMeal = isOrderedMeal;
		this.needHighChair = needHighChair;
		this.isThereCake = isThereCake;
		this.notes = notes;
		this.courses = courses;
		this.tableNumber = tableNumber;
		this.isActive = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public LocalTime getTime() {
		return time;
	}

	@Override
	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(Integer numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}

	public Boolean getIsPayDeposit() {
		if (isPayDeposit == null)
			setIsPayDeposit(false);
		return isPayDeposit;
	}

	public void setIsPayDeposit(Boolean isPayDeposit) {
		this.isPayDeposit = isPayDeposit;
	}

	public Boolean getIsOrderedMeal() {
		if (isOrderedMeal == null)
			setIsOrderedMeal(false);
		return isOrderedMeal;
	}

	public void setIsOrderedMeal(Boolean isOrderedMeal) {
		this.isOrderedMeal = isOrderedMeal;
	}

	public Boolean getNeedHighChair() {
		if (needHighChair == null)
			setNeedHighChair(false);
		return needHighChair;
	}

	public void setNeedHighChair(Boolean needHighChair) {
		this.needHighChair = needHighChair;
	}

	public Boolean getIsThereCake() {
		if (isThereCake == null)
			setIsThereCake(false);
		return isThereCake;
	}

	public void setIsThereCake(Boolean isThereCake) {
		this.isThereCake = isThereCake;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCourses() {
		return courses;
	}

	public void setCourses(String courses) {
		this.courses = courses;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	@Override
	public Boolean getIsActive() {
		if (isActive == null)
			setIsActive(true);
		return isActive;
	}

	@Override
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
    public List<ModifyingLogDto> getModifyingLogs() {
		return modifyingLogs;
	}

	public void setModifyingLogs(List<ModifyingLogDto> modifyingLogs) {
		this.modifyingLogs = modifyingLogs;
	}

	public void addModifyingLog(ModifyingLogDto log) {
        modifyingLogs.add(log);
        log.setReservationDto(this);
    }

    public void removeModifyingLog(ModifyingLogDto log) {
        modifyingLogs.remove(log);
        log.setReservationDto(null);
    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservationDto other = (ReservationDto) obj;
		return Objects.equals(id, other.id);
	}

	public String toKitchen() {
		if (!isActive)
			return "";
		if (!isOrderedMeal && (numberOfPeople < 10))
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("------------%-5s-------------%n", time.toString()));

		builder.append(name).append("\n");
		builder.append(numberOfPeople).append(" fő\n");
		if (tableNumber != null && !tableNumber.isEmpty())
		builder.append(tableNumber).append("\n");
		builder.append("\n");
		if (!isOrderedMeal) {
			builder.append("Nincs leadva étel.\n");
		} else {
			if (courses != null && !courses.isEmpty()) {
				String[] dishes = courses.split(";\s*");
				for (String dish : dishes) {
					List<String> wrappedDishesLines = wrapText(dish, 30);
					for (String line : wrappedDishesLines) {
						builder.append(line).append("\n");
					}
				}
			}
		}
		List<String> wrappedNotesLines = wrapText(notes, 30);
		for (String line : wrappedNotesLines) {
			builder.append(line).append("\n");
		}
		builder.append("------------------------------\n\n");

		return builder.toString();
	}

	public String toCounter() {
		if (!isActive)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("------------%-5s-------------%n", time.toString()));

		builder.append(name).append("\n");
		builder.append(numberOfPeople).append(" fő\n");
		builder.append(occasion).append("\n");
		String orderedMeal = isOrderedMeal ? "Étel leadve\n" : "Nincs étel leadva\n";
		builder.append(orderedMeal);
		String deposit = isPayDeposit ? "Foglaló fizetve\n" : "";
		builder.append(deposit);
		String cake = isThereCake ? "Tortát is hoz\n" : "";
		builder.append(cake);
		String highChair = needHighChair ? "Etetőszékre lesz szükség\n" : "";
		builder.append(highChair).append("\n");
		List<String> wrappedNotesLines = wrapText(notes, 30);
		for (String line : wrappedNotesLines) {
			builder.append(line).append("\n");
		}
		builder.append("------------------------------\n\n");

		return builder.toString();
	}

	public String toPrintAll() {
		if (!isActive)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("------------%-5s-------------%n", time.toString()));
		builder.append(date).append("\n");
		builder.append(name).append("\n");
		builder.append(phoneNumber).append("\n");
		builder.append(numberOfPeople).append(" fő\n");
		builder.append(occasion).append("\n\n");
		String deposit = isPayDeposit ? "Foglaló fizetve\n" : "";
		builder.append(deposit);
		String cake = isThereCake ? "Tortát is hoz\n" : "";
		builder.append(cake);
		String highChair = needHighChair ? "Etetőszékre lesz szükség\n" : "";
		builder.append(highChair).append("\n");
		List<String> wrappedNotesLines = wrapText(notes, 30);
		for (String line : wrappedNotesLines) {
			builder.append(line).append("\n");
		}
		builder.append("\n");
		if (!isOrderedMeal) {
			builder.append("Nincs leadva étel.\n");
		} else {
			if (courses != null && !courses.isEmpty()) {
				String[] dishes = courses.split(";\s*");
				for (String dish : dishes) {
					List<String> wrappedDishesLines = wrapText(dish, 30);
					for (String line : wrappedDishesLines) {
						builder.append(line).append("\n");
					}
				}
			}
		}
		builder.append("------------------------------\n\n");

		return builder.toString();
	}

	private List<String> wrapText(String text, int maxWidth) {
		List<String> lines = new ArrayList<>();
		int start = 0;
		while (start < text.length()) {
			int end = Math.min(start + maxWidth, text.length());
			lines.add(text.substring(start, end));
			start = end;
		}
		return lines;
	}
}
