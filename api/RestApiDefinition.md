# REST API Definition
---
---
## Authentication
---

### /auth/login
    Request
        Method: POST
        Headers: 
        Parameters:
        Body: form-data
            - username
            - password

    Response:
        Headers: x-auth-token
        Content-Type: json
        Body: User
        Status: 200 OK
            - 401 unauthorized
### /auth/logout
    Request
        Method: GET
        Headers: x-auth-token
        Parameters:
        Body: 

    Response:
        Headers: x-auth-token (new session)
        Content-Type:
        Body: 
        Status: 401 unauthorized
        
### /register
    Request
        Method: POST
        Headers: content-type
        Parameters:
        Body: User

    Response:
        Headers:
        Content-Type: json
        Body: User
        Status: 200 OK
            - 409 Conflict
            - 406 Not Acceptable

---
---
## UserManagement
---
### /api/user/get/{id}
    Request
        Method: GET
        Headers: x-auth-token
        Parameters:
        Body: 

    Response:
        Headers:
        Content-Type: json
        Body: User
        Status: 200 OK

### /api/user/getAll
    Request
        Method: GET
        Headers: x-auth-token
        Parameters:
        Body: 

    Response:
        Headers:
        Content-Type: json
        Body: List<User>
        Status: 200 OK

