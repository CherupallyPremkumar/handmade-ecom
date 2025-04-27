package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.service.ArtisanSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class  ArtisanSkillServiceImpl implements ArtisanSkillService {

    private final ArtisanRepository artisanRepository;
    
    // Trie node for efficient skill prefix search
    private static class TrieNode {
        Map<Character, TrieNode> children;
        Set<String> artisanIds;
        boolean isEndOfWord;
        
        TrieNode() {
            children = new ConcurrentHashMap<>();
            artisanIds = ConcurrentHashMap.newKeySet();
            isEndOfWord = false;
        }
    }
    
    private final TrieNode skillTrieRoot = new TrieNode();
    private final Map<String, Set<String>> skillToArtisanMap = new ConcurrentHashMap<>();
    
    @Autowired
    public ArtisanSkillServiceImpl(ArtisanRepository artisanRepository) {
        this.artisanRepository = artisanRepository;
    }
    
    @PostConstruct
    public void initialize() {
        // Load all artisans and build the trie
        artisanRepository.findAll().forEach(this::updateSkillIndexes);
    }
    
    private void updateSkillIndexes(Artisan artisan) {
        Set<String> skills = artisan.getSkills();
        if (skills != null) {
            for (String skill : skills) {
                // Update trie
                insertIntoTrie(skill, artisan.getId());
                
                // Update direct mapping
                skillToArtisanMap.computeIfAbsent(skill, k -> ConcurrentHashMap.newKeySet())
                                .add(artisan.getId());
            }
        }
    }
    
    private void insertIntoTrie(String skill, String artisanId) {
        TrieNode current = skillTrieRoot;
        
        for (char ch : skill.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
            current.artisanIds.add(artisanId);
        }
        
        current.isEndOfWord = true;
    }
    
    private Set<String> searchTrie(String prefix) {
        TrieNode current = skillTrieRoot;
        
        for (char ch : prefix.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return Collections.emptySet();
            }
            current = current.children.get(ch);
        }
        
        return current.artisanIds;
    }

    @Override
    @Transactional
    public Set<String> addSkills(String artisanId, Set<String> skills) {
        Artisan artisan = artisanRepository.findById(artisanId)
            .orElseThrow(() -> new IllegalArgumentException("Artisan not found with id: " + artisanId));
        
        Set<String> currentSkills = artisan.getSkills();
        if (currentSkills == null) {
            currentSkills = new HashSet<>();
        }
        
        // Add new skills
        for (String skill : skills) {
            if (currentSkills.add(skill)) {
                // Update trie
                insertIntoTrie(skill, artisanId);
                
                // Update direct mapping
                skillToArtisanMap.computeIfAbsent(skill, k -> ConcurrentHashMap.newKeySet())
                                .add(artisanId);
            }
        }
        
        artisan.setSkills(currentSkills);
        artisanRepository.save(artisan);
        
        return currentSkills;
    }

    @Override
    @Transactional
    public Set<String> removeSkills(String artisanId, Set<String> skills) {
        Artisan artisan = artisanRepository.findById(artisanId)
            .orElseThrow(() -> new IllegalArgumentException("Artisan not found with id: " + artisanId));
        
        Set<String> currentSkills = artisan.getSkills();
        if (currentSkills != null) {
            // Remove skills
            for (String skill : skills) {
                if (currentSkills.remove(skill)) {
                    // Update direct mapping
                    Set<String> artisanIds = skillToArtisanMap.get(skill);
                    if (artisanIds != null) {
                        artisanIds.remove(artisanId);
                        if (artisanIds.isEmpty()) {
                            skillToArtisanMap.remove(skill);
                        }
                    }
                    
                    // Note: We don't remove from trie as it's complex and would require rebuilding
                    // In a real implementation, we would need a more sophisticated approach
                }
            }
            
            artisan.setSkills(currentSkills);
            artisanRepository.save(artisan);
        }
        
        return currentSkills;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getSkills(String artisanId) {
        return artisanRepository.findById(artisanId)
            .map(Artisan::getSkills)
            .orElse(Collections.emptySet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findArtisansBySkill(String skill) {
        // Use direct mapping for exact match
        Set<String> artisanIds = skillToArtisanMap.getOrDefault(skill, Collections.emptySet());
        return new ArrayList<>(artisanIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findArtisansBySkills(Set<String> skills) {
        // Use direct mapping for multiple skills
        Set<String> result = new HashSet<>();
        boolean isFirst = true;
        
        for (String skill : skills) {
            Set<String> artisanIds = skillToArtisanMap.getOrDefault(skill, Collections.emptySet());
            if (isFirst) {
                result.addAll(artisanIds);
                isFirst = false;
            } else {
                result.retainAll(artisanIds);
            }
        }
        
        return new ArrayList<>(result);
    }
    
    /**
     * Find artisans by skill prefix (e.g., "prog" would match "programming", "progressive", etc.)
     * @param prefix the prefix to search for
     * @return list of artisan IDs with skills matching the prefix
     */
    public List<String> findArtisansBySkillPrefix(String prefix) {
        Set<String> artisanIds = searchTrie(prefix);
        return new ArrayList<>(artisanIds);
    }
    
    /**
     * Get all unique skills in the system
     * @return set of all skills
     */
    public Set<String> getAllSkills() {
        return new HashSet<>(skillToArtisanMap.keySet());
    }
} 