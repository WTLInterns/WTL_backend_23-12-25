package com.workshop.Controller;



import com.razorpay.RazorpayClient;

import com.razorpay.Order;





import org.json.JSONObject;



import org.springframework.beans.factory.annotation.Value;



import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;





import java.util.Map;





@RestController

@RequestMapping("/api/payments") // Changed base path to be more specific

@CrossOrigin(origins = "*")

@Validated // Enable validation for DTOs

public class PaymentController {



    

    @Value("${razorpay.key.id}")

    private String razorpayKeyId;



    @Value("${razorpay.key.secret}")

    private String razorpayKeySecret;



   

    @PostMapping("/create-razorpay-order")

    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {

        try {

            int amount = (int) request.get("amount"); // in rupees



            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);



            JSONObject options = new JSONObject();

            options.put("amount", amount * 100); // in paise

            options.put("currency", "INR");

            options.put("receipt", "txn_" + System.currentTimeMillis());



            Order order = razorpay.orders.create(options);



            return ResponseEntity.ok(Map.of(

                "orderId", order.get("id"),

                "keyId", razorpayKeyId

            ));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(500).body(Map.of(

                "error", "Razorpay key is not configured or something went wrong."

            ));

        }

    }



}