package in.sp.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import in.sp.main.entities.Employee;
import in.sp.main.entities.FollowUps;
import in.sp.main.entities.Inquiry;
import in.sp.main.services.FollowUpsService;
import in.sp.main.services.InquiryService;

@Controller
@SessionAttributes("sessionEmp")
public class InquiryController
{
	@Autowired
	private InquiryService inquiryService;
	
	@Autowired
	private FollowUpsService followUpsService;
	
	@GetMapping("/newInquiry")
	public String openNewInquiryPage(Model model)
	{
		model.addAttribute("inquiry", new Inquiry());
		return "new-inquiry";
	}
	
	@PostMapping("/submitInquiryForm")
	public String submitInquiryForm(@ModelAttribute("inquiry") Inquiry inquiry,
			@SessionAttribute("sessionEmp") Employee sessionEmp, Model model,
			@RequestParam(name="followUpDate", required=false) String followUpDate,
			@RequestParam(name="sourcePage", required = false) String sourcePage)
	{
		inquiry.setEmpEmail(sessionEmp.getEmail());
		
		LocalDate ld = LocalDate.now();
		String date1 = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		LocalTime lt = LocalTime.now();
		String time1 = lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		inquiry.setDateOfInquiry(date1);
		inquiry.setTimeOfInquiry(time1);
		
		try
		{
			inquiryService.addNewInquiry(inquiry);
			
			String status = inquiry.getStatus();
			if(status.equals("Interested - (follow up)") && followUpDate != null)
			{
				FollowUps followUps = new FollowUps();
				
				followUps.setPhoneno(inquiry.getPhoneno());
				followUps.setFollowUpDate(followUpDate);
				followUps.setEmpEmail(sessionEmp.getEmail());
				
				followUpsService.addFollowUps(followUps);
			}
			else
			{
				followUpsService.deleteByPhoneNumber(inquiry.getPhoneno());
			}
			
			model.addAttribute("successMsg", "New inquiry added successsfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			model.addAttribute("errorMsg", "Some error occured, please try again later");
		}
		
		if("followUpsPage".equals(sourcePage))
		{
			model.addAttribute("successMsg", "Inquiry handled successsfully");
			return "follow-ups";
		}
		else
		{
			return "inquiry-management";
		}
	}
}
