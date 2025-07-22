# file-server-api

File Server API written in Java

# Run with docker

## dev
1. Run the local project
    - Supply the right environment variable in deployment/dev/.env first!
    - Add it to IDE environment variables
   - Make sure uploadPath rwx------ permissions
   - Run the project

## prod
1. Run the project
    - Supply the right environment variable in deployment/prod/.env first!
   - Make sure uploadPath rwx------ permissions
    - Run these command
```
docker compose up -d
```

# Access API endpoints easily

1. [Using swagger](http://localhost:8085/api/v2/fsa/swagger-ui/index.html)

2. [Using Postman](https://www.postman.com/voltesiv/workspace/file-server-api/collection/26932885-6a94afb7-fb7c-460c-bb45-6a372f7a763d?action=share&creator=26932885&active-environment=26932885-8ec50e78-6db0-416e-9f27-24a50886824f)