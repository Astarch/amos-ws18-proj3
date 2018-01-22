CREATE TABLE
    ACCOUNT
    (
        id UUID PRIMARY KEY,
        username VARCHAR(255),
        password VARCHAR(255),
        firstname VARCHAR(255),
        lastname VARCHAR(255),
        email VARCHAR(255),
        address VARCHAR(255),
        phone VARCHAR(255),
        UNIQUE(username),
        unique(email)
    );
CREATE TABLE
    ROLE
    (
        id UUID PRIMARY KEY,
        role VARCHAR(255) UNIQUE
    );
CREATE TABLE
    USER_ROLE
    (
        userid UUID REFERENCES ACCOUNT(id),
        roleid UUID REFERENCES ROLE(id),
        PRIMARY KEY(userid, roleid)
    );
CREATE TABLE
    GDELTSCVCACHE
    (
        zipurl VARCHAR(255) NOT NULL,
        COUNT INTEGER,
        PRIMARY KEY(zipurl)
    );