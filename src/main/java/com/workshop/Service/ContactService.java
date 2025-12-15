package com.workshop.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Contact;
import com.workshop.Repo.ContactRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private EmailService emailService;

    public Contact saveContact(Contact contact) {
    // Set timestamp
    contact.setTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    
    // Save to database
    Contact savedContact = this.contactRepo.save(contact);
    
    // Send email notification
    String html =
        "<!DOCTYPE html>" +
        "<html lang=\"en\">" +
        "<head>" +
        "  <meta charset=\"utf-8\">" +
        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
        "  <title>New Customer Contact on WTL Website</title>" +
        "</head>" +
        "<body style=\"margin:0;padding:20px;background-color:#f8fafc;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Oxygen,Ubuntu,Cantarell,sans-serif;\">" +
        "  <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"100%\" " +
        "         style=\"max-width:600px;background:#ffffff;margin:0 auto;border-radius:12px;box-shadow:0 4px 6px -1px rgba(0,0,0,0.1);overflow:hidden;\">" +
        "    <!-- Header -->" +
        "    <tr>" +
        "      <td style=\"padding:32px 32px 24px 32px;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);text-align:center;\">" +
        "        <h1 style=\"margin:0;color:#ffffff;font-size:24px;font-weight:600;letter-spacing:-0.025em;\">📧 New Customer Enquiry</h1>" +
        "        <p style=\"margin:8px 0 0 0;color:#e2e8f0;font-size:16px;opacity:0.9;\">A new enquiry has been submitted</p>" +
        "      </td>" +
        "    </tr>" +
        "    <!-- Content -->" +
        "    <tr>" +
        "      <td style=\"padding:32px;\">" +
        "        <!-- Customer Details Card -->" +
        "        <div style=\"background:#f8fafc;border-radius:8px;padding:24px;margin-bottom:24px;border-left:4px solid #667eea;\">" +
        "          <h2 style=\"margin:0 0 16px 0;color:#1e293b;font-size:18px;font-weight:600;\">👤 Customer Information</h2>" +
        "          <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" " +
        "                 style=\"font-size:14px;line-height:1.6;\">" +
        "            <tr>" +
        "              <td style=\"padding:6px 0;width:100px;color:#64748b;font-weight:500;\">Name:</td>" +
        "              <td style=\"padding:6px 0;color:#1e293b;font-weight:600;\">" + contact.getName() + "</td>" +
        "            </tr>" +
        "            <tr>" +
        "              <td style=\"padding:6px 0;width:100px;color:#64748b;font-weight:500;\">Email:</td>" +
        "              <td style=\"padding:6px 0;\">" +
        "                <a href=\"mailto:" + contact.getEmail() + "\" " +
        "                   style=\"color:#667eea;text-decoration:none;font-weight:500;\">" + contact.getEmail() + "</a>" +
        "              </td>" +
        "            </tr>" +
        "            <tr>" +
        "              <td style=\"padding:6px 0;width:100px;color:#64748b;font-weight:500;\">Phone:</td>" +
        "              <td style=\"padding:6px 0;color:#1e293b;font-weight:600;\">" +
        "                <a href=\"tel:" + contact.getPhone() + "\" " +
        "                   style=\"color:#1e293b;text-decoration:none;\">" + contact.getPhone() + "</a>" +
        "              </td>" +
        "            </tr>" +
        "            <tr>" +
        "              <td style=\"padding:6px 0;width:100px;color:#64748b;font-weight:500;\">Submitted:</td>" +
        "              <td style=\"padding:6px 0;color:#64748b;\">" + contact.getTimeStamp() + "</td>" +
        "            </tr>" +
        "          </table>" +
        "        </div>" +
        "        <!-- Enquiry Details Card -->" +
        "        <div style=\"background:#fefefe;border-radius:8px;padding:24px;border:1px solid #e2e8f0;\">" +
        "          <h2 style=\"margin:0 0 8px 0;color:#1e293b;font-size:18px;font-weight:600;\">📝 Enquiry Details</h2>" +
        "          <div style=\"margin-bottom:16px;\">" +
        "            <p style=\"margin:0 0 4px 0;color:#64748b;font-size:12px;font-weight:500;text-transform:uppercase;letter-spacing:0.05em;\">Subject</p>" +
        "            <p style=\"margin:0;color:#1e293b;font-size:16px;font-weight:600;\">" + contact.getSubject() + "</p>" +
        "          </div>" +
        "          <div>" +
        "            <p style=\"margin:0 0 8px 0;color:#64748b;font-size:12px;font-weight:500;text-transform:uppercase;letter-spacing:0.05em;\">Message</p>" +
        "            <div style=\"background:#f8fafc;border-radius:6px;padding:16px;border-left:3px solid #e2e8f0;\">" +
        "              <p style=\"margin:0;color:#1e293b;font-size:14px;line-height:1.6;white-space:pre-wrap;\">" + contact.getMessage() + "</p>" +
        "            </div>" +
        "          </div>" +
        "        </div>" +
        "        <!-- Action Buttons -->" +
        "        <div style=\"margin-top:32px;text-align:center;\">" +
        "          <a href=\"mailto:" + contact.getEmail() + "?subject=Re:%20" + contact.getSubject() + "\" " +
        "             style=\"display:inline-block;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#ffffff;text-decoration:none;" +
        "                    font-size:14px;font-weight:600;padding:12px 24px;border-radius:6px;margin:0 8px 8px 0;" +
        "                    box-shadow:0 2px 4px rgba(102,126,234,0.3);transition:all 0.2s;\">" +
        "             ✉️ Reply via Email" +
        "          </a>" +
        "          <a href=\"tel:" + contact.getPhone() + "\" " +
        "             style=\"display:inline-block;background:#ffffff;color:#667eea;text-decoration:none;border:2px solid #667eea;" +
        "                    font-size:14px;font-weight:600;padding:10px 24px;border-radius:6px;margin:0 8px 8px 0;" +
        "                    transition:all 0.2s;\">" +
        "             📞 Call Customer" +
        "          </a>" +
        "        </div>" +
        "      </td>" +
        "    </tr>" +
        "    <!-- Footer -->" +
        "    <tr>" +
        "      <td style=\"padding:24px 32px;text-align:center;background:#f8fafc;border-top:1px solid #e2e8f0;\">" +
        "        <p style=\"margin:0;color:#64748b;font-size:12px;line-height:1.5;\">" +
        "          © " + Year.now().getValue() + " <strong style=\"color:#1e293b;\">WTL Tourism</strong><br>" +
        "          This enquiry was automatically forwarded to your team." +
        "        </p>" +
        "      </td>" +
        "    </tr>" +
        "  </table>" +
        "</body>" +
        "</html>";

    String recipients = String.join(",",
        "jaywant61495@gmail.com",
        "sjagtap1099@gmail.com",
        "dhpathan4812@gmail.com"
    );

    // Fixed: removed extra closing parenthesis
    emailService.sendEmail(html, "🔔 New Customer Enquiry - " + contact.getSubject(), recipients);
    
    // Return the saved contact
    return savedContact;
}


    public List<Contact> getAllContacts() {
        return this.contactRepo.findAll();
    }

    public void deleteContact(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Contact ID must be a positive number");
        }
        
        if (!this.contactRepo.existsById(id)) {
            throw new EntityNotFoundException("Contact with ID " + id + " not found");
        }
        
        this.contactRepo.deleteById(id);
    }
}