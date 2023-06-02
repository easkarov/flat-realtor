BEGIN;

CREATE TABLE IF NOT EXISTS house
(
    id              serial,
    name            text   NOT NULL,
    year            bigint NOT NULL CHECK ( year > 0 ),
    number_of_flats int    NOT NULL CHECK ( number_of_flats > 0 ),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS role
(
    name text PRIMARY KEY
);

INSERT INTO role(name)
VALUES ('MIN_USER'),
       ('MIDDLE_USER'),
       ('ADMIN');


CREATE TABLE IF NOT EXISTS "user"
(
    id       serial,
    username text NOT NULL UNIQUE,
    password text NOT NULL,
    salt     text NOT NULL,
    role     text NOT NULL REFERENCES role (name) DEFAULT 'MIN_USER',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS flat
(
    id              bigserial,
    name            text        NOT NULL,
    x_coord         bigint      NOT NULL,
    y_coord         numeric     NOT NULL,
    created_at      timestamptz NOT NULL DEFAULT now(),
    area            bigint      NOT NULL CHECK ( area > 0 ),
    number_of_rooms bigint      NOT NULL CHECK ( number_of_rooms > 0 ),
    furnish         text,
    view            text        NOT NULL,
    transport       text,
    house_id        int         NOT NULL UNIQUE,
    owner_id        int         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (house_id) REFERENCES house (id),
    FOREIGN KEY (owner_id) REFERENCES "user" (id)
);


CREATE TABLE IF NOT EXISTS command_role
(
    command text UNIQUE,
    role    text REFERENCES role (name),
    PRIMARY KEY (command, role)
);

INSERT INTO command_role(command, role)
VALUES ('change_role', 'ADMIN');

COMMIT;
