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

@Controller
@RequestMapping("/message_generator")
public class MessageGeneratorController {
	
	@Autowired
	ReservationService reservationService;
	@Autowired
	ReservationMapper reservationMapper;

	@GetMapping
	public String calendarPage() {
		return "message_generator";
	}
	
	@PostMapping("/accept")
	public ResponseEntity<String> generateAcceptMessage(@RequestBody MessageRequest messageRequest) {
		try {
			ReservationDto reservationDto = splitText(messageRequest.getOriginalText());
			String ending = inflexion(reservationDto.getDate());
			String dateWithDots = String.valueOf(reservationDto.getDate()).replaceAll("-", ".");

			StringBuilder textOpeningHours = new StringBuilder("");
			StringBuilder textHACCP = new StringBuilder("");

			if (messageRequest.isOpeningHours()) {
				textOpeningHours.append("\n\nNyitvatartásunk:\nHétfő - Szombat 11:30-21:00\nVasárnap 11:30-16:30");
			}
			if (messageRequest.isHaccpInfo()) {
				textHACCP.append(
						"\nA torta tárolásához, felszolgálásához HACCP tanusítványra van szükségünk, melyet a cukrászda állít ki.");
			}
			
			String generatedText = "Tisztelt " + reservationDto.getName() + "!\n" + "Várjuk Önöket " + dateWithDots + ending + " "
					+ String.valueOf(reservationDto.getTime()) + "-kor " + String.valueOf(reservationDto.getNumberOfPeople()) + " fővel." + textHACCP
					+ "\n\nÜdvözlettel:\naz étterem csapata\nTelefonszám: +123456789" + textOpeningHours;
			
	    	ModifyingLogDto modifyingLogDto = new ModifyingLogDto("E-mail", "létrehozás");
	    	reservationDto.addModifyingLog(modifyingLogDto);
	    	    	
	    	Reservation reservation = reservationMapper.dtoToReservation(reservationDto);
	    	reservation.getModifyingLogs().forEach(log -> log.setReservation(reservation));
	    	reservationService.save(reservation);

			return ResponseEntity.ok(generatedText);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Hibás szöveg. Az üzenet teljes szövegét másold át!");
		}
	}


	@PostMapping("/reject")
	public ResponseEntity<String> generateRejectMessage(@RequestBody MessageRequest messageRequest) {
		try {
			ReservationDto reservationDto = splitText(messageRequest.getOriginalText());

			String generatedText = "Tisztelt " + reservationDto.getName() + "!\n" + "Sajnos a kért időpontra (" + String.valueOf(reservationDto.getDate())
					+ " " + String.valueOf(reservationDto.getTime()) + ") nem tudunk asztalt biztosítani " + String.valueOf(reservationDto.getNumberOfPeople())
					+ " főnek.\nMásik időpont megbeszéléséért, kérjük hívjon minket telefonon!"
					+ "\n\nÜdvözlettel:\naz étterem csapata\nTelefonszám: +123456789";

			return ResponseEntity.ok(generatedText);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Hibás szöveg. Az üzenet teljes szövegét másold át!");
		}
	}
	
	private ReservationDto splitText(String originalText) {
		String[] partsOfText = originalText.split("\n");
		String[] nameLine = partsOfText[0].split(": ");
		String[] phoneNumberLine = partsOfText[1].split(": ");
		String[] emailLine = partsOfText[2].split(": ");
		String[] dateLine = partsOfText[3].split(": ");
		String[] timeLine = partsOfText[4].split(": ");
		String[] headcountLine = partsOfText[5].split(": ");
		String[] notesLine = partsOfText[6].split(": ");
		StringBuilder notes = new StringBuilder();
		notes.append("(e-mail: " + emailLine[1] + ") ");
		if (notesLine.length > 1)
			notes.append(notesLine[1]);

		return new ReservationDto(LocalDate.parse(dateLine[1]), LocalTime.parse(timeLine[1]), nameLine[1].trim(),  Long.valueOf(phoneNumberLine[1]), Integer.valueOf(headcountLine[1]), notes.toString()) ;
	}


	private String inflexion(LocalDate date) {
		String[] dateArray = String.valueOf(date).split("-");
		return switch (dateArray[2]) {
		case "02", "03", "06", "08", "13", "16", "18", "20", "23", "26", "28", "30" -> "-án";
		default -> "-én";
		};
	}
	
}