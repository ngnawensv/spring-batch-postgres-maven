package cm.belrose.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class RunJobController {

    private  final JobLauncher jobLauncher;
    private  final Job job;

    @PostMapping("/jobs")
    public ResponseEntity<Void> csvToDataBase() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startingAt:" , System.currentTimeMillis())
                .addString("inputFilePath", "src/main/resources/Employee_Sample_Data.csv")
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("RunJobController:csvToDataBase::Exception {}",e.toString());
        }
        return ResponseEntity.ok().build();
    }
}
