create table gift_certificate
(
    id               int PRIMARY KEY  AUTO_INCREMENT,
    name             varchar(45)     not null ,
    description      varchar(500)    not null ,
    price            float           not null ,
    duration         int             not null ,
    create_date      timestamp       not null ,
    last_update_date timestamp       not null ,
    constraint gift_certificate_pk
        primary key (id)
);
create table tag
(
    id   int PRIMARY KEY  AUTO_INCREMENT,
    name varchar(45)     not null ,
    constraint tag_pk
        primary key (id)
);
create table gift_certificate_has_tag
(
    gift_certificate_id int not null,
    tag_id              int not null

);
alter table gift_certificate_has_tag add foreign key(gift_certificate_id) references  gift_certificate(id) on delete cascade on update cascade ;
alter table gift_certificate_has_tag add foreign key(tag_id) references  tag(id) on delete cascade on update cascade ;
