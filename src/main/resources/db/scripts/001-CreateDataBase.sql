create table schedules
(
    tx_id_schedule varchar(255) not null primary key,
    tx_name varchar(100) not null,
    tx_status varchar(10) default 'ACTIVE' not null,
    dt_limit_time datetime(6) not null
);

create table votes
(
    tx_id_votes varchar(255) not null primary key,
    tx_associate_id varchar(255) not null,
    tx_associate_cpf varchar(11) not null,
    tx_vote_value varchar(3) not null,
    tx_schedule_id varchar(255) not null,
    constraint votes_schedule_idfk
        foreign key (tx_schedule_id) references schedules (tx_id_schedule),
    constraint tx_associate_id
        unique (tx_associate_id),
    constraint tx_associate_cpf
        unique (tx_associate_cpf)
);