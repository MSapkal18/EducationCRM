package in.sp.main.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.sp.main.services.OrdersChartService;

@Controller
public class OrdersChartController 
{
	@Autowired
	private OrdersChartService ordersChartService;
	
	@GetMapping("/adminProfile")
	public String openAdminProfilePage(Model model)
	{
		//-------graph 3------------------
		List<Object[]> listOfCoursesSoldPerDay = ordersChartService.findCoursesSoldPerDay();
		
		List<String> dates1 = new ArrayList<>();
		List<Long> counts1 = new ArrayList<>();
		
		for(Object[] obj : listOfCoursesSoldPerDay)
		{
			dates1.add((String) obj[0]);
			counts1.add((Long) obj[1]);
		}
		
		model.addAttribute("dates1", dates1);
		model.addAttribute("counts1", counts1);
		
		
		//---------graph 2------------------
		List<Object[]> listOfCoursesTotalSales = ordersChartService.findCoursesTotalSales();
		
		List<String> coursename1 = new ArrayList<>();
		List<Long> coursecount1 = new ArrayList<>();
		
		for(Object[] obj : listOfCoursesTotalSales)
		{
			coursename1.add((String) obj[0]);
			coursecount1.add((Long) obj[1]);
		}
		
		model.addAttribute("coursename1", coursename1);
		model.addAttribute("coursecount1", coursecount1);
		
		
		
		//---------graph 1------------------
		List<Object[]> listOfCoursesAmountSales = ordersChartService.findCoursesAmountTotalSales();
		
		List<String> date11 = new ArrayList<>();
		List<Double> totalAmount11 = new ArrayList<>();
		
		for(Object[] obj : listOfCoursesAmountSales)
		{
			date11.add((String) obj[0]);
			totalAmount11.add((Double) obj[1]);
		}
		
		model.addAttribute("date11", date11);
		model.addAttribute("totalAmount11", totalAmount11);
		
		return "admin-profile";
	}
}
