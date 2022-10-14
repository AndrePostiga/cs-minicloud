DROP DATABASE IF EXISTS minicloud;
CREATE DATABASE minicloud;

create table minicloud.cpus (
    id bigint not null auto_increment,
    architecture varchar(255) not null,
    cache integer not null,
    clockFrequency double precision not null,
    cores integer not null,
    version INT DEFAULT 0,
    primary key (id)
) engine=InnoDB
CHARACTER SET utf8mb4;