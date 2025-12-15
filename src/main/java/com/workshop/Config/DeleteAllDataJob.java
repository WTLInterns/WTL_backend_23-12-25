package com.workshop.Config;

import com.workshop.Repo.OnewayTripRepo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;

@Component
public class DeleteAllDataJob implements Job {

    @Autowired
    private OnewayTripRepo pricingRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDate today = LocalDate.now();
        pricingRepository.deleteByEndDateBefore(today.plusDays(1));

        String filePath = context.getMergedJobDataMap().getString("filePath");
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) file.delete();
        }
        System.out.println("Deleted expired Excel pricing data and Excel file.");
    }
}
