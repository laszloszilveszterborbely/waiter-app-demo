package hu.waiter.blsz.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ModifyingLog {

	@Id
	@GeneratedValue
	private Long id;
	private String monogram;
	private String event;
	private String reason;
	private LocalDate timestamp = LocalDate.now();

	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	@ManyToOne
	@JoinColumn(name = "preorder_id")
	private PreOrder preOrder;

	public ModifyingLog() {
	}

	public ModifyingLog(String monogram) {
		super();
		this.monogram = monogram;
		this.event = "NA";
		this.reason = "NA";
		this.timestamp = LocalDate.now();
	}

	public ModifyingLog(String monogram, String event) {
		super();
		this.monogram = monogram;
		this.event = event;
		this.reason = "";
		this.timestamp = LocalDate.now();
	}

	public ModifyingLog(String monogram, String event, String reason) {
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

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public PreOrder getPreOrder() {
		return preOrder;
	}

	public void setPreOrder(PreOrder preOrder) {
		this.preOrder = preOrder;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
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
		ModifyingLog other = (ModifyingLog) obj;
		return Objects.equals(id, other.id);
	}

}
