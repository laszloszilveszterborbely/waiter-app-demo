package hu.waiter.blsz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.waiter.blsz.model.ModifyingLog;

public interface ModifyingLogRepository extends JpaRepository<ModifyingLog, Long>{

	
	public List<ModifyingLog> findByReservationId(Long id);
	
	public List<ModifyingLog> findByPreOrderId(Long id);
	
}
