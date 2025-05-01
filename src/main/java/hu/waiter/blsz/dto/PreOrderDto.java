package hu.waiter.blsz.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for representing pre-order data in views and forms.
 * 
 * This DTO mirrors the PreOrder entity structure.
 */
public class PreOrderDto extends RestaurantEntityDto {

	private Long id;
	private LocalDate date;
	private LocalTime time;
	private String customerName;
	private Long phoneNumber;
	private String address;
	private Boolean isDeliver;
	private String notes;
	private String courses;
	private String paymentMethod;
	private Boolean isActive;
	private Integer numberOfCourses;
	List<ModifyingLogDto> modifyingLogs = new ArrayList<>();

	public PreOrderDto() {
	}

	public PreOrderDto(LocalDate date, LocalTime time, String customerName, Long phoneNumber, String address,
			Boolean isDeliver, String notes, String courses, String paymentMethod) {
		super();
		this.date = date;
		this.time = time;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.isDeliver = (address != null && !address.isEmpty());
		this.notes = notes;
		this.courses = courses;
		this.paymentMethod = paymentMethod;
		this.isActive = true;
		this.numberOfCourses = countCourses();
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
        this.isDeliver = (address != null && !address.isEmpty());
	}

	public Boolean getIsDeliver() {
		if (isDeliver == null)
			setIsDeliver(false);
		return isDeliver;
	}

	public void setIsDeliver(Boolean isDeliver) {
		this.isDeliver = isDeliver;
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

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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

	public Integer getNumberOfCourses() {
		return countCourses();
	}

	public void setNumberOfCourses(Integer numberOfCourses) {
		this.numberOfCourses = numberOfCourses;
	}
	
	public List<ModifyingLogDto> getModifyingLogs() {
		return modifyingLogs;
	}

	public void setModifyingLogs(List<ModifyingLogDto> modifyingLogs) {
		this.modifyingLogs = modifyingLogs;
	}
	
    public void addModifyingLog(ModifyingLogDto log) {
        modifyingLogs.add(log);
        log.setPreOrderDto(this);
    }

    public void removeModifyingLog(ModifyingLogDto log) {
        modifyingLogs.remove(log);
        log.setPreOrderDto(null);
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
		PreOrderDto other = (PreOrderDto) obj;
		return Objects.equals(id, other.id);
	}

	public String toKitchen() {
		if (!isActive)
			return "";
		StringBuilder builder = new StringBuilder();
		LocalTime pickUpTime = isDeliver ? time.minusMinutes(30) : time;
		builder.append(String.format("------------%-5s-------------%n", pickUpTime.toString()));
		String shortInfo = isDeliver ? address : customerName;
		builder.append(shortInfo).append("\n");
		List<String> wrappedNotesLines = wrapText(notes, 30);
		for (String line : wrappedNotesLines) {
			builder.append(line).append("\n");
		}
		builder.append("\n");
		if (courses != null && !courses.isEmpty()) {
			String[] dishes = courses.split(";\s*");
			for (String dish : dishes) {
				List<String> wrappedDishesLines = wrapText(dish, 30);
				for (String line : wrappedDishesLines) {
					builder.append(line).append("\n");
				}
			}
		}
		builder.append("CSOMAGOLVA").append("\n");
		builder.append("------------------------------\n\n");

		return builder.toString();
	}

	public String toPrintAll() {
		if (!isActive)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("------------%-5s-------------%n", time.toString()));
		builder.append(date).append("\n");
		builder.append(customerName).append("\n");
		builder.append(phoneNumber).append("\n");
		builder.append(paymentMethod).append("\n");
		String delivery = isDeliver ? address : "Elvitel";
		builder.append(delivery).append("\n\n");
		List<String> wrappedNotesLines = wrapText(notes, 30);
		for (String line : wrappedNotesLines) {
			builder.append(line).append("\n");
		}
		builder.append("\n");
		if (courses != null && !courses.isEmpty()) {
			String[] dishes = courses.split(";\s*");
			for (String dish : dishes) {
				List<String> wrappedDishesLines = wrapText(dish, 30);
				for (String line : wrappedDishesLines) {
					builder.append(line).append("\n");
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

	private Integer countCourses() {
		if (courses != null && !courses.isEmpty()) {
			int result = 0;
			String[] items = this.courses.split(";\s*");
			for (String item : items) {
				item = item.trim();
				String[] parts = item.split(" x ");

				if (parts.length == 2) {
					int quantity = Integer.parseInt(parts[0].trim());
					result += quantity;
				}
			}
			return result;
		} else
			return 0;
	}
}
