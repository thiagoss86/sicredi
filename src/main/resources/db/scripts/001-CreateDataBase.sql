create table schedules
(
    nr_id_schedule bigint auto_increment primary key,
    tx_name varchar(100) not null,
    tx_status varchar(10) default 'ACTIVE' not null,
    dt_limit_time datetime(6) not null
);

create table votes
(
    nr_id_votes bigint auto_increment primary key,
    tx_associate_id varchar(255) not null,
    tx_associate_cpf varchar(11) not null,
    tx_vote_value varchar(3) not null,
    nr_schedule_id bigint not null,
    constraint votes_schedule_idfk
        foreign key (nr_schedule_id) references schedules (nr_id_schedule),
    constraint tx_associate_id
        unique (tx_associate_id),
    constraint tx_associate_cpf
        unique (tx_associate_cpf)
);