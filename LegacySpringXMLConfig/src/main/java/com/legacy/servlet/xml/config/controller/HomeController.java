package com.legacy.servlet.xml.config.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping("/")
	public String showHomePage(Model model) {
		model.addAttribute("message", "Loaded successfully via legacy XML configuration!");
		model.addAttribute("title", "Legacy Spring MVC (XML Config)");
		model.addAttribute("status", "DispatcherServlet successfully initialized!");
		model.addAttribute("timestamp", LocalDateTime.now().toString());
		return "home";// Resolves to /WEB-INF/views/home.jsp
	}

	/**
	 * What Spring does internally before forwarding to the view:
	 * request.setAttribute("message", "Hello World");
	 * request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
	 */

	// Dynamic param endpoint: GET
	// http://localhost:8080/LegacySpringXMLConfig/greet?name=Sankalp
	@GetMapping("/greet")
	public String greetUser(@RequestParam(value = "name", defaultValue = "Guest") String name, Model model) {
		model.addAttribute("title", "Greetings Endpoint");
		model.addAttribute("status", "Hello, " + name + "! Controller is working perfectly.");
		model.addAttribute("timestamp", LocalDateTime.now().toString());
		return "home";
	}

	/** Model is the modern standard, while ModelAndView is considered legacy style. */
	// ModelAndView equivalent:
//	@GetMapping("/")
//	public ModelAndView showHomePage() {
//		ModelAndView mav = new ModelAndView("home");
//		mav.addObject("message", "Hello World");
//		return mav;
//	}
}
