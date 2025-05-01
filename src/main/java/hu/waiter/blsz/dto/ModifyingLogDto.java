package hu.waiter.blsz.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for representing modification log entry data in views and forms.
 * 
 * This DTO mirrors the ModifyingLog entity structure.
 */
public class ModifyingLogDto {

	private Long id;
	private String monogram;
	private String event;
	private String reason;
	private LocalDate timestamp = LocalDate.now();

	private ReservationDto reservationDto;

	private PreOrderDto preOrderDto;

	public ModifyingLogDto() {
	}

	public ModifyingLogDto(String monogram) {
		super();
		this.monogram = monogram;
		this.event = "NA";
		this.reason = "NA";
		this.timestamp = LocalDate.now();
	}

	public ModifyingLogDto(String monogram, String event) {
		super();
		this.monogram = monogram;
		this.event = event;
		this.reason = "";
		this.timestamp = LocalDate.now();
	}

	public ModifyingLogDto(String monogram, String event, String reason) {
		super();
		this.monogram = monogram;
		this.event = event;
		this.reason = reason;
		this.timestamp = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMonogram() {
		return monogram;
	}

	public void setMonogram(String monogram) {
		this.monogram = monogram;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public ReservationDto getReservationDto() {
		return reservationDto;
	}

	public void setReservationDto(ReservationDto reservationDto) {
		this.reservationDto = reservationDto;
	}

	public PreOrderDto getPreOrderDto() {
		return preOrderDto;
	}

	public void setPreOrderDto(PreOrderDto preOrderDto) {
		this.preOrderDto = preOrderDto;
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
		ModifyingLogDto other = (ModifyingLogDto) obj;
		return Objects.equals(id, other.id);
	}

}
