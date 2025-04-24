package hu.waiter.blsz.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.waiter.blsz.model.PreOrder;

public interface PreOrderRepository extends JpaRepository<PreOrder, Long>, JpaSpecificationExecutor<PreOrder>{

	List<PreOrder> findByDate(LocalDate date);

}
