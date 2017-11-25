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
        phone VARCHAR(255)
    );
CREATE TABLE
    ROLE
    (
        id UUID PRIMARY KEY,
        role VARCHAR(255)
    );
CREATE TABLE
    USER_ROLE
    (
        userid UUID REFERENCES ACCOUNT(id),
        roleid UUID REFERENCES ROLE(id),
        PRIMARY KEY(userid, roleid)
    );
CREATE TABLE
    CACHEDS3BUCKET
    (
        id UUID NOT NULL PRIMARY KEY,
        created TIMESTAMP,
        lastmodified TIMESTAMP,
        stillavailable BOOLEAN,
        name VARCHAR(255)
    );
CREATE TABLE
    CACHEDS3OBJECT
    (
        name VARCHAR(255) NOT NULL,
        created TIMESTAMP,
        lastmodified TIMESTAMP,
        bucketid UUID NOT NULL,
        PRIMARY KEY(bucketid, name),
        CONSTRAINT s3o_to_s3b FOREIGN KEY(bucketid) REFERENCES CACHEDS3BUCKET(id)
    );
CREATE TABLE
    CACHEDS3WORDCOUNTPAIR
    (
        word VARCHAR(255) NOT NULL,
        COUNT INTEGER,
        bucketid UUID NOT NULL,
        PRIMARY KEY(bucketid,word),
        CONSTRAINT s3wcp_to_s3b FOREIGN KEY(bucketid) REFERENCES CACHEDS3BUCKET(id)
    );