package com.workshop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Service.EmailService;

@RestController
@RequestMapping("/test")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public String testEmail(@RequestParam String to) {
        // Test data
        String name = "John Doe";
        String bookingId = "WTL123456";
        String pickupLocation = "Mumbai Airport, Terminal 2";
        String dropLocation = "Hotel Grand, Marine Drive";
        String tripType = "One Way";
        String date = "2025-11-05";
        String time = "14:30";
        String total = "2,450";

        String subject = "Booking Confirmation - " + bookingId;
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Booking Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; background-color: #f5f7fa;\">\n" +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 0;\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 650px; margin: 0 auto;\">\n" +
                "                    <tr>\n" +
                "                        <td align=\"center\" style=\"padding: 20px 0;\">\n" +
                "                            <h1 style=\"color: #ffffff; margin: 0; font-size: 32px; font-weight: 700;\">WTL Cab Service</h1>\n" +
                "                            <p style=\"color: #e0e0e0; margin: 8px 0 0 0; font-size: 16px;\">Reliable. Comfortable. On-time.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 650px; margin: -30px auto 0; background-color: #ffffff; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.1);\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 40px 35px;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"text-align: center; margin-bottom: 30px;\">\n" +
                "                                <div style=\"background-color: #e8f5e9; width: 70px; height: 70px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 20px;\">\n" +
                "                                    <span style=\"font-size: 30px; color: #4caf50;\">✓</span>\n" +
                "                                </div>\n" +
                "                                <h2 style=\"color: #2e7d32; margin: 0 0 10px 0; font-size: 28px; font-weight: 700;\">Booking Confirmed!</h2>\n" +
                "                                <p style=\"color: #666; margin: 0; font-size: 18px;\">Thank you for choosing WTL Cab Service</p>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"background-color: #f8f9fa; border-radius: 10px; padding: 25px; margin-bottom: 30px; border: 1px solid #e9ecef;\">\n" +
                "                                <h3 style=\"margin-top: 0; color: #1a1f2e; font-size: 20px; font-weight: 600;\">Hello " + name + ",</h3>\n" +
                "                                <p style=\"font-size: 16px; line-height: 26px; margin-bottom: 0;\">Your booking has been successfully confirmed. Here are the details of your trip:</p>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom: 30px;\">\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 15px 20px; background: linear-gradient(to right, #667eea, #764ba2); color: white; border-radius: 8px 8px 0 0; text-align: center;\">\n" +
                "                                        <h3 style=\"margin: 0; font-size: 20px; font-weight: 600;\">Booking ID: " + bookingId + "</h3>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 0; border: 1px solid #e0e0e0; border-top: none; border-radius: 0 0 8px 8px;\">\n" +
                "                                        <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                            <tr>\n" +
                "                                                <td style=\"padding: 20px;\">\n" +
                "                                                    <h4 style=\"margin: 0 0 15px 0; color: #1a1f2e; font-size: 18px; font-weight: 600; border-bottom: 2px solid #667eea; padding-bottom: 8px;\">Trip Information</h4>\n" +
                "                                                    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Trip Type:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + tripType + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Date:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + date + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Time:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + time + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Distance:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + (pickupLocation.equals(dropLocation) ? "Local Trip" : "Outstation") + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Pickup Location:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + pickupLocation + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Drop-off Location:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + dropLocation + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                    </table>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "\n" +
                "                            <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; padding: 25px; margin-bottom: 30px; color: white; text-align: center;\">\n" +
                "                                <h3 style=\"margin: 0 0 15px 0; font-size: 22px; font-weight: 600;\">Payment Summary</h3>\n" +
                "                                <div style=\"font-size: 36px; font-weight: 700; margin-bottom: 5px;\">₹" + total + "</div>\n" +
                "                                <p style=\"margin: 0; font-size: 16px; opacity: 0.9;\">Amount Paid Successfully</p>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"margin-bottom: 30px;\">\n" +
                "                                <h3 style=\"color: #1a1f2e; margin: 0 0 20px 0; font-size: 20px; font-weight: 600; text-align: center;\">Important Information</h3>\n" +
                "                                <ul style=\"padding-left: 20px; margin: 0;\">\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">Please arrive at the pickup location at least 10 minutes before the scheduled time.</li>\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">Driver details will be shared 30 minutes before pickup time.</li>\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">For any changes or cancellations, contact us at least 2 hours before pickup.</li>\n" +
                "                                    <li style=\"font-size: 15px; line-height: 24px;\">Toll charges, parking fees, and other taxes are not included and will be charged as applicable.</li>\n" +
                "                                </ul>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"text-align: center; margin: 30px 0;\">\n" +
                "                                <a href=\"tel:+919876543210\" style=\"display: inline-block; background-color: #667eea; color: white; text-decoration: none; padding: 14px 28px; border-radius: 30px; font-weight: 600; font-size: 16px; margin: 0 10px;\">Call Support</a>\n" +
                "                                <a href=\"mailto:support@wtlcabs.com\" style=\"display: inline-block; background-color: #2e7d32; color: white; text-decoration: none; padding: 14px 28px; border-radius: 30px; font-weight: 600; font-size: 16px; margin: 0 10px;\">Email Support</a>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <p style=\"font-size: 16px; line-height: 26px; margin-bottom: 0; text-align: center;\">We wish you a safe and comfortable journey!</p>\n" +
                "                            <p style=\"font-size: 16px; line-height: 26px; margin: 10px 0 0 0; text-align: center;\"><strong>Best regards,</strong><br>WTL Cab Service Team</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 25px 35px; background-color: #f8f9fa; border-radius: 0 0 12px 12px; border-top: 1px solid #eee;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align: center; font-size: 13px; color: #6c757d;\">\n" +
                "                            <p style=\"margin: 0 0 15px 0;\">&copy; 2025 WTL Cab Service. All rights reserved.</p>\n" +
                "                            <p style=\"margin: 0;\">If you have any questions, please contact our customer support at <a href=\"mailto:support@wtlcabs.com\" style=\"color: #667eea; text-decoration: none;\">support@wtlcabs.com</a> or call <a href=\"tel:+919876543210\" style=\"color: #667eea; text-decoration: none;\">+91 9876543210</a></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        boolean result = emailService.sendEmail(message, subject, to);
        return result ? "Email sent successfully!" : "Failed to send email.";
    }
}