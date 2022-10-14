DROP DATABASE IF EXISTS minicloud;
CREATE DATABASE minicloud;

create table minicloud.cpus (
    id bigint not null auto_increment,
    architecture varchar(255) not null,
    cache integer not null,
    clock_frequency double precision not null,
    cores integer not null,
    version INT DEFAULT 0,
    primary key (id)
) engine=InnoDB;

create table minicloud.machines (
    id bigint not null auto_increment,
    has_gpu boolean default false not null,
    hd_in_bytes bigint,
    memory_in_bytes bigint not null,
    operational_system varchar(255),
    ssd_in_bytes bigint,
    cpu_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table minicloud.physical_machines (
    remain_cpu_cores integer not null,
    remain_hd_in_bytes bigint,
    remain_memory_in_bytes bigint,
    remain_ssd_in_bytes bigint,
    status varchar(255),
    id bigint not null,
    primary key (id)
) engine=InnoDB;

create table minicloud.virtual_machines (
    status varchar(255),
    id bigint not null,
    primary key (id)
) engine=InnoDB;

create table minicloud.virtual_physical_machine_allocations (
    id bigint not null auto_increment,
    created_date datetime(6),
    started_date datetime(6),
    updated_at datetime(6),
    user_id bigint,
    physical_machine_id bigint,
    virtual_machine_id bigint,
    primary key (id)
) engine=InnoDB;

alter table minicloud.machines
    add constraint FK_MACHINES_TO_CPU
        foreign key (cpu_id)
            references cpus (id);

alter table minicloud.physical_machines
    add constraint FK_PHYSICAL_MACHINES_TO_MACHINES
        foreign key (id)
            references machines (id);

alter table minicloud.virtual_machines
    add constraint FK_VIRTUAL_MACHINES_TO_MACHINES
        foreign key (id)
            references machines (id);

alter table minicloud.virtual_physical_machine_allocations
    add constraint FK_virtual_physical_machine_allocations_TO_PHYSICAL_MACHINES
        foreign key (physical_machine_id)
            references physical_machines (id);

alter table minicloud.virtual_physical_machine_allocations
    add constraint FK_virtual_physical_machine_allocations_TO_VIRTUAL_MACHINES
        foreign key (virtual_machine_id)
            references virtual_machines (id);