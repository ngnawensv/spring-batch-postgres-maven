package cm.belrose.controller;
import cm.belrose.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class RunJobController {

    private  final EmployeeService employeeService;

    @PostMapping("/jobs")
    public ResponseEntity<Void> csvToDataBase() {
        employeeService.csvToDataBase();
        return ResponseEntity.ok().build();
    }
}
