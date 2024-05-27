# image-server-api
API for managing your application images

# Run with docker server
1. Create a network
```
docker network image_server_api_network
```
2. Run MySQL Server
```
docker run -itd --rm -p 3307:3306 --network image_server_api_network --name mysql_server -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=image_server_api_db mysql:8.0.32
```
3. Run Image Server API
```
docker run -itd --rm -p 8085:8085 --network sma-image_server_api_network --name image_server_api_app image-server-api
```

# Useful Links
[Connection Refused](https://bobcares.com/blog/could-not-connect-to-redis-connection-refused/)

# Access API endpoints easily
[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/26932885-1cd0dd88-73eb-4ed2-9541-dc73d20cdc6e?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D26932885-1cd0dd88-73eb-4ed2-9541-dc73d20cdc6e%26entityType%3Dcollection%26workspaceId%3D2544d7eb-bd86-4b5d-8b99-4b942f493c1f)