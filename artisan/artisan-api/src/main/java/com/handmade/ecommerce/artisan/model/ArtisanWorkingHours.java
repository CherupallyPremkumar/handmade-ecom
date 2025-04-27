package com.handmade.ecommerce.artisan.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "hm_artisan_working_hours")
public class ArtisanWorkingHours extends BaseJpaEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id", nullable = false)
    private Artisan artisan;
    
    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;
    
    @Column(name = "break_start_time")
    private LocalTime breakStartTime;
    
    @Column(name = "break_end_time")
    private LocalTime breakEndTime;
    
    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = true;
    
    @Column(name = "specific_date")
    private java.time.LocalDate specificDate;
    
    @Column(name = "notes")
    private String notes;
    
    public Artisan getArtisan() {
        return artisan;
    }
    
    public void setArtisan(Artisan artisan) {
        this.artisan = artisan;
    }
    
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public LocalTime getBreakStartTime() {
        return breakStartTime;
    }
    
    public void setBreakStartTime(LocalTime breakStartTime) {
        this.breakStartTime = breakStartTime;
    }
    
    public LocalTime getBreakEndTime() {
        return breakEndTime;
    }
    
    public void setBreakEndTime(LocalTime breakEndTime) {
        this.breakEndTime = breakEndTime;
    }
    
    public boolean isRecurring() {
        return isRecurring;
    }
    
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }
    
    public java.time.LocalDate getSpecificDate() {
        return specificDate;
    }
    
    public void setSpecificDate(java.time.LocalDate specificDate) {
        this.specificDate = specificDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Check if a given time falls within the working hours
     * @param time the time to check
     * @return true if the time is within working hours, false otherwise
     */
    public boolean isWithinWorkingHours(LocalTime time) {
        if (!isAvailable) {
            return false;
        }
        
        // Check if time is within main working hours
        boolean withinMainHours = !time.isBefore(startTime) && !time.isAfter(endTime);
        
        // If there's no break time, just return the main hours check
        if (breakStartTime == null || breakEndTime == null) {
            return withinMainHours;
        }
        
        // If time is within break hours, return false
        boolean withinBreakHours = !time.isBefore(breakStartTime) && !time.isAfter(breakEndTime);
        
        // Return true if within main hours but not within break hours
        return withinMainHours && !withinBreakHours;
    }
    
    /**
     * Check if this working hours entry applies to a specific date
     * @param date the date to check
     * @return true if the working hours apply to the date, false otherwise
     */
    public boolean appliesToDate(java.time.LocalDate date) {
        // If it's a specific date entry, check if it matches
        if (!isRecurring && specificDate != null) {
            return date.equals(specificDate);
        }
        
        // For recurring entries, check if the day of week matches
        return date.getDayOfWeek() == dayOfWeek;
    }
} 