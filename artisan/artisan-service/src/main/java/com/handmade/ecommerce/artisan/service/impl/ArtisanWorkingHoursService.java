package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import com.handmade.ecommerce.artisan.configuration.dao.ArtisanWorkingHoursRepository;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.model.ArtisanWorkingHours;
import com.handmade.ecommerce.common.exception.ResourceNotFoundException;
import com.handmade.ecommerce.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
public class ArtisanWorkingHoursService extends AbstractArtisanWorkingHoursService{


    public ArtisanWorkingHoursService(ArtisanWorkingHoursRepository workingHoursRepository, ArtisanRepository artisanRepository) {
        super(workingHoursRepository, artisanRepository);
    }

    @Override
    @Transactional
    public ArtisanWorkingHours createWorkingHours(String artisanId, ArtisanWorkingHours workingHours) {
        log.info("Creating working hours for artisan: {}", artisanId);

        Artisan artisan = super.artisanRepository.findById(artisanId)
                .orElseThrow(() -> new ResourceNotFoundException("Artisan", artisanId));

        workingHours.setArtisan(artisan);
        validateWorkingHours(workingHours);

        return workingHoursRepository.save(workingHours);
    }

    @Override
    @Transactional
    public ArtisanWorkingHours updateWorkingHours(String workingHoursId, ArtisanWorkingHours workingHours) {
        log.info("Updating working hours with id: {}", workingHoursId);

        ArtisanWorkingHours existingHours = workingHoursRepository.findById(workingHoursId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkingHours", workingHoursId));

        validateWorkingHours(workingHours);

        // Update fields while preserving the artisan reference
        existingHours.setDayOfWeek(workingHours.getDayOfWeek());
        existingHours.setStartTime(workingHours.getStartTime());
        existingHours.setEndTime(workingHours.getEndTime());
        existingHours.setAvailable(workingHours.isAvailable());
        existingHours.setBreakStartTime(workingHours.getBreakStartTime());
        existingHours.setBreakEndTime(workingHours.getBreakEndTime());
        existingHours.setRecurring(workingHours.isRecurring());
        existingHours.setSpecificDate(workingHours.getSpecificDate());
        existingHours.setNotes(workingHours.getNotes());

        return workingHoursRepository.save(existingHours);
    }

    @Override
    @Transactional
    public void deleteWorkingHours(String workingHoursId) {
        log.info("Deleting working hours with id: {}", workingHoursId);

        if (!workingHoursRepository.existsById(workingHoursId)) {
            throw new ResourceNotFoundException("WorkingHours", workingHoursId);
        }

        workingHoursRepository.deleteById(workingHoursId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtisanWorkingHours> getWorkingHoursByArtisan(String artisanId) {
        log.info("Getting working hours for artisan: {}", artisanId);

        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan", artisanId);
        }

        return workingHoursRepository.findByArtisanId(artisanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtisanWorkingHours> getWorkingHoursByArtisanAndDay(String artisanId, DayOfWeek dayOfWeek) {
        log.info("Getting working hours for artisan: {} on day: {}", artisanId, dayOfWeek);

        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan", artisanId);
        }

        return workingHoursRepository.findByArtisanIdAndDayOfWeek(artisanId, dayOfWeek);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtisanWorkingHours> getWorkingHoursByArtisanAndDate(String artisanId, LocalDate date) {
        log.info("Getting working hours for artisan: {} on date: {}", artisanId, date);

        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan", artisanId);
        }

        return workingHoursRepository.findByArtisanIdAndDate(artisanId, date, date.getDayOfWeek());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAvailable(String artisanId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        log.info("Checking availability for artisan: {} on date: {} from {} to {}",
                artisanId, date, startTime, endTime);

        if (!artisanRepository.existsById(artisanId)) {
            throw new ResourceNotFoundException("Artisan", artisanId);
        }

        List<ArtisanWorkingHours> workingHours = workingHoursRepository.findByArtisanIdAndDate(
                artisanId, date, date.getDayOfWeek());

        return workingHours.stream()
                .filter(ArtisanWorkingHours::isAvailable)
                .anyMatch(hours -> isTimeOverlapping(hours, startTime, endTime));
    }

    @Override
    @Transactional
    public void setAvailability(String workingHoursId, boolean isAvailable) {
        log.info("Setting availability for working hours: {} to: {}", workingHoursId, isAvailable);

        ArtisanWorkingHours workingHours = workingHoursRepository.findById(workingHoursId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkingHours", workingHoursId));

        workingHours.setAvailable(isAvailable);
        workingHoursRepository.save(workingHours);
    }

    @Override
    @Transactional
    public void setBreakTime(String workingHoursId, LocalTime breakStartTime, LocalTime breakEndTime) {
        log.info("Setting break time for working hours: {} from {} to {}",
                workingHoursId, breakStartTime, breakEndTime);

        ArtisanWorkingHours workingHours = workingHoursRepository.findById(workingHoursId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkingHours", workingHoursId));

        validateBreakTime(workingHours.getStartTime(), workingHours.getEndTime(), breakStartTime, breakEndTime);

        workingHours.setBreakStartTime(breakStartTime);
        workingHours.setBreakEndTime(breakEndTime);
        workingHoursRepository.save(workingHours);
    }

    private void validateWorkingHours(ArtisanWorkingHours workingHours) {
        if (workingHours.getStartTime() == null || workingHours.getEndTime() == null) {
            throw new ValidationException("startTime", null, "Start time and end time are required");
        }

        if (workingHours.getStartTime().isAfter(workingHours.getEndTime())) {
            throw new ValidationException("startTime", workingHours.getStartTime(), "Start time must be before end time");
        }

        if (workingHours.isRecurring() && workingHours.getDayOfWeek() == null) {
            throw new ValidationException("dayOfWeek", null, "Day of week is required for recurring working hours");
        }

        if (!workingHours.isRecurring() && workingHours.getSpecificDate() == null) {
            throw new ValidationException("specificDate", null, "Specific date is required for non-recurring working hours");
        }

        if (workingHours.getBreakStartTime() != null || workingHours.getBreakEndTime() != null) {
            validateBreakTime(workingHours.getStartTime(), workingHours.getEndTime(),
                    workingHours.getBreakStartTime(), workingHours.getBreakEndTime());
        }
    }

    private void validateBreakTime(LocalTime startTime, LocalTime endTime,
                                   LocalTime breakStartTime, LocalTime breakEndTime) {
        if (breakStartTime == null || breakEndTime == null) {
            throw new ValidationException("breakTime", null, "Both break start time and end time are required");
        }

        if (breakStartTime.isAfter(breakEndTime)) {
            throw new ValidationException("breakStartTime", breakStartTime, "Break start time must be before break end time");
        }

        if (breakStartTime.isBefore(startTime) || breakEndTime.isAfter(endTime)) {
            throw new ValidationException("breakTime", breakStartTime + "-" + breakEndTime, "Break time must be within working hours");
        }
    }

    private boolean isTimeOverlapping(ArtisanWorkingHours workingHours,
                                      LocalTime startTime, LocalTime endTime) {
        // Check if the time range overlaps with the main working hours
        boolean overlapsMainHours = !startTime.isAfter(workingHours.getEndTime()) &&
                !endTime.isBefore(workingHours.getStartTime());

        if (!overlapsMainHours) {
            return false;
        }

        // If there's no break time, the overlap with main hours is sufficient
        if (workingHours.getBreakStartTime() == null || workingHours.getBreakEndTime() == null) {
            return true;
        }

        // Check if the time range overlaps with the break time
        boolean overlapsBreakTime = !startTime.isAfter(workingHours.getBreakEndTime()) &&
                !endTime.isBefore(workingHours.getBreakStartTime());

        // Return true if it overlaps with main hours but not with break time
        return !overlapsBreakTime;
    }
}
