package com.workshop.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

	@Query("SELECT MAX(b.bookid) FROM Booking b")
	String findMaxBookingId();

	List<Booking> findByUserId(String userid);

	List<Booking> findAllByOrderByIdDesc();

	void deleteByBookingId(String bookingId);

	boolean existsByBookingId(String bookingId);

	List<Booking> findByCompanyName(String companyName);


	List<Booking> findByVendorId(Long vendorId);

	// @Query("SELECT b FROM Booking b " +
	// 		"LEFT JOIN FETCH b.vendor v " +
	// 		"LEFT JOIN FETCH b.vendorCab vc " +
	// 		"LEFT JOIN FETCH b.vendorDriver vd " +
	// 		"WHERE b.id = :id")
	// Optional<Booking> findByIdWithAssociations(@Param("id") int id);


	List<Booking> findByVendorDriverVendorDriverId(int vendorDriverId);

	List<Booking> findByCarRentalUserId(int carRentalUserId);

	Optional<Booking> findByBookingId(String bookingId);

	// public Booking findBookingByCarRentalUserId(int id);

    // Admin Driver bookings
    List<Booking> findByDriveAdminId(int driveAdminId);

}
