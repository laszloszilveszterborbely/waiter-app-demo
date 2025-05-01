package hu.waiter.blsz.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * This class represents a meal pre-order.
 * 
 * Features:
 * - Stores all relevant data about a pre-order.
 * - Supports modification logging.
 * - Provides formatted print views for kitchen staff use.
 * 
 *  * Usage:
 * Pre-orders are used when the guest orders food in advance, optionally requesting delivery.
 * The print format is adapted for a 30-character-wide thermal receipt printer.
 * Inactive pre-orders are skipped when generating printed output.
 */
@Entity
public class PreOrder extends RestaurantEntity {

	@Id
	@GeneratedValue
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

	@OneToMany(mappedBy = "preOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ModifyingLog> modifyingLogs = new ArrayList<>();

	public PreOrder() {
	}

	public PreOrder(LocalDate date, LocalTime time, String customerName, Long phoneNumber, String address, String notes,
			String courses, String paymentMethod) {
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

	public List<ModifyingLog> getModifyingLogs() {
		return modifyingLogs;
	}

	public void setModifyingLogs(List<ModifyingLog> modifyingLogs) {
		this.modifyingLogs = modifyingLogs;
	}

	public void addModifyingLog(ModifyingLog log) {
		modifyingLogs.add(log);
		log.setPreOrder(this);
	}

	public void removeModifyingLog(ModifyingLog log) {
		modifyingLogs.remove(log);
		log.setPreOrder(null);
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
		PreOrder other = (PreOrder) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * Generates a printable string for kitchen staff.
	 * 
	 * Includes only pre-orders that are active.
	 * Displays basic informations.
	 * 
	 * The output format fits a 30-character-wide receipt printer.
	 */
	@Override
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

	
	/**
	 * Generates a printable string for full overview.
	 * 
	 * Includes reservations that are active. 
	 * Displays all informations.
	 * 
	 * The output format fits a 30-character-wide receipt printer.
	 */
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

	/**
	 * Helper method that splits a longer string into lines of maximum width.
	 * 
	 * Used to ensure text fits the receipt printer width.
	 * 
	 * @param text the full string to wrap
	 * @param maxWidth the maximum character per line
	 * @return a list of wrapped text lines
	 */
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