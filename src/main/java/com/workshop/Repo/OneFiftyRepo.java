package com.workshop.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.workshop.Entity.OneFifty;

@Repository
public interface OneFiftyRepo extends JpaRepository<OneFifty, Integer> {
    
    /**
     * Find pricing data where the given distance falls within the min-max range
     * This means: minDistance <= distance <= maxDistance
     */
    List<OneFifty> findBySourceStateAndDestinationStateAndSourceCityAndDestinationCityAndMinDistanceLessThanEqualAndMaxDistanceGreaterThanEqual(
        String sourceState, String destinationState, String sourceCity, String destinationCity, 
        Integer distance1, Integer distance2
    );
    
    /**
     * Find pricing data by state only (ignore cities) where distance falls within range
     */
    List<OneFifty> findBySourceStateAndDestinationStateAndMinDistanceLessThanEqualAndMaxDistanceGreaterThanEqual(
        String sourceState, String destinationState, Integer distance1, Integer distance2
    );
    
    /**
     * Custom query with proper distance range checking
     * This ensures the distance falls within the stored range
     */
    @Query("SELECT o FROM OneFifty o WHERE " +
           "LOWER(o.sourceState) = LOWER(:sourceState) AND " +
           "LOWER(o.destinationState) = LOWER(:destState) AND " +
           "LOWER(o.sourceCity) = LOWER(:sourceCity) AND " +
           "LOWER(o.destinationCity) = LOWER(:destCity) AND " +
           "o.minDistance <= :distance AND o.maxDistance >= :distance")
    List<OneFifty> findByExactRouteAndDistance(
        @Param("sourceState") String sourceState,
        @Param("destState") String destinationState,
        @Param("sourceCity") String sourceCity,
        @Param("destCity") String destinationCity,
        @Param("distance") Integer distance
    );
    
    /**
     * Find by state only with proper distance range
     */
    @Query("SELECT o FROM OneFifty o WHERE " +
           "LOWER(o.sourceState) = LOWER(:sourceState) AND " +
           "LOWER(o.destinationState) = LOWER(:destState) AND " +
           "o.minDistance <= :distance AND o.maxDistance >= :distance")
    List<OneFifty> findByStateAndDistance(
        @Param("sourceState") String sourceState,
        @Param("destState") String destinationState,
        @Param("distance") Integer distance
    );



    List<OneFifty> findByMinDistanceLessThanEqualAndMaxDistanceGreaterThanEqual(int min, int max);

}