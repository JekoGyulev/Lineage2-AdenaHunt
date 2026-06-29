package main.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FarmZoneController {


    @GetMapping("/farm-zone")
    public ModelAndView getFarmZonePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("farm-zone");

        return modelAndView;
    }


}
