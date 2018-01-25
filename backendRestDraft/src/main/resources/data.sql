INSERT
INTO
    ACCOUNT
    (
        id,
        username,
        password,
        firstname,
        lastname,
        email,
        address,
        phone
    )
    VALUES
    (
        '63b61ed5-5ae4-4cc8-9aa3-f14a15c31755',
        'user1',
        '$2a$11$kRVKDZdUVu/8ukF/vD97HOKSTqyOE2GW1CxzEjIRFOjOE.ee9dFvC', -- "pass1"
        'James',
        'Doe',
        'james.doe@pretrendr.de',
        'Grovestreet 1',
        '555 555 555'
    )
    ,
    (
        '337bf2b7-a12e-4d48-ba84-1feaddbd4b20',
        'user2',
        '$2a$11$V4K/YKxxp.N6hHJF/Ef0qul/aDZkOK6GTmo4VgEXGmcgpBXPtifTi', -- "pass2"
        'John',
        'Doe',
        'john.doe@pretrendr.de',
        'Grovestreet 1',
        '555 555 555'
    )
    ,
    (
        '8ad5b3f5-5c78-415f-bd7a-34b38d523027',
        'user3',
        '$2a$11$ItX.nhf7GXskxpGGH9u2Re0M5gjdBGFvWtg/mZZDbmMScjfBgn1ua', -- "pass3"
        'Johnny',
        'Sixpack',
        'johnny.sixpack@pretrendr.de',
        'Grovestreet 1',
        '555 555 555'
    );
INSERT
INTO
    ROLE
    (
        id,
        role
    )
    VALUES
    (
        '8ad5b3f5-abcd-415f-bd7a-34b38d523027',
        'USER'
    )
    ,
    (
        '8ad5b3f5-abce-415f-bd7a-34b38d523027',
        'ADMIN'
    );
INSERT
INTO
    USER_ROLE
    (
        userid,
        roleid
    )
    VALUES
    (
        '63b61ed5-5ae4-4cc8-9aa3-f14a15c31755',
        '8ad5b3f5-abcd-415f-bd7a-34b38d523027'
    )
    ,
    (
        '337bf2b7-a12e-4d48-ba84-1feaddbd4b20',
        '8ad5b3f5-abcd-415f-bd7a-34b38d523027'
    )
    ,
    (
        '337bf2b7-a12e-4d48-ba84-1feaddbd4b20',
        '8ad5b3f5-abce-415f-bd7a-34b38d523027'
    )
    ,
    (
        '8ad5b3f5-5c78-415f-bd7a-34b38d523027',
        '8ad5b3f5-abce-415f-bd7a-34b38d523027'
    );