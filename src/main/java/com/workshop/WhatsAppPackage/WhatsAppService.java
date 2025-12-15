package com.workshop.WhatsAppPackage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${whatsapp.api.url:https://graph.facebook.com/v22.0}")
    private String whatsappApiUrl;

    @Value("${whatsapp.access.token}")
    private String accessToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    private final RestTemplate restTemplate = new RestTemplate();



    public WhatsAppResponse sendBookingConfirmation(String customerPhone, BookingDetails booking) {
        try {
            if (customerPhone == null || customerPhone.trim().isEmpty()) {
                throw new WhatsAppServiceException("Customer phone number is required");
            }

            if (booking == null || booking.getBookingId() == null) {
                throw new WhatsAppServiceException("Booking details are required");
            }

            String cleanPhone = cleanPhoneNumber(customerPhone);
            logger.info("📱 Original phone: {} → Cleaned phone: {}", customerPhone, cleanPhone);

            WhatsAppMessageRequest request = buildBookingMessage(cleanPhone, booking);

            try {
                String requestJson = objectMapper.writeValueAsString(request);
                logger.info("📤 WhatsApp Request JSON: {}", requestJson);
            } catch (Exception e) {
                logger.warn("Could not serialize request for logging: {}", e.getMessage());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            logger.info("📋 Request Headers: Content-Type={}, Authorization=Bearer [HIDDEN]",
                    headers.getContentType());

            HttpEntity<WhatsAppMessageRequest> entity = new HttpEntity<>(request, headers);

            String url = String.format("%s/%s/messages", whatsappApiUrl, phoneNumberId);
            logger.info("📡 WhatsApp API URL: {}", url);

            ResponseEntity<WhatsAppResponse> response = restTemplate.postForEntity(url, entity, WhatsAppResponse.class);

            logger.info("📥 WhatsApp API Response Status: {}", response.getStatusCode());

            if (response.getBody() != null) {
                try {
                    String responseJson = objectMapper.writeValueAsString(response.getBody());
                    logger.info("WhatsApp Response JSON: {}", responseJson);
                } catch (Exception e) {
                    logger.warn("Could not serialize response for logging: {}", e.getMessage());
                }

                WhatsAppResponse whatsAppResponse = response.getBody();

                if (whatsAppResponse.getMessages() != null && !whatsAppResponse.getMessages().isEmpty()) {
                    for (WhatsAppMessage msg : whatsAppResponse.getMessages()) {
                        logger.info("Message ID: {}, Status: {}", msg.getId(), msg.getMessageStatus());
                    }
                }

                if (whatsAppResponse.getContacts() != null && !whatsAppResponse.getContacts().isEmpty()) {
                    for (WhatsAppContact contact : whatsAppResponse.getContacts()) {
                        logger.info("Contact - Input: {}, WA ID: {}", contact.getInput(), contact.getWaId());
                    }
                }
            }

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("WhatsApp booking confirmation sent to {} for booking {}",
                        cleanPhone, booking.getBookingId());
                return response.getBody();
            } else {
                logger.warn("⚠️ WhatsApp API returned non-success status: {}", response.getStatusCode());
                return null;
            }

        } catch (HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            logger.error("WhatsApp API error for booking {}: {} - Response: {}",
                    booking.getBookingId(), e.getMessage(), errorBody);
            throw new WhatsAppServiceException("WhatsApp API error: " + e.getMessage(), e);

        } catch (Exception e) {
            logger.error("Failed to send WhatsApp booking confirmation for {}: {}",
                    booking.getBookingId(), e.getMessage(), e);
            throw new WhatsAppServiceException("Failed to send WhatsApp message: " + e.getMessage(), e);
        }
    }


    private WhatsAppMessageRequest buildBookingMessage(String phone, BookingDetails booking) {
        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setMessagingProduct("whatsapp");
        request.setTo(phone);
        request.setType("text");

        String messageText = buildSimpleTestMessage(booking);

        WhatsAppText text = new WhatsAppText();
        text.setBody(messageText);
        request.setText(text);

        logger.info("📝 Built WhatsApp message for booking: {} to phone: {}", booking.getBookingId(), phone);
        return request;
    }

    private String getTermsAndConditionsText() {
        return "\n\n📌 *Terms and Conditions:*\n" +
                "• Toll charges, parking fees, and other taxes are not included and will be charged as applicable.\n" +
                "• Prices may be extended in case of route changes, additional stops, or waiting time.\n" +
                "• Please refer to our website or contact support for detailed terms and conditions.";
    }



    private String buildSimpleTestMessage(BookingDetails booking) {
        return String.format(
                "🎉 *BOOKING CONFIRMED* 🎉\n\n" +
                        "Hello *%s*! 👋\n\n" +
                        "Your cab booking has been successfully confirmed.\n\n" +
                        "📋 *BOOKING DETAILS*\n" +
                        "━━━━━━━━━━━━━━━━━━━━\n" +
                        "🆔 Booking ID: *%s*\n" +
                        "📍 Pickup: %s\n" +
                        "🎯 Drop: %s\n" +
                        "📅 Date: %s\n" +
                        "🕐 Time: %s\n" +
                        "💰 Amount: *Rs %s*\n" +
                        "━━━━━━━━━━━━━━━━━━━━\n\n" +
                        "🚗 Your driver details will be shared soon.\n\n" +
                        "Thank you for choosing our service! ✨"+
                        "%s",
                booking.getCustomerName(),
                booking.getBookingId(),
                booking.getPickupLocation(),
                booking.getDropLocation(),
                booking.getDate(),
                booking.getTime(),
                booking.getPrice(),
                getTermsAndConditionsText()
        );
    }


    private String cleanPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        String cleaned = phone.replaceAll("[^+\\d]", "");
        logger.debug("🧹 After removing non-digits: {}", cleaned);

        if (cleaned.startsWith("+")) {
            cleaned = cleaned.substring(1);
            logger.debug("🧹 After removing +: {}", cleaned);
        }

        if (cleaned.length() == 10) {
            cleaned = "91" + cleaned;
            logger.debug("🧹 Added India code (91): {}", cleaned);
        } else if (cleaned.startsWith("0") && cleaned.length() == 11) {
            cleaned = "91" + cleaned.substring(1);
            logger.debug("🧹 Removed leading 0 and added India code: {}", cleaned);
        }

        if (cleaned.length() < 10 || cleaned.length() > 15) {
            throw new IllegalArgumentException("Invalid phone number format: " + phone + " -> " + cleaned);
        }

        logger.info("Final cleaned phone number: {}", cleaned);
        return cleaned;
    }


    public boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        try {
            String cleaned = cleanPhoneNumber(phone);
            boolean valid = cleaned.length() >= 10 && cleaned.length() <= 15;
            logger.debug("Phone validation - Input: {}, Cleaned: {}, Valid: {}", phone, cleaned, valid);
            return valid;
        } catch (Exception e) {
            logger.debug("Invalid phone number: {} - {}", phone, e.getMessage());
            return false;
        }
    }


    public WhatsAppResponse sendDriverAssignmentNotification(String customerPhone, String driverName, String driverPhone, String vehicleNo, String bookingId) {
        try {
            if (customerPhone == null || customerPhone.trim().isEmpty()) {
                throw new WhatsAppServiceException("Customer phone number is required");
            }

            String cleanPhone = cleanPhoneNumber(customerPhone);
            logger.info("📱 Sending driver assignment notification to customer: {}", cleanPhone);

            WhatsAppMessageRequest request = new WhatsAppMessageRequest();
            request.setMessagingProduct("whatsapp");
            request.setTo(cleanPhone);
            request.setType("text");

            String messageText = String.format(
                    "🚗 *DRIVER ASSIGNED* 🚗\n\n" +
                            "Hello! Your driver has been assigned for your trip.\n\n" +
                            "📋 *TRIP DETAILS*\n" +
                            "━━━━━━━━━━━━━━━━━━━━\n" +
                            "🆔 Booking ID: *%s*\n\n" +
                            "👨‍💼 *DRIVER INFORMATION*\n" +
                            "━━━━━━━━━━━━━━━━━━━━\n" +
                            "👤 Driver Name: *%s*\n" +
                            "📞 Driver Phone: *%s*\n" +
                            "🚙 Vehicle Number: *%s*\n" +
                            "━━━━━━━━━━━━━━━━━━━━\n\n" +
                            "✅ Please save the driver's contact number.\n" +
                            "🕐 Driver will contact you before pickup.\n\n" +
                            "Have a safe and pleasant journey! 🛣️✨"+
                            "%s",
                    bookingId,
                    driverName != null ? driverName : "TBD",
                    driverPhone != null ? driverPhone : "TBD",
                    vehicleNo != null ? vehicleNo : "TBD",
                    getTermsAndConditionsText()
            );

            WhatsAppText text = new WhatsAppText();
            text.setBody(messageText);
            request.setText(text);

            logger.info("Built message - Driver: {}, Phone: {}, Vehicle: {}", driverName, driverPhone, vehicleNo);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<WhatsAppMessageRequest> entity = new HttpEntity<>(request, headers);

            String url = String.format("%s/%s/messages", whatsappApiUrl, phoneNumberId);

            ResponseEntity<WhatsAppResponse> response = restTemplate.postForEntity(url, entity, WhatsAppResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Driver assignment notification sent to customer {} for booking {}", cleanPhone, bookingId);
                return response.getBody();
            } else {
                logger.warn("Customer WhatsApp API returned non-success status: {}", response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            logger.error("Failed to send driver assignment notification to customer for {}: {}", bookingId, e.getMessage());
            throw new WhatsAppServiceException("Failed to send customer WhatsApp message: " + e.getMessage(), e);
        }
    }
}