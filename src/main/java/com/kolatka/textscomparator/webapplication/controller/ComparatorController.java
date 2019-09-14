package com.kolatka.textscomparator.webapplication.controller;


import com.kolatka.textscomparator.webapplication.service.ComparatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ComparatorController {

    @Autowired
    ComparatorService comparatorService;


    @GetMapping("/results")
    public String showIndex(){
        /*System.out.println("started");
        String t1 = "To jest test tekstu z nawiasami(różnymi) oraz symbolami takimi jak: <jeden> {dwa}; [trzy]; super-ekstra. Lol. 200/5+4=44.";
        String t2 = "To jest test tekstu z nawiasami(różnymi) oraz symbolami takimi jak: <jeden> {dwa}; [trzy]; super-ekstra. Lol. 200/5+4=44";
        SolverService solverService = new SolverService();
        solverService.loadParameters(false, false,false,false,false);
        solverService.loadFirstText(t1);
        solverService.prepareText(0);
        solverService.loadSecondText(t2);
        solverService.prepareText(1);
        solverService.compareTexts();
        solverService.getComparingStatistics();
        System.out.println("completed");
        System.out.println(solverService.getComparingStatistics());

        return solverService.getComparingStatistics();*/
        return "random result";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/firsttext")
    public void sendString(@RequestBody String firstText){

    }

}



