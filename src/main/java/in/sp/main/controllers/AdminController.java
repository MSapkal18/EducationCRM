package in.sp.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import in.sp.main.entities.Admin;
import in.sp.main.entities.Course;
import in.sp.main.entities.Feedback;
import in.sp.main.repositories.AdminRepository;
import in.sp.main.services.AdminService;
import in.sp.main.services.CourseService;
import in.sp.main.services.FeedbackService;

@Controller
public class AdminController 
{
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private AdminRepository adminRepository;

	
	@Autowired
	private AdminService adminService;

	
	//------------------Admin login start-----------------------------
		@GetMapping("/adminLogin")
		public String openAdminLoginPage() 
		{
			return "admin-login";
		}

		@PostMapping("/adminLoginForm")
		public String adminLoginForm(@ModelAttribute("Admin") Admin admin, Model model) 
		{
			boolean isAuthenticated = adminService.loginAdminService(admin.getEmail(), admin.getPassword());
			if (isAuthenticated) {
				Admin authenticatedAdmin = adminRepository.findByEmail(admin.getEmail());
				model.addAttribute("sessionAdmin", authenticatedAdmin);
				return "admin-profile";
			} else {
				model.addAttribute("errorMsg", "Incorrect id or password");
				return "admin-login";
			}

		}
	
	
	
	@GetMapping("/courseManagement")
	public String openCourseManagementPage(Model model,
					@RequestParam(name="page", defaultValue = "0") int page,
					@RequestParam(name="size", defaultValue = "4") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Course> coursesPage = courseService.getAllCourseDetailsByPagination(pageable);
		
		model.addAttribute("coursesPage", coursesPage);
		
		return "course-management";
	}
	
	//-------------admin logout------------------------
	@GetMapping("/adminLogout")
	public String adminLogout(SessionStatus sessionStatus)
	{
		sessionStatus.setComplete();
		return "admin-login";
	}
	
	
	//-------------feedback----------------------------
	@GetMapping("/adminFeedback")
	public String openFeedbackPage(Model model,
			@RequestParam(name="page", defaultValue = "0") int page,
			@RequestParam(name="size", defaultValue = "4") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Feedback> feedbackPage = feedbackService.getAllFeedbacksByPagination(pageable);
		
		model.addAttribute("feedbackPage", feedbackPage);
		
		return "view-feedbacks";
	}
	
    @PostMapping("/updateFeedbackStatus")
    public String updateFeedbackStatus(@RequestParam("id") Long id, @RequestParam("status") String status, RedirectAttributes redirectAttributes)
    {
        boolean success = feedbackService.updateFeedbackStatus(id, status);
        if (success) {
            redirectAttributes.addFlashAttribute("successMsg", "Feedback updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Failed to update feedback status.");
        }
        return "redirect:/adminFeedback"; // Redirect to the page where feedbacks are listed
    }
}
