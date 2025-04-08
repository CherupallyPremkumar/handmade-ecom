#curl -X POST "http://localhost:8180/realms/sarees/protocol/openid-connect/token" \
#  -H "Content-Type: application/x-www-form-urlencoded" \
#  -d "client_id=authz-servlet" \
#  -d "client_secret=secret" \
#  -d "grant_type=password" \
#  -d "username=artisan1" \
#  -d "password=password"


curl -X POST http://localhost:8098/product \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJKVjJ0RHhrZGJkYVVIY3poUXcxRWVUdDhDMFp4VXlnVjg0ZjZJQnJQMTR3In0.eyJleHAiOjE3NDQxMDQ0OTEsImlhdCI6MTc0NDEwNDE5MSwianRpIjoiNWQ2NmQ3NGYtNGMyMy00NDE2LTgxYjctZWM1YWZjZGNmMzk4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9zYXJlZXMiLCJzdWIiOiJmYmNkZGRlYi05NTllLTQxNzMtODZkMC02OTVjNTJkMzVmODEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhdXRoei1zZXJ2bGV0Iiwic2Vzc2lvbl9zdGF0ZSI6IjFhNjY5MTVhLTU4Y2YtNDZiOC05MjVhLThhY2UxNTExMjhkYiIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInRlc3QucHJlbWl1bSJdfSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiMWE2NjkxNWEtNThjZi00NmI4LTkyNWEtOGFjZTE1MTEyOGRiIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6ImFydGlzYW4xIn0.CGe7mw_wXuzymPCZ4_GknowNkKn0kMXXJDEORF0OeO70b682FdGXrolf71G7HwKZwF3GOtc0q0w2do9FxrU-a_PxQACIIQgf8crLStdgpeZsjsQqbi7aT7xrcaHQ4rk38v5hpkzPUVxBaPuXwa3p8lFNEpBXb5ePrxyY7x6WPE-XL_GcOJdKy8Pep9WGJV0iPootwarca_wP5xY-E7-3b3VNMpaW6i-3pDS5Ix-hz3wr6O_ztjflkWvggHSe86uyEfGHfHzXzzRSFK0Bmn7lvMDZFBdGBtN7-rPuLRQGmzn0Th15ucw9KpwNMceGZ0MVUOifyl268UZXAHTO7ExJZQ" \
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