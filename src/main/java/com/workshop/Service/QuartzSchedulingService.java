package com.workshop.Service;

import com.workshop.Config.DeleteAllDataJob;
import com.workshop.Config.DeleteAllDataJobRoundTrip;
import com.workshop.Config.SaveAllDataJob;
import com.workshop.Config.SaveAllDataJobRoundTrip;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class QuartzSchedulingService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ExceFileStorageService fileStorageService;

    // ---------------- ONE WAY TRIP ----------------

    public void scheduleSaveJob(LocalDate startDate, LocalDate endDate, String filePath) throws SchedulerException {
        JobDetail jobDetail = buildSaveJobDetail(filePath, startDate, endDate);
        Trigger trigger = buildJobTrigger(jobDetail, startDate, "saveAllTrigger", "onewayTripTriggers");
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleDeleteJob(LocalDate endDate, String filePath) throws SchedulerException {
        JobDetail jobDetail = buildDeleteJobDetail(filePath);
        Trigger trigger = buildJobTrigger(jobDetail, endDate, "deleteAllTrigger", "onewayTripTriggers");
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public boolean deleteExistingJobsAndFile() throws SchedulerException {
        boolean deleted = false;
        for (String jobName : List.of("saveAllJob", "deleteAllJob")) {
            JobKey jobKey = JobKey.jobKey(jobName, "onewayTripJobs");
            if (scheduler.checkExists(jobKey)) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                String filePath = jobDataMap.getString("filePath");

                scheduler.deleteJob(jobKey);
                deleted = true;

                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.exists()) file.delete();
                }
            }
        }
        return deleted;
    }

    public List<Map<String, String>> getScheduledJobs() throws SchedulerException {
        List<Map<String, String>> jobs = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                Map<String, String> jobMap = new HashMap<>();
                jobMap.put("jobName", jobKey.getName());
                jobMap.put("groupName", jobKey.getGroup());

                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (!triggers.isEmpty()) {
                    Date nextFireTime = triggers.get(0).getNextFireTime();
                    jobMap.put("nextFireTime", nextFireTime != null ? nextFireTime.toString() : "null");
                }
                jobs.add(jobMap);
            }
        }
        return jobs;
    }

    private JobDetail buildSaveJobDetail(String filePath, LocalDate startDate, LocalDate endDate) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("filePath", filePath);
        if (startDate != null) {
            jobDataMap.put("startDate", startDate.toString());
        }
        if (endDate != null) {
            jobDataMap.put("endDate", endDate.toString());
        }
        return JobBuilder.newJob(SaveAllDataJob.class)
                .withIdentity("saveAllJob", "onewayTripJobs")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private JobDetail buildDeleteJobDetail(String filePath) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("filePath", filePath);
        return JobBuilder.newJob(DeleteAllDataJob.class)
                .withIdentity("deleteAllJob", "onewayTripJobs")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    // ---------------- ROUND TRIP ----------------

    public void scheduleRoundTripSaveJob(LocalDate startDate, LocalDate endDate, String filePath) throws SchedulerException {
        JobDetail jobDetail = buildRoundSaveJobDetail(filePath, startDate, endDate);
        Trigger trigger = buildJobTrigger(jobDetail, startDate, "roundSaveAllTrigger", "roundTripTriggers");
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void scheduleRoundTripDeleteJob(LocalDate endDate, String filePath) throws SchedulerException {
        JobDetail jobDetail = buildRoundDeleteJobDetail(filePath);
        Trigger trigger = buildJobTrigger(jobDetail, endDate, "roundDeleteAllTrigger", "roundTripTriggers");
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public boolean deleteRoundTripExistingJobsAndFile() throws SchedulerException {
        boolean deleted = false;
        for (String jobName : List.of("roundSaveAllJob", "roundDeleteAllJob")) {
            JobKey jobKey = JobKey.jobKey(jobName, "roundTripJobs");
            if (scheduler.checkExists(jobKey)) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                String filePath = jobDataMap.getString("filePath");

                scheduler.deleteJob(jobKey);
                deleted = true;

                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.exists()) file.delete();
                }
            }
        }
        return deleted;
    }

    public List<Map<String, String>> getRoundScheduledJobs() throws SchedulerException {
        List<Map<String, String>> jobs = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                if (jobKey.getGroup().equals("roundTripJobs")) {
                    Map<String, String> jobMap = new HashMap<>();
                    jobMap.put("jobName", jobKey.getName());
                    jobMap.put("groupName", jobKey.getGroup());

                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    if (!triggers.isEmpty()) {
                        Date nextFireTime = triggers.get(0).getNextFireTime();
                        jobMap.put("nextFireTime", nextFireTime != null ? nextFireTime.toString() : "null");
                    }
                    jobs.add(jobMap);
                }
            }
        }
        return jobs;
    }

    private JobDetail buildRoundSaveJobDetail(String filePath, LocalDate startDate, LocalDate endDate) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("filePath", filePath);
        if (startDate != null) {
            jobDataMap.put("startDate", startDate.toString());
        }
        if (endDate != null) {
            jobDataMap.put("endDate", endDate.toString());
        }
        return JobBuilder.newJob(SaveAllDataJobRoundTrip.class)
                .withIdentity("roundSaveAllJob", "roundTripJobs")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private JobDetail buildRoundDeleteJobDetail(String filePath) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("filePath", filePath);
        return JobBuilder.newJob(DeleteAllDataJobRoundTrip.class)
                .withIdentity("roundDeleteAllJob", "roundTripJobs")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    // ---------------- COMMON ----------------

    private Trigger buildJobTrigger(JobDetail jobDetail, LocalDate date, String triggerId, String triggerGroup) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerId, triggerGroup)
                .startAt(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}
