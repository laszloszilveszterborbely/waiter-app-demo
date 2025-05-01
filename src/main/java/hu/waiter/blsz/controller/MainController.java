package hu.waiter.blsz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller handles the index page.
 * 
 * Features:
 * - Redirects users to the index page.
 */
@Controller
public class MainController {
    
	@GetMapping("/")
    public String index() {
        return "index";
    }
}