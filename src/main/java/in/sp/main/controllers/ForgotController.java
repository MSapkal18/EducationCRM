package in.sp.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgotController
{
    Random random = new Random(1000);
    @GetMapping("/forgot")
    public String openEmailForm()
    {
        return "forgotPass/forgotPass";
    }
    @PostMapping("/forOTP")
    public String sendOTP(@RequestParam("email") String email)
    {
        System.out.println("Email="+email);
        // for generating random OTP
        int otp= random.nextInt(999999);
        System.out.println("OTP="+otp);

        //Code for send OTP to email
        return "forgotPass/verify_otp";
    }
}
