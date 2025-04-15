package in.sp.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoController
{
    @GetMapping
    public String videoStart()
    {
        return "";
    }
}
