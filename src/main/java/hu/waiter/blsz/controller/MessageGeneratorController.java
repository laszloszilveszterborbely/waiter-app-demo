package hu.waiter.blsz.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.waiter.blsz.dto.ModifyingLogDto;
import hu.waiter.blsz.dto.ReservationDto;
import hu.waiter.blsz.mapper.ReservationMapper;
import hu.waiter.blsz.model.MessageRequest;
import hu.waiter.blsz.model.Reservation;
import hu.waiter.blsz.service.ReservationService;

/**
 * This controller handles generating reply messages for incoming online table reservations.
 * 
 * Context:
 * Incoming reservation requests are submitted by guests via the restaurant's official website, 
 * through a standardized form. These submissions generate automatic emails with a fixed, predictable format.
 * 
 * Features:
 * - Parses incoming reservation details from the standardized email text (pasted manually by staff).
 * - Generates a ready-to-send confirmation (accept) or rejection (reject) message based on parsed data.
 * - Optionally appends information about opening hours and HACCP certificate requirements to the message.
 * - Automatically saves confirmed reservations into the internal system upon acceptance.
 * 
 * Usage context:
 * This functionality helps restaurant staff efficiently respond to online reservation requests 
 * and simultaneously register accepted bookings without needing separate manual data entry.
 */
@Controller
@RequestMapping("/message_generator")
public class MessageGeneratorController {
	
	@Autowired
	ReservationService reservationService;
	@Autowired
	ReservationMapper reservationMapper;

	/**
	 * GET endpoint to open the message generator page.
	 * 
	 * @return the name of the Thymeleaf template for message generation.
	 */
	@GetMapping
	public String calendarPage() {
		return "message_generator";
	}
	
	/**
	 * POST endpoint to generate an acceptance message based on the incoming reservation details.
	 * 
	 * Steps:
	 * - Parses the original email text into a ReservationDto. 
	 * - Builds the confirmation message text (optionally adding opening hours and HACCP notice).
	 * - Saves the reservation into the database with a log entry (only on acceptance).
	 * 
	 * @param messageRequest contains the pasted email text and checkbox flags.
	 * @return the generated message text to be copied and sent to the customer.
	 */
	@PostMapping("/accept")
	public ResponseEntity<String> generateAcceptMessage(@RequestBody MessageRequest messageRequest) {
		try {
			// Parse the incoming text into ReservationDto.
			ReservationDto reservationDto = splitText(messageRequest.getOriginalText());
			
			// Determine appropriate Hungarian suffix (-én / -án)
			String ending = getDateSuffix(reservationDto.getDate());

			// Format date into yyyy.MM.dd
			String dateWithDots = String.valueOf(reservationDto.getDate()).replaceAll("-", ".");

			// Build optional texts based on selected checkboxes
			StringBuilder textOpeningHours = new StringBuilder("");
			StringBuilder textHACCP = new StringBuilder("");

			if (messageRequest.isOpeningHours()) {
				textOpeningHours.append("\n\nNyitvatartásunk:\nHétfő - Szombat 11:30-21:00\nVasárnap 11:30-16:30");
			}
			if (messageRequest.isHaccpInfo()) {
				textHACCP.append(
						"\nA torta tárolásához, felszolgálásához HACCP tanusítványra van szükségünk, melyet a cukrászda állít ki.");
			}
			
			// Build the final message
			String generatedText =
					"Tisztelt " + reservationDto.getName() + "!\n" 
					+ "Várjuk Önöket " + dateWithDots + ending + " "
					+ String.valueOf(reservationDto.getTime()) + "-kor " 
					+ String.valueOf(reservationDto.getNumberOfPeople()) + " fővel." 
					+ textHACCP
					+ "\n\nÜdvözlettel:\naz étterem csapata\nTelefonszám: +123456789" 
					+ textOpeningHours;

			// Add modifying log for reservation creation
	    	ModifyingLogDto modifyingLogDto = new ModifyingLogDto("E-mail", "létrehozás");
	    	reservationDto.addModifyingLog(modifyingLogDto);
	    	
			// Save the reservation
	    	Reservation reservation = reservationMapper.dtoToReservation(reservationDto);
	    	reservation.getModifyingLogs().forEach(log -> log.setReservation(reservation));
	    	reservationService.save(reservation);

			return ResponseEntity.ok(generatedText);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Hibás szöveg. Az üzenet teljes szövegét másold át!");
		}
	}

	/**
	 * POST endpoint to generate a rejection message based on the incoming reservation details.
	 * 
	 * No database save occurs in case of rejection.
	 * 
	 * @param messageRequest contains the pasted email text.
	 * @return the generated rejection message.
	 */
	@PostMapping("/reject")
	public ResponseEntity<String> generateRejectMessage(@RequestBody MessageRequest messageRequest) {
		try {
			// Parse the incoming text into ReservationDto.
			ReservationDto reservationDto = splitText(messageRequest.getOriginalText());

			// Build the rejection message
			String generatedText = 
					"Tisztelt " + reservationDto.getName() + "!\n" 
					+ "Sajnos a kért időpontra (" + String.valueOf(reservationDto.getDate())
					+ " " + String.valueOf(reservationDto.getTime()) 
					+ ") nem tudunk asztalt biztosítani " 
					+ String.valueOf(reservationDto.getNumberOfPeople()) 
					+ " főnek.\nMásik időpont megbeszéléséért, kérjük hívjon minket telefonon!"
					+ "\n\nÜdvözlettel:\naz étterem csapata\nTelefonszám: +123456789";

			return ResponseEntity.ok(generatedText);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Hibás szöveg. Az üzenet teljes szövegét másold át!");
		}
	}
	
	/**
	 * Helper method to split the pasted email text into structured data fields.
	 * 
	 * Expected email structure (lines in order): Name, Phone number, Email, Date, Time, Headcount, Notes.
	 * 
	 * @param originalText the entire pasted email text.
	 * @return populated ReservationDto object.
	 */
	private ReservationDto splitText(String originalText) {
		// Split the email into lines
		String[] partsOfText = originalText.split("\n");
		
		// Extract fields line-by-line
		String[] nameLine = partsOfText[0].split(": ");
		String[] phoneNumberLine = partsOfText[1].split(": ");
		String[] emailLine = partsOfText[2].split(": ");
		String[] dateLine = partsOfText[3].split(": ");
		String[] timeLine = partsOfText[4].split(": ");
		String[] headcountLine = partsOfText[5].split(": ");
		String[] notesLine = partsOfText[6].split(": ");
		
		// Build notes with email included
		StringBuilder notes = new StringBuilder();
		notes.append("(e-mail: " + emailLine[1] + ") ");
		if (notesLine.length > 1)
			notes.append(notesLine[1]);

		return new ReservationDto(
				LocalDate.parse(dateLine[1]), 
				LocalTime.parse(timeLine[1]), 
				nameLine[1].trim(),  
				Long.valueOf(phoneNumberLine[1]), 
				Integer.valueOf(headcountLine[1]), 
				notes.toString());
	}

	/**
	 * Helper method to select the appropriate Hungarian suffix ("-én" or "-án") based on the day.
	 * 
	 * Example: 2 → 2-án, 4 → 4-én
	 * 
	 * @param date the reservation date.
	 * @return "-én" or "-án" suffix string.
	 */
	private String getDateSuffix(LocalDate date) {
		String[] dateArray = String.valueOf(date).split("-");
		return switch (dateArray[2]) {
		case "02", "03", "06", "08", "13", "16", "18", "20", "23", "26", "28", "30" -> "-án";
		default -> "-én";
		};
	}
	
}