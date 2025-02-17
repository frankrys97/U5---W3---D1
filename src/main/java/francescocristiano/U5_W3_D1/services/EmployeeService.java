package francescocristiano.U5_W3_D1.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import francescocristiano.U5_W3_D1.config.MailgunSender;
import francescocristiano.U5_W3_D1.entities.Employee;
import francescocristiano.U5_W3_D1.exceptions.BadRequestException;
import francescocristiano.U5_W3_D1.exceptions.NotFoundException;
import francescocristiano.U5_W3_D1.payloads.NewEmployeeDTO;
import francescocristiano.U5_W3_D1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private Cloudinary cloudinaryService;

    @Autowired
    private MailgunSender mailgunSender;

    public Employee saveEmployee(NewEmployeeDTO employeePayload) {
        employeeRepository.findByUsername(employeePayload.username()).ifPresent(employee -> {
            throw new BadRequestException("Username already exists");
        });
        employeeRepository.findByEmail(employeePayload.email()).ifPresent(employee -> {
            throw new BadRequestException("Email already exists");
        });
        Employee newEmployee = new Employee(employeePayload.username(), employeePayload.name(), employeePayload.surname(), employeePayload.email(), employeePayload.password());
        mailgunSender.sendRegistrationEmail(newEmployee);
        return employeeRepository.save(newEmployee);
    }

    public Employee findEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }


    public Page<Employee> findAllEmployees(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return employeeRepository.findAll(pageable);
    }

    public Employee findEmployeeByIdAndUpdate(UUID id, NewEmployeeDTO updatedEmployeePayload) {
        Employee updatedEmployee = new Employee(updatedEmployeePayload.username(), updatedEmployeePayload.name(), updatedEmployeePayload.surname(), updatedEmployeePayload.email(), updatedEmployeePayload.password());
        Employee foundEmployee = findEmployeeById(id);
        foundEmployee.setUsername(updatedEmployee.getUsername());
        foundEmployee.setName(updatedEmployee.getName());
        foundEmployee.setSurname(updatedEmployee.getSurname());
        foundEmployee.setEmail(updatedEmployee.getEmail());
        foundEmployee.setAvatarUrl(updatedEmployee.getAvatarUrl());
        return employeeRepository.save(foundEmployee);
    }

    public void deleteEmployeeById(UUID id) {

        Employee foundEmployee = findEmployeeById(id);
        if (!foundEmployee.getDevices().isEmpty()) {
            String devicesList = foundEmployee.getDevices().stream().map(device -> device.getDeviceType().name() + " (" + device.getId() + ")").collect(Collectors.joining(", "));
            throw new BadRequestException("Cannot delete employee with assigned devices: " + devicesList + ". Please unassign them first");
        }
        employeeRepository.deleteById(id);
    }


    public Employee uploadAvatar(UUID id, MultipartFile file) throws IOException {
        String cloudinaryUrl = cloudinaryService.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();
        Employee foundEmployee = findEmployeeById(id);
        foundEmployee.setAvatarUrl(cloudinaryUrl);
        return employeeRepository.save(foundEmployee);
    }

    public Employee findEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username with username " + username + " not found"));
    }
}
