package com.handmade.ecommerce.artisan.service;

import com.handmade.ecommerce.artisan.model.ArtisanWorkingHours;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service interface for managing artisan working hours.
 * This service handles both recurring and specific date schedules,
 * as well as break times and availability management.
 */
public interface ArtisanWorkingHoursService {

    /**
     * Create new working hours for an artisan
     * @param artisanId the ID of the artisan
     * @param workingHours the working hours to create
     * @return the created working hours
     */
    ArtisanWorkingHours createWorkingHours(String artisanId, ArtisanWorkingHours workingHours);

    /**
     * Update existing working hours
     * @param workingHoursId the ID of the working hours to update
     * @param workingHours the updated working hours
     * @return the updated working hours
     */
    ArtisanWorkingHours updateWorkingHours(String workingHoursId, ArtisanWorkingHours workingHours);

    /**
     * Delete working hours
     * @param workingHoursId the ID of the working hours to delete
     */
    void deleteWorkingHours(String workingHoursId);

    /**
     * Get all working hours for an artisan
     * @param artisanId the ID of the artisan
     * @return list of working hours
     */
    List<ArtisanWorkingHours> getWorkingHoursByArtisan(String artisanId);

    /**
     * Get working hours for an artisan on a specific day of the week
     * @param artisanId the ID of the artisan
     * @param dayOfWeek the day of the week
     * @return list of working hours
     */
    List<ArtisanWorkingHours> getWorkingHoursByArtisanAndDay(String artisanId, DayOfWeek dayOfWeek);

    /**
     * Get working hours for an artisan on a specific date
     * @param artisanId the ID of the artisan
     * @param date the date
     * @return list of working hours
     */
    List<ArtisanWorkingHours> getWorkingHoursByArtisanAndDate(String artisanId, LocalDate date);

    /**
     * Check if an artisan is available during a specific time period
     * @param artisanId the ID of the artisan
     * @param date the date to check
     * @param startTime the start time
     * @param endTime the end time
     * @return true if available, false otherwise
     */
    boolean isAvailable(String artisanId, LocalDate date, LocalTime startTime, LocalTime endTime);

    /**
     * Set the availability status for working hours
     * @param workingHoursId the ID of the working hours
     * @param isAvailable the availability status
     */
    void setAvailability(String workingHoursId, boolean isAvailable);

    /**
     * Set break time for working hours
     * @param workingHoursId the ID of the working hours
     * @param breakStartTime the break start time
     * @param breakEndTime the break end time
     */
    void setBreakTime(String workingHoursId, LocalTime breakStartTime, LocalTime breakEndTime);
} 