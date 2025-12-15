package com.workshop.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.workshop.Entity.onewayTrip;
import com.workshop.Entity.roundTrip;
import com.workshop.Entity.ExcelGetData.TransportRateExcelView;
import com.workshop.Repo.OnewayTripRepo;
import com.workshop.Service.ExceFileStorageService;
import com.workshop.Service.QuartzSchedulingService;
import com.workshop.Service.TripService;

@RestController
@RequestMapping("/upload/excel")
public class UploadController {

    @Autowired
    private QuartzSchedulingService schedulingService;

    @Autowired
    private ExceFileStorageService fileStorageService;

    @Autowired
        private OnewayTripRepo pricingRepository;

        @Autowired
        private TripService transportRateService;


    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) throws Exception {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Delete old jobs + file before scheduling new one
        schedulingService.deleteExistingJobsAndFile();

        String filePath = fileStorageService.saveFile(file);

        schedulingService.scheduleSaveJob(start, end, filePath);
        schedulingService.scheduleDeleteJob(end, filePath); // Pass filePath for cleanup

        return ResponseEntity.ok("Excel uploaded and scheduled successfully. Old job and file replaced.");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Map<String, String>>> getJobs() throws SchedulerException {
        return ResponseEntity.ok(schedulingService.getScheduledJobs());
    }

    @DeleteMapping("/delete")
public ResponseEntity<String> deleteJobAndFile() throws SchedulerException, IOException {
    boolean deleted = schedulingService.deleteExistingJobsAndFile();

    // Manually clear only Excel-based data (rows with endDate in the past or today)
    pricingRepository.deleteByEndDateBefore(LocalDate.now().plusDays(1));

    return deleted ?
        ResponseEntity.ok("Deleted existing jobs, Excel file and cleared Excel-based data.") :
        ResponseEntity.ok("No job found to delete.");
}


@GetMapping("/export")
    public ModelAndView exportTransportRates() {
        List<onewayTrip> rates = transportRateService.getAllTransportRates();
        Map<String, Object> model = new HashMap<>();
        model.put("rates", rates);
        return new ModelAndView(new TransportRateExcelView(), model);
    }
    @GetMapping("/exportRound")
    public ModelAndView exportRoundTripTransportRates() {
        List<roundTrip> rates = transportRateService.getAllRoundTripTransportRates();
        Map<String, Object> model = new HashMap<>();
        model.put("rates", rates);
        return new ModelAndView(new TransportRateExcelView(), model);
    }
}
