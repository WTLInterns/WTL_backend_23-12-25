package com.workshop.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Enquiry;
import com.workshop.Repo.EnquiryRepo;

@Service
public class EnquiryService {
   

    @Autowired
    private EnquiryRepo repo;

    @Autowired
    private EmailService emailService;



    public Enquiry save(Enquiry enquiry) {
enquiry.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    Enquiry saved = repo.save(enquiry);

    String html =
        "<!DOCTYPE html>" +
        "<html lang=\"en\">" +
        "<head>" +
        "  <meta charset=\"utf-8\">" +
        "  <title>New Enquiry</title>" +
        "</head>" +
        "<body style=\"margin:0;padding:0;background-color:#f6f7fb;\">" +
        "  <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"100%\" " +
        "         style=\"max-width:560px;background:#ffffff;margin:0 auto;border:1px solid #e5e7eb;border-radius:6px;\">" +
        "    <tr>" +
        "      <td style=\"padding:16px 20px;background:#111827;color:#ffffff;font-family:Arial,Helvetica,sans-serif;font-size:18px;\">" +
        "        New Enquiry Saved" +
        "      </td>" +
        "    </tr>" +
        "    <tr>" +
        "      <td style=\"padding:16px 20px;font-family:Arial,Helvetica,sans-serif;color:#111827;font-size:14px;line-height:20px;\">" +
        "        A new enquiry has been recorded with the following details:" +
        "      </td>" +
        "    </tr>" +
        "    <tr>" +
        "      <td style=\"padding:0 20px 16px 20px;\">" +
        "        <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" " +
        "               style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#111827;border-collapse:collapse;\">" +
        "          <tr>" +
        "            <td style=\"padding:8px 0;width:140px;color:#374151;\">Name</td>" +
        "            <td style=\"padding:8px 0;\">" + safe(saved.getName()) + "</td>" +
        "          </tr>" +
        "          <tr>" +
        "            <td style=\"padding:8px 0;width:140px;color:#374151;\">Email</td>" +
        "            <td style=\"padding:8px 0;\"><a href=\"mailto:" + safe(saved.getEmail()) + "\" style=\"color:#2563eb;text-decoration:none;\">" + safe(saved.getEmail()) + "</a></td>" +
        "          </tr>" +
        "          <tr>" +
        "            <td style=\"padding:8px 0;width:140px;color:#374151;\">Contact</td>" +
        "            <td style=\"padding:8px 0;\">" + safe(saved.getContact()) + "</td>" +
        "          </tr>" +
        "          <tr>" +
        "            <td style=\"padding:8px 0;width:140px;color:#374151;\">Service</td>" +
        "            <td style=\"padding:8px 0;\">" + safe(saved.getServiceName()) + "</td>" +
        "          </tr>" +
         "          <tr>" +
        "            <td style=\"padding:8px 0;width:140px;color:#374151;\">CompanyName</td>" +
        "            <td style=\"padding:8px 0;\">" + safe(saved.getCompanyName()) + "</td>" +
        "          </tr>" +
        "          <tr>" +
        "            <td style=\"padding:8px 0;width:140px;color:#374151;\">Timestamp</td>" +
        "            <td style=\"padding:8px 0;\">" + safe(saved.getTimeStamp()) + "</td>" +
        "          </tr>" +
        "        </table>" +
        "        <div style=\"margin:16px 0 0 0;\">" +
        "          <a href=\"mailto:" + safe(saved.getEmail()) + "?subject=Re:%20Your%20Enquiry\" " +
        "             style=\"display:inline-block;background-color:#2563eb;color:#ffffff;text-decoration:none;" +
        "                    font-family:Arial,Helvetica,sans-serif;font-size:14px;line-height:20px;padding:10px 16px;border-radius:4px;\">" +
        "             Reply to Enquirer" +
        "          </a>" +
        "        </div>" +
        "      </td>" +
        "    </tr>" +
        "    <tr>" +
        "      <td style=\"padding:12px 20px 18px 20px;text-align:center;color:#9ca3af;font-family:Arial,Helvetica,sans-serif;font-size:12px;\">" +
        "        © " + Year.now().getValue() + " WTl Tourism. All rights reserved." +
        "      </td>" +
        "    </tr>" +
        "  </table>" +
        "</body>" +
        "</html>";

        String recipients = String.join(",","jaywant61495@gmail.com",
    "sjagtap1099@gmail.com",
    "dhpathan4812@gmail.com"
);

    emailService.sendEmail(html, "New Enquiry Saved", recipients);
    return saved;
}

private static String safe(Object v) {
    return v == null ? "" : v.toString();
}


    public List<Enquiry> getAllEnquiry(){
        return this.repo.findAll();
    }

    public Enquiry getEnquiryById(int id){
        return this.repo.findById(id).get();
    }

    public void deleteEnquiry(int id){
        this.repo.deleteById(id);
    }
   




}