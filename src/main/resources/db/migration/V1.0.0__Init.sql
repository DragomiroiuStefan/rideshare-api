create table if not exists "user"
(
    user_id         bigint generated always as identity primary key,
    email           varchar(255) unique not null,
    password        varchar(68)         not null,
    first_name      varchar(255)        not null,
    last_name       varchar(255)        not null,
    phone_number    varchar(15),
    birth_date      date                not null,
    profile_picture varchar(64),
    created_at      timestamp default now(),
    last_login      timestamp
);

create table role
(
    role_id    bigint generated always as identity primary key,
    name       varchar(50) not null unique,
    active     boolean   default true,
    created_by bigint references "user" (user_id),
    created_at timestamp default now()
);

create table user_roles
(
    user_id    bigint references "user" (user_id),
    role_id    bigint references role (role_id),
    granted_by bigint references "user" (user_id),
    granted_at timestamp default now(),
    primary key (user_id, role_id)
);

create table permission
(
    permission_id bigint generated always as identity primary key,
    name          varchar(50) not null unique,
    created_by    bigint references "user" (user_id),
    created_at    timestamp default now()
);

create table role_permissions
(
    role_id       bigint references role (role_id),
    permission_id bigint references permission (permission_id),
    assigned_by    bigint references "user" (user_id),
    assigned_at    timestamp default now(),
    primary key (role_id, permission_id)
);

create table if not exists vehicle
(
    plate_number      varchar(20) primary key,
    brand             varchar(50) not null,
    model             varchar(50) not null,
    color             varchar(50) not null,
    registration_year integer     not null,
    seats             int         not null,
    owner             bigint references "user" (user_id),
    image             varchar(64)
);

create table if not exists location
(
    location_id bigint generated always as identity primary key,
    city        varchar(255) not null,
    county      varchar(255) not null
);

create table if not exists ride
(
    ride_id            bigint generated always as identity primary key,
    driver             bigint references "user" (user_id)            not null,
    departure_date     date                                          not null,
    seats              int                                           not null,
    additional_comment varchar(255),
    vehicle            varchar(20) references vehicle (plate_number) not null,
    status             varchar(10)                                   not null,
    posted_at          timestamp default now()
);

create table if not exists ride_connection
(
    connection_id      bigint generated always as identity primary key,
    departure_location bigint references location (location_id),
    arrival_location   bigint references location (location_id),
    departure_time     timestamp not null,
    arrival_time       timestamp not null,
    price              int       not null,
    ride_id            bigint references ride (ride_id)
    -- ride_id , departure UNIQUE
    -- ride_id , arrival UNIQUE
);

create table if not exists ride_rating
(
    ride_rating_id bigint generated always as identity primary key,
    ride_id        bigint references ride (ride_id)   not null,
    user_id        bigint references "user" (user_id) not null,
    rating         int                                not null,
    comment        varchar(255),
    posted_at      timestamp default now()
);

-- sa il pot folosi si la user si la driver
create table if not exists user_review
(
    user_review_id bigint generated always as identity primary key,
    user_id        bigint references "user" (user_id) not null,
    --reviewed_by    bigint references "user" (user_id) not null,
    rating         int                                not null,
    comment        varchar(255),
    posted_at      timestamp default now()
);

create table if not exists booking
(
    booking_id bigint generated always as identity primary key,
    user_id    bigint references "user" (user_id) not null,
    ride_id    bigint references ride (ride_id)   not null,
    seats      int                                not null,
    status     varchar(10)                        not null
);

create table if not exists booking_status_history
(
    booking_status_history_id bigint generated always as identity primary key,
    booking_id                bigint references booking (booking_id) not null,
    status                    varchar(10)                            not null,
    updated_at                timestamp default now()
);

create table if not exists booking_connection
(
    booking_connection_id bigint generated always as identity primary key,
    booking_id            bigint references booking (booking_id),
    connection_id         bigint references ride_connection (connection_id),
    unique (booking_id, connection_id)
);