package hu.waiter.blsz.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.waiter.blsz.dto.ReservationDto;
import hu.waiter.blsz.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
	
	public ReservationDto reservationToDto(Reservation reservation);
	
	public List<ReservationDto> reservationsToDtos(List<Reservation> reservations);

	public Reservation dtoToReservation(ReservationDto reservationDto);
	
	public List<Reservation> dtosToReservations(List<ReservationDto> reservationDtos);

}

