package hu.waiter.blsz.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.waiter.blsz.model.PreOrder;
import hu.waiter.blsz.repository.PreOrderRepository;

@Service
public class PreOrderService {
	
	@Autowired
	private PreOrderRepository preOrderRepository;
	
	public PreOrder findById(long id){
		return preOrderRepository.findById(id).orElse(null);
	}
	
	public List<PreOrder> findByDate(LocalDate date){
		return preOrderRepository.findByDate(date);
	}
	
	@Transactional
	public PreOrder save(PreOrder preOrder) {
		return preOrderRepository.save(preOrder);
	}
	
	public PreOrder findById(Long id){
		return preOrderRepository.findById(id).orElse(null);
	}
	
	public List<PreOrder> findPreOrderByExample(LocalDate start, LocalDate end, String name, Long phoneNumber){
		Specification<PreOrder> spec = Specification.where(null);
		
		if (start != null && end != null)
			spec = spec.and(PreOrderSpecifications.isBetweenDates(start, end));
		if (StringUtils.hasText(name))
			spec = spec.and(PreOrderSpecifications.hasName(name));
		if (phoneNumber != null)
			spec = spec.and(PreOrderSpecifications.hasPhoneNumber(phoneNumber));
		
		return preOrderRepository.findAll(spec, Sort.by("date"));
	}
	
	@Transactional
	public PreOrder create(PreOrder preOrder) {
		if (findById(preOrder.getId()) != null)
			return null;
		return save(preOrder);
	}
	
	@Transactional
	public PreOrder update(PreOrder preOrder) {
		if (findById(preOrder.getId()) == null)
			return null;
		return save(preOrder);
	}
}