package cm.belrose.config;

import cm.belrose.dto.EmployeeDto;
import cm.belrose.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<EmployeeDto, Employee> {

    @Override
    public Employee process(EmployeeDto employeeDto) throws Exception {
        if(employeeDto.jobTitle().equals("Vice President")) return null;
        return mapEmployee(employeeDto);
    }

    //TODO you can replace this peace of code using mapstruct
    private Employee mapEmployee(EmployeeDto employeeDto) {
        return Employee.builder()
                .employeeId(employeeDto.employeeId())
                .age(employeeDto.age())
                .gender(employeeDto.gender())
                .department(employeeDto.department())
                .fullName(employeeDto.fullName())
                .ethnicity(employeeDto.ethnicity())
                .businessUnit(employeeDto.businessUnit())
                .jobTitle(employeeDto.jobTitle())
                .build();
    }
}
