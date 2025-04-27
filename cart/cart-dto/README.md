# Cart DTO Module

This module contains Data Transfer Objects (DTOs) for the Cart service. DTOs are used to transfer data between different layers of the application and between different services.

## Purpose

The DTO module serves several purposes:

1. **API Contract**: Defines the contract for API requests and responses
2. **Data Validation**: Provides validation constraints for incoming data
3. **Documentation**: Includes Swagger/OpenAPI annotations for API documentation
4. **Decoupling**: Decouples the internal entity model from the external API

## Structure

The module is structured as follows:

- `BaseDTO`: Abstract base class with common fields for all DTOs
- `CartDTO`: Main DTO for cart data
- `CartItemDTO`: DTO for cart item data
- `AddItemRequestDTO`: DTO for adding an item to a cart
- `UpdateItemRequestDTO`: DTO for updating a cart item
- `CartListResponseDTO`: DTO for returning a list of carts
- `mapper`: Package containing mappers to convert between DTOs and entities

## Usage

### In Controllers

```java
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartDTO> addItem(
            @PathVariable String cartId,
            @RequestBody AddItemRequestDTO request) {
        CartItem cartItem = cartMapper.toEntity(request, cartId, getCurrentTenantId());
        Cart updatedCart = cartService.addItem(cartId, cartItem);
        return ResponseEntity.ok(cartMapper.toDTO(updatedCart));
    }

    // Other controller methods...
}
```

### In Other Services

When other services need to interact with the Cart service, they can use these DTOs to ensure compatibility.

## Best Practices

1. **Keep DTOs Simple**: DTOs should only contain the data needed for the specific operation
2. **Use Validation**: Apply appropriate validation constraints to ensure data integrity
3. **Document Well**: Use Swagger/OpenAPI annotations to document the API
4. **Version Carefully**: When making changes to DTOs, consider backward compatibility

## Integration with Other Modules

This module can be used as a dependency by other modules that need to interact with the Cart service. For example:

```xml
<dependency>
    <groupId>com.handmade.ecommerce</groupId>
    <artifactId>cart-dto</artifactId>
    <version>${project.version}</version>
</dependency>
```
