#curl -X POST "http://localhost:8180/realms/sarees/protocol/openid-connect/token" \
#  -H "Content-Type: application/x-www-form-urlencoded" \
#  -d "client_id=authz-servlet" \
#  -d "client_secret=secret" \
#  -d "grant_type=password" \
#  -d "username=artisan1" \
#  -d "password=password"


curl -X POST http://localhost:8099/product \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOSHNaS0tOa2ZIYjAwVGxqY04wNDA4bDhBSlp5clVJcXZUSDl5c2plVXo4In0.eyJleHAiOjE3NDM3ODMzNzgsImlhdCI6MTc0Mzc4MzA3OCwianRpIjoiMDNkOWM4YzktMzZmNi00MGIyLWEwNDgtOGI0ZGJhNmY2ZGZmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy9zYXJlZXMiLCJzdWIiOiJiN2I3ZjNkNy02NjAzLTQxNDgtOTU5MC00ODI2NmY1ODIyMzgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhdXRoei1zZXJ2bGV0Iiwic2Vzc2lvbl9zdGF0ZSI6ImZjYjcwOGFmLTY5MjYtNDJjYi04NTE5LTkwZWVkNzU2NjkwNSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInRlc3QucHJlbWl1bSJdfSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiZmNiNzA4YWYtNjkyNi00MmNiLTg1MTktOTBlZWQ3NTY2OTA1IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6ImFydGlzYW4xIn0.PQsM94s87ckIFjn5hQRBPRvNR8SpiR-COFTpzPYYqN72uTatvw9dsG_AQF6e0ECZZTwAeUjv1QQ4WLz1Vo98KPeiQO_IxhRogkXFN7Fs8fvbiVQCrEkl06sep61c4a2Z7KH2A0VwSnECXgyFOGN1OzAI_HYHThC_yzKFIi9rYJgmUikcCEbp_whmh0_Aa0QeWTTlLtgNJkPA8e6QUEnOWjztmwjtP6mO-lUj8Orl_Jb_FJisdk9S-xhaI-q1cexgQJ5tQ7EkH7BgXW3rWMFWbUVOKwK3GfiO9bPAHjGxuEzHLNqrvXT7R_aMQtyrwFFuUICwdzfLJ2JBZWX3l5bYVQ","expires_in":300,"refresh_expires_in":1800,"refresh_token":"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIxMGI3YzFkNi0xZmM0LTQxOGEtYTQ2My0wNjA3MzYzZjU1NzYifQ.eyJleHAiOjE3NDM3ODQ4NzgsImlhdCI6MTc0Mzc4MzA3OCwianRpIjoiZWY5M2RkY2EtMzQ2Ny00Nzc5LWEwNjgtNDIwMjdmYTE3NmEzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL3JlYWxtcy9zYXJlZXMiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvcmVhbG1zL3NhcmVlcyIsInN1YiI6ImI3YjdmM2Q3LTY2MDMtNDE0OC05NTkwLTQ4MjY2ZjU4MjIzOCIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJhdXRoei1zZXJ2bGV0Iiwic2Vzc2lvbl9zdGF0ZSI6ImZjYjcwOGFmLTY5MjYtNDJjYi04NTE5LTkwZWVkNzU2NjkwNSIsInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6ImZjYjcwOGFmLTY5MjYtNDJjYi04NTE5LTkwZWVkNzU2NjkwNSJ9.o9k64lH2eKTY2MQ2YRuRRourQ0Qq_kJ2KbW5xs8_kY8" \
  -H "Content-Type: application/json" \
   -H "x-chenile-tenant-id: sarees" \
  -d '{
        "id": "prd-12345",
        "name": "Handwoven Silk Saree",
        "description": "A beautifully handwoven silk saree with traditional motifs.",
        "price": 6499.99,
        "stockQuantity": 15,
        "material": "Silk",
        "origin": "Kanchipuram",
        "color": "Crimson Red",
        "artisanId": "art-001",
        "categoryId": "bridal"
      }'