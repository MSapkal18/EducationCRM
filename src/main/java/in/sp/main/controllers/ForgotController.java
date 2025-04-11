package in.sp.main.controllers;

import in.sp.main.entities.User;
import in.sp.main.repositories.UserRepository;
import in.sp.main.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.Session;
import java.util.Random;

@Controller
public class ForgotController
{
    Random random = new Random(1000);
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/forgot")
    public String openEmailForm()
    {
        return "forgotPass/sendOTPform";
    }

    @PostMapping("/forOTP")
    public String sendOTP(@RequestParam("email") String getEmail, Model model,HttpSession session)
    {
        // to get email from frontend
        session.setAttribute("email", getEmail);
        User user=this.userRepository.findByEmail(getEmail);

        //checking user is exit or not
        if (user==null)
        {
            model.addAttribute("errorMsg", "User not exist with this email");
            System.out.println("User not found.................");
            return "forgotPass/sendOTPform";
        }
        else
        {
            // for generating random OTP
            int otp= random.nextInt(999999);


            //Code for send OTP to email
            String subject = "OTP from vega education to verify your ";
            String message = ""
                    + "<div style='border:1px solid #e2e2e2; padding:20px'>"
                    + "<h1>"
                    + "OTP is "
                    + "<b>" + otp
                    + "</b>"
                    + "</h1>"
                    + "</div>";
            String to = getEmail;
            boolean flag = this.emailService.sendEmail(subject, message, to);
            if (flag)
            {
                session.setAttribute("sentOtp", otp);
                model.addAttribute("successMsg", "We have sent OTP to your email..");
                System.out.println("OTP="+otp);
                return "forgotPass/verify_otp";
            }
            else
            {
                model.addAttribute("errorMsg", "Incorrect Email id or Password");
                return "forgotPass/sendOTPform";
            }
        }
    }

    //verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp,Model model,HttpSession session)
    {
        int sentOtp=(int)session.getAttribute("sentOtp");

        if (sentOtp==otp)
        {
            //password change form
            return "forgotPass/change_password_form";
        }
        else
        {
            //back to verify form if OTP is wrong
            model.addAttribute("errorMsg","You have entered wrong OTP!");
            return "forgotPass/verify_otp";
        }
    }

    // change password
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("newPassword") String newPassword,@RequestParam("conformPassword") String conformPassword, HttpSession session,Model model)
    {

        boolean check=newPassword.equals(conformPassword);
        if (check)
        {
            String email=(String)session.getAttribute("email");
            User user=this.userRepository.findByEmail(email);
            user.setPassword(newPassword);
            this.userRepository.save(user);
            model.addAttribute("successMsg", "Password update successfully ..!");
            return "login";
        }
        else
        {
            model.addAttribute("errorMsg", "Password do not match !");
            return "forgotPass/change_password_form";
        }
    }
}
