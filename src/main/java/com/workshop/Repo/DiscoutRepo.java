package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Discount;


@Repository
public interface DiscoutRepo extends JpaRepository<Discount,Integer>{
    Discount findByCouponCode(String couponCode);
    Discount findByCouponCodeAndIsEnabled(String couponCode, String isEnabled);
}
