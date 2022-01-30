use doordash;
create table phone_record
(
    id           int auto_increment
        primary key,
    phone_number varchar(100)  not null,
    phone_type   varchar(50)   not null,
    occurrences  int           not null,
    version      int default 0 not null,
    constraint phone_type
        unique (phone_type, phone_number)
);

create index phone_record_type_index
    on phone_record (phone_type, phone_number);
