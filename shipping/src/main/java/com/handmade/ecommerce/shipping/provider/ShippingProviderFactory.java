package com.handmade.ecommerce.shipping.provider;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory class for managing shipping providers
 */
@Component
public class ShippingProviderFactory {

    private final Map<String, ShippingProvider> providers;

    public ShippingProviderFactory(List<ShippingProvider> providerList) {
        // Convert the list of providers into a map with provider name as the key
        this.providers = providerList.stream()
                .collect(Collectors.toMap(
                        ShippingProvider::getProviderName,
                        Function.identity()
                ));
    }

    /**
     * Get a shipping provider by name
     *
     * @param providerName the name of the provider (e.g., "FedEx", "UPS")
     * @return the shipping provider
     * @throws IllegalArgumentException if the provider is not found
     */
    public ShippingProvider getProvider(String providerName) {
        ShippingProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Shipping provider not found: " + providerName);
        }
        return provider;
    }

    /**
     * Get all available shipping providers
     *
     * @return list of all shipping providers
     */
    public List<ShippingProvider> getAllProviders() {
        return List.copyOf(providers.values());
    }

    /**
     * Check if a provider exists
     *
     * @param providerName the name of the provider
     * @return true if the provider exists, false otherwise
     */
    public boolean hasProvider(String providerName) {
        return providers.containsKey(providerName);
    }
} 