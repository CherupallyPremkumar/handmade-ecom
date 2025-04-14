## Multi-Tenancy in Product API

This application supports **multi-tenant architecture**, where each client (tenant) has logically isolated data. All domain entities (e.g., `Product`, `Order`) include a `tenant` field that references the `hm_tenant` table via a foreign key.

---

###  Tenant Identification

To associate data with a specific tenant, every API request must include the **Tenant ID** in the request headers.

### Header Format

```http
X-Tenant-ID: Tenant-a297e5a0-1d14-4a08-8160-ac0677df8f5a-0001
```
### Example: Creating a Product

```json
curl -X POST "http://localhost:8098/products" \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: Tenant-a297e5a0-1d14-4a08-8160-ac0677df8f5a-0001" \
  -d '{
    "name": "Handwoven Silk Saree",
    "description": "Rich handwoven silk saree with traditional motifs",
    "price": 6499.99,
    "stockQuantity": 10,
    "material": "Silk",
    "origin": "Kanchipuram",
    "artisanId": "ar-001",
    "categoryId": "brdal",
    "color": "Red and Gold"
  }'
```

### Backend Behavior

 	•The tenantId is internally assigned to all entities before saving.
	•This ensures all records are correctly isolated under the corresponding tenant.