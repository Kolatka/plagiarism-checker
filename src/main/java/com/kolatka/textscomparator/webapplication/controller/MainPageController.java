package com.kolatka.textscomparator.webapplication.controller;


import com.kolatka.textscomparator.webapplication.service.Comparation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainPageController {

	@GetMapping("/")
	public String showIndex(Model model){
		model.addAttribute("comparation", new Comparation());
		return "index";
	}

	@PostMapping("/")
	public String textsSumbit(@ModelAttribute Comparation comparation){
		return "index";
	}

}
