package hu.waiter.blsz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import hu.waiter.blsz.dto.ModifyingLogDto;
import hu.waiter.blsz.dto.PreOrderDto;
import hu.waiter.blsz.mapper.ModifyingLogMapper;
import hu.waiter.blsz.mapper.PreOrderMapper;
import hu.waiter.blsz.model.ModifyingLog;
import hu.waiter.blsz.model.PreOrder;
import hu.waiter.blsz.service.PreOrderService;


/**
 * This controller manages pre-orders.
 * 
 * Features:
 * - Creating, editing, and cancelling pre-orders.
 * - Displaying and printing pre-order details.
 * - Recording modification logs for changes.
 */
@Controller
@RequestMapping("/preorder")
public class PreOrderController {

	@Autowired
	private PreOrderService preOrderService;
	@Autowired
	private PreOrderMapper preOrderMapper;
	@Autowired
	private ModifyingLogMapper modifyingLogMapper;

	
	/**
	 * GET request to open the pre-order creation page.
	 * 
	 * @param model the Thymeleaf model to populate with an empty PreOrderDto.
	 * @return the pre-order creation form view.
	 */
    @GetMapping("/create")
    public String preorderForm(Map<String, Object> model) {
        model.put("preOrder", new PreOrderDto());
        return "preorder_create";
    }
    
	/**
	 * POST request to save a new pre-order into the database.
	 * 
	 * Adds a log entry for creation.
	 * 
	 * @param preOrderDto the filled pre-order data from the form.
	 * @param monogram the monogram of the staff member who created the pre-order.
	 * @return view that closes the form page.
	 */
    @PostMapping("/create")
    public String savePreOrder(@ModelAttribute PreOrderDto preOrderDto, @RequestParam String monogram) {
    	ModifyingLog modifyingLog = new ModifyingLog(monogram, "létrehozás");
    	
    	PreOrder preOrder = preOrderMapper.dtoToPreOrder(preOrderDto);
    	preOrder.addModifyingLog(modifyingLog);
    	PreOrder savedPreOrder = preOrderService.save(preOrder);
    	if (savedPreOrder == null)
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    	
        return "close_page";
    }
    
    
	/**
	 * GET request to show the details of a specific pre-order.
	 * 
	 * @param id the pre-order ID.
	 * @param model the Thymeleaf model to populate.
	 * @return the pre-order details view.
	 */
    @GetMapping("/{id}")
    public String showPreOrderDetails(@PathVariable Long id, Map<String, Object> model) {
        PreOrder preOrder = preOrderService.findById(id);
        if (preOrder == null)
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.put("preOrder", preOrder);
        return "preorder_details";
    }
    
    
	/**
	 * GET request to open the edit page for a specific pre-order.
	 * 
	 * @param id the pre-order ID.
	 * @param model the Thymeleaf model to populate with the pre-order data.
	 * @return the pre-order editing form view.
	 */
    @GetMapping("/{id}/edit")
    public String showPreOrderEditor(@PathVariable Long id, Map<String, Object> model) {
    	PreOrder preOrder = preOrderService.findById(id);
        if (preOrder == null)
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	model.put("preOrder", preOrder);
    	return "preorder_edit";
    }
    
    
	/**
	 * GET request to generate a printable version of the pre-order details.
	 * 
	 * @param id the pre-order ID.
	 * @return a text representation of the pre-order (formatted for printing).
	 */
    @GetMapping("/{id}/print")
    @ResponseBody
    public String printPreOrder(@PathVariable Long id) {
    	PreOrder preOrder = preOrderService.findById(id);
        if (preOrder == null)
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return preOrder.toKitchen();
    }
    
    
	/**
	 * POST request to update an existing pre-order.
	 * 
	 * Adds a log entry for modification.
	 * 
	 * @param preOrderDto the updated pre-order data.
	 * @param monogram the monogram of the staff member who modified the pre-order.
	 * @return redirects to the home page after saving.
	 */
    @PostMapping("/update")
    public String updatePreOrder(@ModelAttribute PreOrderDto preOrderDto, @RequestParam String monogram) {
    	PreOrder preOrder = preOrderMapper.dtoToPreOrder(preOrderDto);
    	PreOrder originalPreOrder = preOrderService.findById(preOrder.getId());
    	if (originalPreOrder == null)
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	
    	originalPreOrder.setDate(preOrder.getDate());
    	originalPreOrder.setTime(preOrder.getTime());
    	originalPreOrder.setCustomerName(preOrder.getCustomerName());
    	originalPreOrder.setPhoneNumber(preOrder.getPhoneNumber());
    	originalPreOrder.setAddress(preOrder.getAddress());
    	originalPreOrder.setNotes(preOrder.getNotes());
    	originalPreOrder.setCourses(preOrder.getCourses());
    	originalPreOrder.setPaymentMethod(preOrder.getPaymentMethod());

    	ModifyingLog modifyingLog = new ModifyingLog(monogram, "módosítás");
    	originalPreOrder.addModifyingLog(modifyingLog);
    	
    	preOrderService.update(originalPreOrder);
    	
    	return "redirect:/";
    }
    
    
	/**
	 * GET request to open the cancel pre-order page.
	 * 
	 * @param id the pre-order ID.
	 * @param model the Thymeleaf model to populate.
	 * @return the pre-order cancel form view.
	 */
    @GetMapping("/{id}/cancel")
    public String showCancelPreOrder(@PathVariable Long id, Map<String, Object> model) {
    	PreOrder preOrder = preOrderService.findById(id);
        if (preOrder == null)
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	model.put("preOrder", preOrder);
    	model.put("modifyingLog", new ModifyingLogDto());
    	return "preorder_cancel";
    }
    
	/**
	 * POST request to cancel a pre-order by marking it inactive.
	 * 
	 * Adds a log entry for cancellation.
	 * 
	 * @param id the pre-order ID.
	 * @param modifyingLogDto the cancellation reason and staff member monogram.
	 * @return redirects to the home page after canceling.
	 */
    @PostMapping("/{id}/cancel")
    public String cancelPreOrder(@PathVariable Long id, @ModelAttribute ModifyingLogDto modifyingLogDto) {
    	modifyingLogDto.setId(null);
    	PreOrder preOrder = preOrderService.findById(id);
    	if (preOrder == null)
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    	
    	ModifyingLog modifyingLog = modifyingLogMapper.dtoToModifyingLog(modifyingLogDto);
    	preOrder.addModifyingLog(modifyingLog);
    		
    	preOrder.setIsActive(false);
    	
    	preOrderService.update(preOrder);
    	
    	return "redirect:/";
    }
}