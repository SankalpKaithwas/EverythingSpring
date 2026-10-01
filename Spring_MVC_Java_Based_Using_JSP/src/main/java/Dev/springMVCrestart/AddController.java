package Dev.springMVCrestart;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Dev.springMVCrestart.service.ServiceClass;

@Controller
public class AddController {

	/** @RequestMapping("add")): This matches all HTTP methods (GET, POST, PUT, DELETE, PATCH) OPTIONS.
	 * If a client sends a POST request or a DELETE request to /add, Spring will still execute this method. 
	 * 
	 * @RequestMapping(value = "add", method = RequestMethod.GET)): This explicitly restricts the 
	 * endpoint to HTTP GET requests only. If someone tries to send a POST request to /add, 
	 * Spring will reject it with an HTTP 405 Method Not Allowed error. 
	 * 
	 * @GetMapping("add") is strongly preferred over both of them.
	 * */
	
	private final ServiceClass service;

    // Recommended: Dependency Injection via constructor instead of 'new Service()'
    @Autowired
    public AddController(ServiceClass service) {
        this.service = service;
    }
	
	// Use Model as it is better than ModelAndView
	@GetMapping("add")
	public String addition(@RequestParam("t1") int t1, @RequestParam("t2") int t2, Model model) {
		System.out.println("Print line");
		/**	ServiceClass service = new ServiceClass(); 
		 * Instead of creating new service object, 
		 * we use @Autowire on ServiceClass to let Spring manage its lifecycle through 
		 * dependency injection.*/
		int k = service.add(t1, t2);
		model.addAttribute("result",k);
		model.addAttribute("status","Hello! AddController is working perfectly.");
		model.addAttribute("timestamp", LocalDateTime.now().toString());
		return "display"; // Resolves to templates/profile.html (or .jsp)
	}
	
	/** Why Model won over ModelAndView:
		1. With Model, the return type is a simple, readable String view name.
		2. Redirects are clean: return "redirect:/login";.
		3. ModelAndView forces you to instantiate a wrapper object manually (new ModelAndView("profile")), 
		which adds boilerplate without adding value. */
	
    
	// Preferred Way with service injection
//	@GetMapping("add")
//	public ModelAndView add2(@RequestParam("t1") int t1, @RequestParam("t2") int t2) {
//		System.out.println("Print line");
//		/**	ServiceClass service = new ServiceClass(); 
//		 * Instead of creating new service object, 
//		 * we use @Autowire on ServiceClass to let Spring manage its lifecycle through 
//		 * dependency injection.*/
//		int k = service.add(t1, t2);
//		ModelAndView view = new ModelAndView();
//		view.addObject("result", k);
//		view.setViewName("display");
//		return view;
//	}
	
	
//	@RequestMapping("add")
//	public ModelAndView add(@RequestParam("t1") String t1, @RequestParam("t2") String t2) {
//		System.out.println("Print line");
//
////		int i = Integer.parseInt(request.getParameter("t1"));
////		int j = Integer.parseInt(request.getParameter("t2"));
//		int i = Integer.parseInt(t1);
//		int j = Integer.parseInt(t2);
//		ServiceClass service = new ServiceClass();
//		int k = service.add(i, j);
//
//		/** ModelAndView is of SpringFramework so we need dependencies */
//		ModelAndView view = new ModelAndView();
//		view.addObject("result", k);
//		view.setViewName("display");
//		return view;
//	}

//	@RequestMapping("add")
//	public ModelAndView add1(@RequestParam("t1") int i, @RequestParam("t2") int j, HttpServletRequest request,
//			HttpServletResponse response) {
//
////		int i = Integer.parseInt(request.getParameter("t1"));
////		int j = Integer.parseInt(request.getParameter("t2"));
//		ServiceClass service = new ServiceClass();
//		int k = service.add(i, j);
//
//		ModelAndView view = new ModelAndView();
//		view.addObject("result", k);
//		view.setViewName("display");
//		return view;
//	}
	

}
/** This complete setup is called "JAVA BASED SPRING MVC" */
/** addition is not our job its service class job */
//Request param is what we are using as annotations for annotation based configuration
// @Controller - Interpreted as a View Name (e.g., "index" renders index.jsp or index.html)