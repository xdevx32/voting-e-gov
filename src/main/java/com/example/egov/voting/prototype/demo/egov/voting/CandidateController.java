package com.example.egov.voting.prototype.demo.egov.voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.soap.Detail;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;


    @RequestMapping("/list")
    public String getAllData(Candidate candidate, Model model) {

        String title = "Candidates";
        model.addAttribute("title", title);

        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);

        candidates.add(new Candidate("Примерен кандидат 1","Примерна партия на кандидата","1"));
        candidates.add(new Candidate("Примерен кандидат 2","Примерна партия на кандидата","2"));
        candidates.add(new Candidate("Примерен кандидат 3","Примерна партия на кандидата","3"));

        /* Use case
        List<Employee> employees = employeeService.getAllEmployees();
        List<Detail> details = detailService.getAllDetails();
        List<Material> materials = materialService.getAllMaterials();

        // Разходи:
        double expensesBasedOnSalaries = Employee.calculateExpensesBasedOnSalaries(employees);
        model.addAttribute("expensesBasedOnSalaries", expensesBasedOnSalaries);
        double expensesBasedOnMaterialsBought = Material.calculateExpensesBasedOnMaterialsBought(materials);
        model.addAttribute("expensesBasedOnMaterialsBought", expensesBasedOnMaterialsBought);
        // Общо разходи:
        double expensesTotal = expensesBasedOnMaterialsBought + expensesBasedOnSalaries;
        model.addAttribute("expensesTotal", expensesTotal);

        // Приходи:
        double incomeBasedOnDetailsProduced = Detail.calculateIncomeBasedOnDetailsProduced(details);
        model.addAttribute("incomeBasedOnDetailsProduced", incomeBasedOnDetailsProduced);
        // Общо приходи:
        double incomeTotal = incomeBasedOnDetailsProduced;
        model.addAttribute("incomeTotal", incomeTotal);

        // Печалба:
        double profitBasedOnDetailsProduced = Detail.calculateProfitBasedOnDetailsProduced(details);
        model.addAttribute("profitBasedOnDetailsProduced", profitBasedOnDetailsProduced);
        // Общо печалба:
        double profitTotal = profitBasedOnDetailsProduced - (profitBasedOnDetailsProduced * 10.0 / 100); // -10%
        model.addAttribute("profitTotal", profitTotal);

        // Текуща дата
        LocalDate currentDate = LocalDate.now();
        model.addAttribute("currentDate", currentDate);

        // Заглавие на страницата за справки
        String title = "Справка";
        model.addAttribute("title", title);

        */
        return "candidates-list";
    }

}