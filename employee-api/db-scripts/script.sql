create table tb_people(
    dsfirst_name        varchar(100) not null ,
    dslast_name         varchar(100) not null,
    cdidentification    varchar(100),
    constraint tb_people_pk primary key ( cdidentification )
);