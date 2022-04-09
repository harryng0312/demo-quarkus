CREATE TABLE user_
(
    id_                     bigint        not null,
    created_date            timestamp      not null,
    modified_date           timestamp      not null,
    status                  varchar(36)  not null,
    screenname              varchar(36)  not null,
    username                varchar(36)  not null,
    password_               varchar(100) not null,
    dob                     date          not null,
    passwd_encrypted_method varchar(50)  not null default '',
    CONSTRAINT user_pk PRIMARY KEY (id_)
);

CREATE UNIQUE INDEX user_IX1 ON user_ (id_);
CREATE INDEX user_IX2 ON user_ (username);

INSERT INTO USER_(ID_, CREATED_DATE, MODIFIED_DATE, STATUS, SCREENNAME,
                  USERNAME, PASSWORD_, DOB, PASSWD_ENCRYPTED_METHOD)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', 'screen name 1',
        'username 1', 'passwd1', CURRENT_DATE, 'plain');