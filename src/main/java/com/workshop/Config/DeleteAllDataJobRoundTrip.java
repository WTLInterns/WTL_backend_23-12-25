package com.workshop.Config;

import com.workshop.Repo.OnewayTripRepo;
import com.workshop.Repo.RoundTripRepo;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DeleteAllDataJobRoundTrip implements Job {

    @Autowired
    private RoundTripRepo pricingRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        pricingRepository.deleteAll();

        String filePath = context.getMergedJobDataMap().getString("filePath");
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) file.delete();
        }
        System.out.println("Deleted all pricing data and Excel file.");
    }
}
