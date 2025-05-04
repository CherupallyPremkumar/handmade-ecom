package com.handmade.ecommerce.artisan.service.impl;

import com.handmade.ecommerce.artisan.configuration.dao.ArtisanBankAccountRepository;
import com.handmade.ecommerce.artisan.configuration.dao.ArtisanRepository;
import com.handmade.ecommerce.artisan.model.Artisan;
import com.handmade.ecommerce.artisan.model.ArtisanBankAccount;
import com.handmade.ecommerce.artisan.model.service.ArtisanBankAccountService;
import com.handmade.ecommerce.artisan.model.service.ArtisanWorkingHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

@Service
public class ArtisanBankAccountServiceImpl implements ArtisanBankAccountService {

    private final ArtisanBankAccountRepository bankAccountRepository;
    private final ArtisanRepository artisanRepository;

    private ArtisanWorkingHoursService artisanWorkingHoursService;
    
    // Cache for artisan bank accounts
    private final Map<String, List<ArtisanBankAccount>> artisanAccountsCache = new ConcurrentHashMap<>();
    
    // Priority queue for default accounts (sorted by verification date, most recent first)
    private final Map<String, PriorityBlockingQueue<ArtisanBankAccount>> defaultAccountsQueue = new ConcurrentHashMap<>();
    
    @Autowired
    public ArtisanBankAccountServiceImpl(ArtisanBankAccountRepository bankAccountRepository,
                                       ArtisanRepository artisanRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.artisanRepository = artisanRepository;
    }
    
    @PostConstruct
    public void initialize() {
        // Load all bank accounts and build the cache
        List<ArtisanBankAccount> allAccounts = bankAccountRepository.findAll();
        
        // Group by artisan ID
        Map<String, List<ArtisanBankAccount>> accountsByArtisan = allAccounts.stream()
            .collect(Collectors.groupingBy(account -> account.getArtisan().getId()));
        
        // Initialize cache and priority queues
        accountsByArtisan.forEach((artisanId, accounts) -> {
            artisanAccountsCache.put(artisanId, accounts);
            
            // Create priority queue for default accounts
            PriorityBlockingQueue<ArtisanBankAccount> queue = new PriorityBlockingQueue<>(
                accounts.size(),
                Comparator.comparing(ArtisanBankAccount::isDefault).reversed()
                    .thenComparing(account -> account.getVerificationDate() != null ? 
                        account.getVerificationDate() : LocalDateTime.MIN)
                    .reversed()
            );
            queue.addAll(accounts);
            defaultAccountsQueue.put(artisanId, queue);
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "artisanBankAccounts", key = "#artisanId")
    public ArtisanBankAccount addBankAccount(String artisanId, ArtisanBankAccount bankAccount) {
        Artisan artisan = artisanRepository.findById(artisanId)
            .orElseThrow(() -> new IllegalArgumentException("Artisan not found with id: " + artisanId));
        
        bankAccount.setArtisan(artisan);
        bankAccount.setVerificationStatus("PENDING");
        
        ArtisanBankAccount savedAccount = bankAccountRepository.save(bankAccount);
        
        // Update cache
        List<ArtisanBankAccount> accounts = artisanAccountsCache.computeIfAbsent(
            artisanId, k -> new ArrayList<>());
        accounts.add(savedAccount);
        
        // Update priority queue
        PriorityBlockingQueue<ArtisanBankAccount> queue = defaultAccountsQueue.computeIfAbsent(
            artisanId, k -> new PriorityBlockingQueue<>(
                10, // Initial capacity
                Comparator.comparing(ArtisanBankAccount::isDefault).reversed()
                    .thenComparing(account -> account.getVerificationDate() != null ? 
                        account.getVerificationDate() : LocalDateTime.MIN)
                    .reversed()
            ));
        queue.offer(savedAccount);
        
        if (savedAccount.isDefault()) {
            // Set all other accounts as non-default
            accounts.stream()
                .filter(account -> !account.getId().equals(savedAccount.getId()))
                .forEach(account -> {
                    account.setDefault(false);
                    bankAccountRepository.save(account);
                });
            
            // Rebuild priority queue
            rebuildPriorityQueue(artisanId);
        }
        
        return savedAccount;
    }

    @Override
    @Transactional
    @CacheEvict(value = "artisanBankAccounts", key = "#artisanId")
    public ArtisanBankAccount updateBankAccount(String artisanId, String bankAccountId, ArtisanBankAccount bankAccount) {
        ArtisanBankAccount existingAccount = bankAccountRepository.findById(bankAccountId)
            .orElseThrow(() -> new IllegalArgumentException("Bank account not found with id: " + bankAccountId));
        
        if (!existingAccount.getArtisan().getId().equals(artisanId)) {
            throw new IllegalArgumentException("Bank account does not belong to the specified artisan");
        }
        
        existingAccount.setAccountHolderName(bankAccount.getAccountHolderName());
        existingAccount.setAccountNumber(bankAccount.getAccountNumber());
        existingAccount.setBankName(bankAccount.getBankName());
        existingAccount.setBranchCode(bankAccount.getBranchCode());
        
        ArtisanBankAccount updatedAccount = bankAccountRepository.save(existingAccount);
        
        // Update cache
        List<ArtisanBankAccount> accounts = artisanAccountsCache.get(artisanId);
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getId().equals(bankAccountId)) {
                    accounts.set(i, updatedAccount);
                    break;
                }
            }
        }
        
        if (bankAccount.isDefault()) {
            // Set all other accounts as non-default
            List<ArtisanBankAccount> allAccounts = artisanAccountsCache.get(artisanId);
            if (allAccounts != null) {
                allAccounts.stream()
                    .filter(account -> !account.getId().equals(bankAccountId))
                    .forEach(account -> {
                        account.setDefault(false);
                        bankAccountRepository.save(account);
                    });
            }
            
            // Rebuild priority queue
            rebuildPriorityQueue(artisanId);
        }
        
        existingAccount.setDefault(bankAccount.isDefault());
        return bankAccountRepository.save(existingAccount);
    }

    @Override
    @Transactional
    @CacheEvict(value = "artisanBankAccounts", key = "#artisanId")
    public void removeBankAccount(String artisanId, String bankAccountId) {
        ArtisanBankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
            .orElseThrow(() -> new IllegalArgumentException("Bank account not found with id: " + bankAccountId));
        
        if (!bankAccount.getArtisan().getId().equals(artisanId)) {
            throw new IllegalArgumentException("Bank account does not belong to the specified artisan");
        }
        
        bankAccountRepository.delete(bankAccount);
        
        // Update cache
        List<ArtisanBankAccount> accounts = artisanAccountsCache.get(artisanId);
        if (accounts != null) {
            accounts.removeIf(account -> account.getId().equals(bankAccountId));
        }
        
        // Rebuild priority queue
        rebuildPriorityQueue(artisanId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "artisanBankAccounts", key = "#artisanId + '_' + #bankAccountId")
    public Optional<ArtisanBankAccount> getBankAccount(String artisanId, String bankAccountId) {
        return bankAccountRepository.findById(bankAccountId)
            .filter(account -> account.getArtisan().getId().equals(artisanId));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "artisanBankAccounts", key = "#artisanId")
    public List<ArtisanBankAccount> getBankAccounts(String artisanId) {
        // Try to get from cache first
        List<ArtisanBankAccount> cachedAccounts = artisanAccountsCache.get(artisanId);
        if (cachedAccounts != null) {
            return new ArrayList<>(cachedAccounts);
        }
        
        // If not in cache, fetch from repository
        List<ArtisanBankAccount> accounts = bankAccountRepository.findByArtisanId(artisanId);
        
        // Update cache
        artisanAccountsCache.put(artisanId, accounts);
        
        return accounts;
    }

    @Override
    @Transactional
    @CacheEvict(value = "artisanBankAccounts", key = "#artisanId")
    public ArtisanBankAccount setDefaultBankAccount(String artisanId, String bankAccountId) {
        ArtisanBankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
            .orElseThrow(() -> new IllegalArgumentException("Bank account not found with id: " + bankAccountId));
        
        if (!bankAccount.getArtisan().getId().equals(artisanId)) {
            throw new IllegalArgumentException("Bank account does not belong to the specified artisan");
        }
        
        // Set all accounts as non-default
        List<ArtisanBankAccount> accounts = artisanAccountsCache.get(artisanId);
        if (accounts != null) {
            accounts.forEach(account -> {
                account.setDefault(false);
                bankAccountRepository.save(account);
            });
        }
        
        // Set the specified account as default
        bankAccount.setDefault(true);
        ArtisanBankAccount updatedAccount = bankAccountRepository.save(bankAccount);
        
        // Update cache
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getId().equals(bankAccountId)) {
                    accounts.set(i, updatedAccount);
                    break;
                }
            }
        }
        
        // Rebuild priority queue
        rebuildPriorityQueue(artisanId);
        
        return updatedAccount;
    }

    @Override
    @Transactional
    @CacheEvict(value = "artisanBankAccounts", key = "#artisanId")
    public ArtisanBankAccount verifyBankAccount(String artisanId, String bankAccountId, String verifiedBy) {
        ArtisanBankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
            .orElseThrow(() -> new IllegalArgumentException("Bank account not found with id: " + bankAccountId));
        
        if (!bankAccount.getArtisan().getId().equals(artisanId)) {
            throw new IllegalArgumentException("Bank account does not belong to the specified artisan");
        }
        
        bankAccount.setVerificationStatus("VERIFIED");
        bankAccount.setVerificationDate(LocalDateTime.now());
        bankAccount.setVerifiedBy(verifiedBy);
        
        ArtisanBankAccount updatedAccount = bankAccountRepository.save(bankAccount);
        
        // Update cache
        List<ArtisanBankAccount> accounts = artisanAccountsCache.get(artisanId);
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getId().equals(bankAccountId)) {
                    accounts.set(i, updatedAccount);
                    break;
                }
            }
        }
        
        // Rebuild priority queue
        rebuildPriorityQueue(artisanId);
        
        return updatedAccount;
    }
    
    /**
     * Get the default bank account for an artisan
     * @param artisanId the ID of the artisan
     * @return Optional containing the default bank account if found
     */
    public Optional<ArtisanBankAccount> getDefaultBankAccount(String artisanId) {
        PriorityBlockingQueue<ArtisanBankAccount> queue = defaultAccountsQueue.get(artisanId);
        if (queue != null && !queue.isEmpty()) {
            return Optional.of(queue.peek());
        }
        
        // If not in queue, try to find in repository
        List<ArtisanBankAccount> accounts = bankAccountRepository.findByArtisanIdAndIsDefaultTrue(artisanId);
        if (!accounts.isEmpty()) {
            return Optional.of(accounts.get(0));
        }
        
        return Optional.empty();
    }
    
    /**
     * Rebuild the priority queue for an artisan
     * @param artisanId the ID of the artisan
     */
    private void rebuildPriorityQueue(String artisanId) {
        List<ArtisanBankAccount> accounts = artisanAccountsCache.get(artisanId);
        if (accounts != null) {
            PriorityBlockingQueue<ArtisanBankAccount> queue = new PriorityBlockingQueue<>(
                    accounts.size(),
                    Comparator.comparing(ArtisanBankAccount::isDefault).reversed()
                            .thenComparing(
                                    account -> account.getVerificationDate() != null
                                            ? account.getVerificationDate()
                                            : LocalDateTime.MIN,
                                    Comparator.reverseOrder()
                            )
            );
            
            accounts.forEach(queue::offer);
            defaultAccountsQueue.put(artisanId, queue);
        }
    }
} 