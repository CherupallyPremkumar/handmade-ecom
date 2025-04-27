package com.handmade.ecommerce.artisan.configuration.dao;

import com.handmade.ecommerce.artisan.model.ArtisanWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ArtisanWorkingHoursRepository extends JpaRepository<ArtisanWorkingHours, String> {
    
    /**
     * Find all working hours for a specific artisan
     * @param artisanId the ID of the artisan
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId")
    List<ArtisanWorkingHours> findByArtisanId(@Param("artisanId") String artisanId);
    
    /**
     * Find all working hours for a specific artisan and day of week
     * @param artisanId the ID of the artisan
     * @param dayOfWeek the day of week
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId AND w.dayOfWeek = :dayOfWeek AND w.isRecurring = true")
    List<ArtisanWorkingHours> findByArtisanIdAndDayOfWeek(@Param("artisanId") String artisanId, @Param("dayOfWeek") DayOfWeek dayOfWeek);
    
    /**
     * Find all working hours for a specific artisan and date
     * @param artisanId the ID of the artisan
     * @param date the date
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId AND ((w.isRecurring = true AND w.dayOfWeek = :dayOfWeek) OR (w.isRecurring = false AND w.specificDate = :date))")
    List<ArtisanWorkingHours> findByArtisanIdAndDate(@Param("artisanId") String artisanId, @Param("date") LocalDate date, @Param("dayOfWeek") DayOfWeek dayOfWeek);
    
    /**
     * Find all working hours for a specific artisan that overlap with a given time range
     * @param artisanId the ID of the artisan
     * @param startTime the start time
     * @param endTime the end time
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId AND w.isAvailable = true AND ((w.startTime <= :endTime AND w.endTime >= :startTime) OR (w.breakStartTime IS NOT NULL AND w.breakStartTime <= :endTime AND w.breakEndTime >= :startTime))")
    List<ArtisanWorkingHours> findByArtisanIdAndTimeOverlap(@Param("artisanId") String artisanId, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
    
    /**
     * Find all working hours for a specific artisan that are available
     * @param artisanId the ID of the artisan
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId AND w.isAvailable = true")
    List<ArtisanWorkingHours> findAvailableByArtisanId(@Param("artisanId") String artisanId);
    
    /**
     * Find all working hours for a specific artisan that are recurring
     * @param artisanId the ID of the artisan
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId AND w.isRecurring = true")
    List<ArtisanWorkingHours> findRecurringByArtisanId(@Param("artisanId") String artisanId);
    
    /**
     * Find all working hours for a specific artisan that are for a specific date
     * @param artisanId the ID of the artisan
     * @param date the date
     * @return list of working hours
     */
    @Query("SELECT w FROM ArtisanWorkingHours w WHERE w.artisan.id = :artisanId AND w.isRecurring = false AND w.specificDate = :date")
    List<ArtisanWorkingHours> findSpecificDateByArtisanId(@Param("artisanId") String artisanId, @Param("date") LocalDate date);
}
