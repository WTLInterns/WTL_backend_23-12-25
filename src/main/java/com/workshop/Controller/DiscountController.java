package com.workshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.Discount;
import com.workshop.Service.DiscountService;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    

    @Autowired
    private DiscountService discountService;

    @Autowired
    private FcmTestController fcmTestController;

    @PostMapping("/create")
    public Discount createDiscount(@RequestBody Discount discount){
        Discount saved = this.discountService.createDiscount(discount);
        try {
            String title = "🎟️ New Coupon Available";
            String body = "Use code " + (saved.getCouponCode() != null ? saved.getCouponCode() : "") +
                          " and save ₹" + saved.getPriceDiscount();
            String expiry = saved.getExpiryDate() != null
                    ? saved.getExpiryDate().format(DateTimeFormatter.ISO_DATE)
                    : "";
            fcmTestController.sendCouponBroadcast(
                title,
                body,
                saved.getCouponCode(),
                String.valueOf(saved.getPriceDiscount()),
                expiry
            );
        } catch (Exception ex) {
            // swallow push errors to not block coupon creation
        }
        return saved;
    }

    @GetMapping("/getAll")
    public List<Discount> getAllCouponCode(){
        return this.discountService.getAllCouponCode();
    }

    @GetMapping("/getById/{id}")
    public Discount getCouponById(@PathVariable int id){
        return this.discountService.getCouponCodeById(id);
    }

    @PutMapping("/update/{id}")
    public Discount updateCDiscount(@RequestBody Discount discount, @PathVariable int id){
        return this.discountService.updateCouponCode(discount, id);
    }   

    @DeleteMapping("/delete/{id}")
    public void deleteCouponCode(@PathVariable int id){
        this.discountService.deleteCouponCode(id);
    }

    @GetMapping("/validate")
    public ResponseEntity<Discount> validateCoupon(@RequestParam("code") String code){
        Discount d = this.discountService.findEnabledByCouponCode(code);
        if(d == null){
            return ResponseEntity.notFound().build();
        }
        if (this.discountService.isExpired(d)) {
            return ResponseEntity.status(410).build();
        }
        return ResponseEntity.ok(d);
    }
    

}
