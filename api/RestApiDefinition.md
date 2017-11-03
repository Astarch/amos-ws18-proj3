# REST API Definition

### /login
    Method: POST
    Parameters
        - username
        - password
    Request Payload
        - none
    Response Payload:
        - user (full json)
        - x-auth-token
        
### /login
    Method: DELETE
    Parameters
        - none
    Request Payload
        - x-auth-token
    Response Payload:
        - none
    
### /user
    Method: GET
    Parameters
        - id
    Request Payload
        - none
    Response Payload:
        - user (full json)
        - x-auth-token

### /users
    Method: GET
    Parameters
        - limit
        - offset
        - searchterm
    Request Payload
        - x-auth-token
    Response Payload:
        - List of users

### /register
    Method: POST
    Parameters
        - none
    Request Payload
        - user (full json except ID)
        - x-auth-token
    Response Payload:
        - user (full json with ID)
