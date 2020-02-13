package com.example.egov.voting.prototype.demo.egov.voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/parties")
public class PartyController {

    @Autowired
    private PartyService partyService;


    @RequestMapping("/list")
    public String getAllData(Party party, Model model) {

        String title = "Party";
        model.addAttribute("title", title);

        List<Party> parties = partyService.getAllParties();
        model.addAttribute("parties", parties);

        parties.add(new Party("Примерна партия 1","1"));
        parties.add(new Party("Примерна партия 2","2"));
        parties.add(new Party("Примерна партия 3","3"));

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
        return "parties-list";
    }

}