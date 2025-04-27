# Shipping Provider Implementation

This document provides an overview of the shipping provider implementation in the Handmade E-commerce platform.

## Overview

The shipping module provides a flexible and extensible way to integrate with multiple shipping carriers (like FedEx, UPS, etc.) through a common interface. This allows the system to:

- Create and manage shipments
- Calculate shipping rates
- Track shipments
- Schedule pickups
- Cancel shipments

## Architecture

The shipping provider implementation follows a factory pattern with the following components:

### Core Components

1. **ShippingProvider Interface**

   - Defines the contract that all shipping providers must implement
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/provider/ShippingProvider.java`

2. **ShippingRate Class**

   - Represents shipping rates offered by providers
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/provider/ShippingRate.java`

3. **ShippingProviderFactory**
   - Manages available shipping providers
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/provider/ShippingProviderFactory.java`

### Provider Implementations

1. **FedExShippingProvider**

   - Implementation for FedEx shipping services
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/provider/impl/FedExShippingProvider.java`

2. **UPSShippingProvider**
   - Implementation for UPS shipping services
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/provider/impl/UPSShippingProvider.java`

### Service Layer

1. **ShipmentService Interface**

   - Defines the business operations for shipment management
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/service/ShipmentService.java`

2. **ShipmentServiceImpl**
   - Implements the ShipmentService interface
   - Located in: `shipping/src/main/java/com/handmade/ecommerce/shipping/service/impl/ShipmentServiceImpl.java`

## Key Features

### ShippingProvider Interface

The `ShippingProvider` interface defines the following operations:

- `getProviderName()`: Returns the name of the shipping provider
- `createShipment(Shipment)`: Creates a new shipment
- `updateShipmentStatus(String)`: Updates the status of a shipment
- `calculateRates(Shipment)`: Calculates shipping rates for a shipment
- `schedulePickup(Shipment, LocalDateTime)`: Schedules a pickup for a shipment
- `cancelShipment(String)`: Cancels a shipment

### ShippingRate Class

The `ShippingRate` class contains the following information:

- `provider`: The shipping provider offering the rate
- `serviceLevel`: The service level (e.g., "Ground", "Express")
- `cost`: The shipping cost
- `currency`: The currency of the cost
- `estimatedDeliveryDate`: The estimated delivery date
- `minDeliveryDays`: The minimum delivery time in days
- `maxDeliveryDays`: The maximum delivery time in days
- `guaranteed`: Whether the rate is guaranteed
- `features`: Additional features or benefits of the rate

### ShippingProviderFactory

The `ShippingProviderFactory` class:

- Manages available shipping providers
- Provides methods to get providers by name
- Allows checking if a provider exists
- Returns a list of all available providers

## Configuration

Each shipping provider implementation requires the following configuration properties:

### FedEx Configuration

```properties
shipping.fedex.api-key=your-api-key
shipping.fedex.api-secret=your-api-secret
shipping.fedex.account-number=your-account-number
shipping.fedex.api-url=https://api.fedex.com
```

### UPS Configuration

```properties
shipping.ups.api-key=your-api-key
shipping.ups.api-secret=your-api-secret
shipping.ups.account-number=your-account-number
shipping.ups.api-url=https://api.ups.com
```

## Usage Examples

### Creating a Shipment

```java
// Get the shipping provider
ShippingProvider provider = shippingProviderFactory.getProvider("FedEx");

// Create a shipment
Shipment shipment = new Shipment();
// Set shipment properties
shipment.setOrderId("ORDER123");
shipment.setCustomerId("CUST456");
// ... set other properties

// Create the shipment
Shipment createdShipment = provider.createShipment(shipment);
```

### Calculating Shipping Rates

```java
// Get the shipping provider
ShippingProvider provider = shippingProviderFactory.getProvider("UPS");

// Create a shipment for rate calculation
Shipment shipment = new Shipment();
// Set shipment properties
shipment.setOrderId("ORDER123");
shipment.setWeight(new BigDecimal("2.5"));
shipment.setWeightUnit("KG");
// ... set other properties

// Calculate rates
List<ShippingRate> rates = provider.calculateRates(shipment);

// Process the rates
for (ShippingRate rate : rates) {
    System.out.println("Service: " + rate.getServiceLevel());
    System.out.println("Cost: " + rate.getCost() + " " + rate.getCurrency());
    System.out.println("Estimated Delivery: " + rate.getEstimatedDeliveryDate());
}
```

### Updating Shipment Status

```java
// Get the shipping provider
ShippingProvider provider = shippingProviderFactory.getProvider("FedEx");

// Update the status
Shipment updatedShipment = provider.updateShipmentStatus("TRACKING123");
System.out.println("New Status: " + updatedShipment.getStatus());
```

## Extending with New Providers

To add a new shipping provider:

1. Create a new class that implements the `ShippingProvider` interface
2. Implement all required methods
3. Add the necessary configuration properties
4. The provider will be automatically registered by the `ShippingProviderFactory`

Example:

```java
@Service
public class DHLShippingProvider implements ShippingProvider {

    @Value("${shipping.dhl.api-key}")
    private String apiKey;

    // ... other properties

    @Override
    public String getProviderName() {
        return "DHL";
    }

    // Implement other methods
}
```

## Error Handling

The shipping provider implementations include error handling for:

- API communication errors
- Invalid input data
- Provider-specific errors

Each provider logs errors using SLF4J and throws appropriate exceptions when operations fail.

## Future Enhancements

Potential future enhancements to the shipping provider implementation:

1. Add support for more shipping carriers
2. Implement rate caching to reduce API calls
3. Add support for international shipping
4. Implement batch operations for multiple shipments
5. Add support for shipping labels generation
6. Implement webhook notifications for shipment status updates
