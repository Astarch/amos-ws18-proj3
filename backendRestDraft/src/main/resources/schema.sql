CREATE TABLE
    USER
    (
        id UUID primary key,
        username VARCHAR(255),
        password VARCHAR(255),
        firstname VARCHAR(255),
        lastname VARCHAR(255),
        email VARCHAR(255),
        address VARCHAR(255),
        phone VARCHAR(255)
    );
CREATE TABLE
    ROLE
    (
        id UUID PRIMARY KEY,
        role VARCHAR(255)
    );
CREATE TABLE
    USER_ROLES
    (
        user_id UUID REFERENCES USER(id),
        role_id UUID REFERENCES role(id),
        PRIMARY KEY(user_id, role_id)  
    );