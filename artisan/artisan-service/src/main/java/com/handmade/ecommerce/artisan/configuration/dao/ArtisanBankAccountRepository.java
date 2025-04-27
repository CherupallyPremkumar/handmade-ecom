package com.handmade.ecommerce.artisan.configuration.dao;

import com.handmade.ecommerce.artisan.model.ArtisanBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtisanBankAccountRepository extends JpaRepository<ArtisanBankAccount, String> {
    
    /**
     * Find all bank accounts for a specific artisan
     * @param artisanId the ID of the artisan
     * @return list of bank accounts
     */
    @Query("SELECT a FROM ArtisanBankAccount a WHERE a.artisan.id = :artisanId")
    List<ArtisanBankAccount> findByArtisanId(@Param("artisanId") String artisanId);
    
    /**
     * Find all default bank accounts for a specific artisan
     * @param artisanId the ID of the artisan
     * @return list of default bank accounts
     */
    @Query("SELECT a FROM ArtisanBankAccount a WHERE a.artisan.id = :artisanId AND a.isDefault = true")
    List<ArtisanBankAccount> findByArtisanIdAndIsDefaultTrue(@Param("artisanId") String artisanId);
}
