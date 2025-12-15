package com.workshop.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.Repo.OnewayTripRepo;
import com.workshop.Repo.RoundTripRepo;
import com.workshop.Service.ExceFileStorageService;
import com.workshop.Service.QuartzSchedulingService;

@RestController
@RequestMapping("/upload/roundTrip/excel")
public class RoundTripUploadController {

    @Autowired
    private QuartzSchedulingService schedulingService;

    @Autowired
    private ExceFileStorageService fileStorageService;

    @Autowired
        private RoundTripRepo pricingRepository;


    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) throws Exception {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Delete old jobs + file before scheduling new one
        schedulingService.deleteRoundTripExistingJobsAndFile();

        String filePath = fileStorageService.saveFile(file);

        // Schedule save job with start and end date so Excel rows get correct validity range
        schedulingService.scheduleRoundTripSaveJob(start, end, filePath);
        schedulingService.scheduleRoundTripDeleteJob(end, filePath); // Pass filePath for cleanup

        return ResponseEntity.ok("Excel uploaded and scheduled successfully. Old job and file replaced.");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Map<String, String>>> getJobs() throws SchedulerException {
        return ResponseEntity.ok(schedulingService.getRoundScheduledJobs());
    }

    @DeleteMapping("/delete")
public ResponseEntity<String> deleteJobAndFile() throws SchedulerException, IOException {
    boolean deleted = schedulingService.deleteRoundTripExistingJobsAndFile();


    // Manually clear DB now
    pricingRepository.deleteAll();

    return deleted ?
        ResponseEntity.ok("Deleted existing jobs, Excel file and cleared data.") :
        ResponseEntity.ok("No job found to delete.");
}
}
