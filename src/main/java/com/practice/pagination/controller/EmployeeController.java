package com.practice.pagination.controller;

import com.practice.pagination.model.Employee;
import com.practice.pagination.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // display list of employess
    @GetMapping("/")
    public String viewHomePage(Model model) {
//        model.addAttribute("listEmployees", this.employeeService.getAllEmployees());
        return this.findPaginated(1, "firstName", "asc", model);
    }

    // show employee form
    @GetMapping("/newEmployeeForm")
    public String newEmployeeForm(Model model) {
        // create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "newEmployeeForm";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        this.employeeService.createEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/employeUpdateForm/{id}")
    public String employeeUpdateForm(@PathVariable(value = "id") long id, Model model) throws Exception {
        model.addAttribute("employee", this.employeeService.getEmployeeById(id));
        return "employeeUpdateForm";
    }

    @GetMapping("/employeeDelete/{id}")
    public String employeeDelete(@PathVariable(value = "id") long id) throws Exception {
        this.employeeService.deleteEmployee(id);
        return "redirect:/";
    }

    // if you want to pass the pageSize you can by just adding /{pageSize}
    // we need to create a query string ex: /page/1?sortField=name&sortDir=asc
    @GetMapping("/page/{pageNumber}")
    public String findPaginated(
            @PathVariable(name = "pageNumber") int pageNumber,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model
    ) {
        int pageSize = 5;
        Page<Employee> page = employeeService.findPaginated(pageNumber, pageSize, sortField, sortDir);
        List<Employee> employeeList = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", employeeList);
        return "index";
    }
}
