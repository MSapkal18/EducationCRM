package in.sp.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Employee;
import in.sp.main.entities.EmployeeOrders;
import in.sp.main.entities.Inquiry;
import in.sp.main.entities.Orders;
import in.sp.main.repositories.EmployeeOrdersRepository;
import in.sp.main.repositories.EmployeeRepository;
import in.sp.main.services.CourseService;
import in.sp.main.services.EmployeeService;
import in.sp.main.services.OrderService;

@Controller
@SessionAttributes("sessionEmp")
public class EmployeeController
{
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeOrdersRepository employeeOrdersRepository;
	
	@GetMapping("/employeeLogin")
	public String openEmployeeLoginPage()
	{
		return "employee-login";
	}
	
	@PostMapping("/empLoginForm")
	public String employeeLoginForm(@RequestParam("email") String email, @RequestParam("password") String pass, Model model)
	{
		boolean isAuthenticated = employeeService.loginEmpService(email, pass);
		if(isAuthenticated)
		{
			Employee authenticatedEmp = employeeRepository.findByEmail(email);
			model.addAttribute("sessionEmp", authenticatedEmp);
			
			return "employee-profile";
		}
		else
		{
			model.addAttribute("errorMsg", "Incorrect Email id or Password");
			return "employee-login";
		}
	}
	
	@GetMapping("/employeeProfile")
	public String openEmployeeProfilePage()
	{
		return "employee-profile";
	}
	
	@GetMapping("/employeeManagement")
	public String openEmployeeManagementPage(Model model,
					@RequestParam(name="page", defaultValue = "0") int page,
					@RequestParam(name="size", defaultValue = "5") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Employee> employeePage = employeeService.getAllEmployeeDetailsByPagination(pageable);
		
		model.addAttribute("employeePage", employeePage);
		
		return "employee-management";
	}
	
	//---------------add employee starts-----------------------------
	@GetMapping("/addEmployee")
	public String openAddCoursePage(Model model)
	{
		model.addAttribute("employee", new Employee());
		return "add-employee";
	}
	
	@PostMapping("/addEmployeeForm")
	public String addEmployeeForm(@ModelAttribute("employee") Employee employee, Model model)
	{
		try
		{
			employeeService.addEmployee(employee);
			model.addAttribute("successMsg", "Employee added successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("errorMsg", "Employee not added due to some error");
		}
		return "add-employee";
	}
	//---------------add employee ends-----------------------------
	
	
	//---------------edit employee starts-----------------------------
	@GetMapping("/editEmployee")
	public String openEditEmployeePage(@RequestParam("employeeEmail") String employeeEmail, Model model)
	{
		Employee employee = employeeService.getEmployeeDetails(employeeEmail);
		
		model.addAttribute("employee", employee);
		model.addAttribute("newEmployeeObj", new Employee());
		
		return "edit-employee";
	}
	
	@PostMapping("/updateEmployeeDetailsForm")
	public String updateEmployeeDetailsForm(@ModelAttribute("newEmployeeObj") Employee newEmployeeObj, RedirectAttributes redirectAttributes)
	{
		try
		{
			Employee oldEmployeeObj = employeeService.getEmployeeDetails(newEmployeeObj.getEmail());
			newEmployeeObj.setId(oldEmployeeObj.getId());
			
			employeeService.updateEmployeeDetails(newEmployeeObj);
			
			redirectAttributes.addFlashAttribute("successMsg", "Employee details updated successfully");
		}
		catch(Exception e)
		{
			redirectAttributes.addFlashAttribute("errorMsg", "Employee details not updated due to some error");
			e.printStackTrace();
		}
		
		return "redirect:/employeeManagement";
	}
	//---------------edit employee ends-----------------------------
	
	@GetMapping("/deleteEmployeeDetails")
	public String deleteEmployeeDetails(@RequestParam("employeeEmail") String employeeEmail, RedirectAttributes redirectAttributes)
	{
		try
		{
			employeeService.deleteEmployeeDetails(employeeEmail);
			redirectAttributes.addFlashAttribute("successMsg", "Employee deleted successfully");
		}
		catch(Exception e)
		{
			redirectAttributes.addFlashAttribute("errorMsg", "Employee not deleted due to some error");
			e.printStackTrace();
		}
		return "redirect:/employeeManagement";
	}
	
	//-------------open sell course page------------------------
	@GetMapping("/sellCourse")
	public String openSellCoursePage(Model model)
	{
		List<String> courseNameList = courseService.getAllCourseNames();
		model.addAttribute("courseNameList", courseNameList);
		
		String uuidOrderId = UUID.randomUUID().toString();
		model.addAttribute("uuidOrderId", uuidOrderId);
		
		model.addAttribute("orders", new Orders());
		
		return "sell-course";
	}
	@PostMapping("/sellCourseForm")
	public String sellCourseForm(@ModelAttribute("orders") Orders orders, @SessionAttribute("sessionEmp") Employee sessionEmp, RedirectAttributes redirectAttributes)
	{
		LocalDate ld = LocalDate.now();
		String pdate = ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		LocalTime lt = LocalTime.now();
		String ptime = lt.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
		
		String purchased_date_time = pdate+", "+ptime;
		
		orders.setDateOfPurchase(purchased_date_time);
		
		try
		{
			orderService.storeUserOrders(orders);
			
			EmployeeOrders employeeOrders = new EmployeeOrders();
			employeeOrders.setOrderId(orders.getOrderId());
			employeeOrders.setEmployeeEmail(sessionEmp.getEmail());
			
			employeeOrdersRepository.save(employeeOrders);
			
			redirectAttributes.addFlashAttribute("successMsg", "Course provided successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Course not provided due to some error");
		}
		return "redirect:/sellCourse";
	}
	
	
	//-------------inquiry management------------------------
	@GetMapping("/inquiryManagement")
	public String openIquiryManagementPage(Model model)
	{
		model.addAttribute("inquiry", new Inquiry());
		return "inquiry-management";
	}
	
	//-------------employee logout------------------------
	@GetMapping("/employeeLogout")
	public String employeeLogout(SessionStatus sessionStatus)
	{
		sessionStatus.setComplete();
		return "employee-login";
	}
}