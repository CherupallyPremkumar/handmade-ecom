package com.handmade.ecommerce.artisan.service;

import java.util.List;
import java.util.Set;

public interface ArtisanSkillService {
    
    /**
     * Add skills to an artisan
     * @param artisanId the ID of the artisan
     * @param skills the set of skills to add
     * @return the updated set of skills
     */
    Set<String> addSkills(String artisanId, Set<String> skills);
    
    /**
     * Remove skills from an artisan
     * @param artisanId the ID of the artisan
     * @param skills the set of skills to remove
     * @return the updated set of skills
     */
    Set<String> removeSkills(String artisanId, Set<String> skills);
    
    /**
     * Get all skills for an artisan
     * @param artisanId the ID of the artisan
     * @return the set of skills
     */
    Set<String> getSkills(String artisanId);
    
    /**
     * Find artisans by skill
     * @param skill the skill to search for
     * @return list of artisan IDs with the given skill
     */
    List<String> findArtisansBySkill(String skill);
    
    /**
     * Find artisans by multiple skills
     * @param skills the set of skills to search for
     * @return list of artisan IDs with any of the given skills
     */
    List<String> findArtisansBySkills(Set<String> skills);
} 