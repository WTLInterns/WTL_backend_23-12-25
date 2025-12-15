package com.workshop.Repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.workshop.Entity.onewayTrip;
import jakarta.transaction.Transactional;

@Repository
public interface OnewayTripRepo extends JpaRepository<onewayTrip, Long> {

	List<Trip> findBySourceCityAndDestinationCity(String to, String from);
	Optional<onewayTrip> findById(Long id);
	List<onewayTrip> findBySourceStateAndDestinationStateAndSourceCityAndDestinationCity(
			String sourceState, String destinationState, String sourceCity, String destinationCity);
	List<onewayTrip> findBySourceStateAndSourceCityAndDestinationStateAndDestinationCity(
			String sourceState, String sourceCity, String destinationState, String destinationCity);

	List<onewayTrip> findBySourceCityIgnoreCaseAndSourceStateIgnoreCase(String sourceCity, String sourceState);
	List<onewayTrip> findByDestinationCityIgnoreCaseAndDestinationStateIgnoreCase(String destinationCity, String destinationState);

	@Query("SELECT DISTINCT o.sourceCity FROM onewayTrip o")
	List<String> findDistinctSourceCities();

	@Query("SELECT DISTINCT o.destinationCity FROM onewayTrip o")
	List<String> findDistinctDestinationCities();

	List<onewayTrip> findBySourceCityIgnoreCase(String sourceCity);
	List<onewayTrip> findByDestinationCityIgnoreCase(String destinationCity);
	List<onewayTrip> findBySourceCityIgnoreCaseAndDestinationCityIgnoreCase(String sourceCity, String destinationCity);

	@Modifying
	@Transactional
	@Query("DELETE FROM onewayTrip o WHERE o.endDate < :endDate")
	public  void deleteByEndDateBefore(@Param("endDate") LocalDate endDate);

	@Query("SELECT o FROM onewayTrip o WHERE o.sourceState = :sourceState AND o.destinationState = :destinationState AND o.sourceCity = :sourceCity AND o.destinationCity = :destinationCity AND o.startDate <= :currentDate AND o.endDate >= :currentDate")
	List<onewayTrip> findActiveByRouteAndDate(
			@Param("sourceState") String sourceState,
			@Param("destinationState") String destinationState,
			@Param("sourceCity") String sourceCity,
			@Param("destinationCity") String destinationCity,
			@Param("currentDate") LocalDate currentDate);

	// Method to save a list of trips
	<S extends onewayTrip> List<S> saveAll(Iterable<S> trips);

	// Method to delete all trips
	void deleteAll();
}