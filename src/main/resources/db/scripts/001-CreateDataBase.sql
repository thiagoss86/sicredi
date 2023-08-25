create table pautas
(
    nr_id_pauta bigint auto_increment primary key,
    tx_name varchar(100) not null,
    tx_session_status varchar(10) default 'CLOSED' not null,
    dt_limit_time datetime(6) null
);

create table votos
(
    nr_id_voto bigint auto_increment primary key,
    tx_associate_cpf varchar(14) not null,
    tx_voto_value varchar(3) not null,
    nr_pauta_id bigint not null,
    constraint voto_pauta_idfk
        foreign key (nr_pauta_id) references pautas (nr_id_pauta),
    constraint tx_associate_cpf
        unique (tx_associate_cpf)
);