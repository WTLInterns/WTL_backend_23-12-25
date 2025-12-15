//package com.workshop.Service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.workshop.Entity.roundTrip;
//import com.workshop.Repo.RoundTripRepo;
//
//@Service
//public class RoundTripService {
//	
//	@Autowired
//	private RoundTripRepo roundrepo;
//
//	
//	public void updatePricesByRoundWay(String sourceState, String destinationState, String sourceCity, String destinationCity ,int hatchbackPrice, int sedanPrice, int sedanPremiumPrice, int suvPrice, int suvPlusPrice) {
//        List<roundTrip> trips = this.roundrepo.findBySourceStateAndDestinationStateAndSourceCityAndDestinationCity(sourceState, destinationState,sourceCity,destinationCity);
//        
//        for (roundTrip trip : trips) {
//            trip.setHatchback(hatchbackPrice);
//            trip.setSedan(sedanPrice);
//            trip.setSedanpremium(sedanPremiumPrice);
//            trip.setSuv(suvPrice);
//            trip.setSuvplus(suvPlusPrice);
//        }
//        this.roundrepo.saveAll(trips);
//    }
//	
//}
