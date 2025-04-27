package com.handmade.ecommerce.artisan.service;

import com.handmade.ecommerce.artisan.model.ArtisanBankAccount;
import java.util.List;
import java.util.Optional;

public interface ArtisanBankAccountService {
    
    /**
     * Add a bank account for an artisan
     * @param artisanId the ID of the artisan
     * @param bankAccount the bank account details
     * @return the saved bank account
     */
    ArtisanBankAccount addBankAccount(String artisanId, ArtisanBankAccount bankAccount);
    
    /**
     * Update a bank account
     * @param artisanId the ID of the artisan
     * @param bankAccountId the ID of the bank account
     * @param bankAccount the updated bank account details
     * @return the updated bank account
     */
    ArtisanBankAccount updateBankAccount(String artisanId, String bankAccountId, ArtisanBankAccount bankAccount);
    
    /**
     * Remove a bank account
     * @param artisanId the ID of the artisan
     * @param bankAccountId the ID of the bank account
     */
    void removeBankAccount(String artisanId, String bankAccountId);
    
    /**
     * Get a bank account by ID
     * @param artisanId the ID of the artisan
     * @param bankAccountId the ID of the bank account
     * @return Optional containing the bank account if found
     */
    Optional<ArtisanBankAccount> getBankAccount(String artisanId, String bankAccountId);
    
    /**
     * Get all bank accounts for an artisan
     * @param artisanId the ID of the artisan
     * @return list of bank accounts
     */
    List<ArtisanBankAccount> getBankAccounts(String artisanId);
    
    /**
     * Set a bank account as default
     * @param artisanId the ID of the artisan
     * @param bankAccountId the ID of the bank account
     * @return the updated bank account
     */
    ArtisanBankAccount setDefaultBankAccount(String artisanId, String bankAccountId);
    
    /**
     * Verify a bank account
     * @param artisanId the ID of the artisan
     * @param bankAccountId the ID of the bank account
     * @param verifiedBy the ID of the user performing the verification
     * @return the verified bank account
     */
    ArtisanBankAccount verifyBankAccount(String artisanId, String bankAccountId, String verifiedBy);
} 