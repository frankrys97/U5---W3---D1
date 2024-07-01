package francescocristiano.U5_W3_D1.controllers;

import francescocristiano.U5_W3_D1.entities.Employee;
import francescocristiano.U5_W3_D1.exceptions.BadRequestException;
import francescocristiano.U5_W3_D1.payloads.NewEmployeeDTO;
import francescocristiano.U5_W3_D1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<Employee> getEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return employeeService.findAllEmployees(page, size, sortBy);
    }


    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable UUID id) {
        return employeeService.findEmployeeById(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable UUID id) {
        employeeService.deleteEmployeeById(id);
    }


    @PutMapping("/{id}")
    public Employee updateEmployeeById(@PathVariable UUID id, @RequestBody @Validated NewEmployeeDTO employeePayload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return employeeService.findEmployeeByIdAndUpdate(id, employeePayload);
    }

    @PostMapping("/{id}/avatar")
    public Employee uploadAvatar(@PathVariable UUID id, @RequestParam("avatar") MultipartFile file) throws IOException {
        return employeeService.uploadAvatar(id, file);
    }

}


