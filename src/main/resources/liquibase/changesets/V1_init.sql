create table if not exists users
(
    id       bigserial primary key,
    name     varchar(100) not null,
    email    varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists events
(
    id                              bigserial       primary key,
    name                            varchar(100)    not null,
    date                            date            not null,
    available_attendees_count       bigint          not null,
    description                     varchar(500)    null,
    category                        varchar(255)    not null
);

create table if not exists users_events
(
    user_id bigserial not null,
    event_id bigserial not null,
    ticket_count bigint not null,
    primary key (user_id, event_id),
    constraint fk_users_events_users foreign key (user_id) references users (id) on delete cascade on update no action,
    constraint fk_users_events_events foreign key (event_id) references events (id) on delete cascade on update no action
);

