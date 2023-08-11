create table gift_certificate
(
    id               int PRIMARY KEY AUTO_INCREMENT,
    name             varchar(45)  not null,
    description      varchar(500) not null,
    price            float        not null,
    duration         int          not null,
    create_date      timestamp    not null,
    last_update_date timestamp    not null,
    constraint gift_certificate_pk
        primary key (id)
);
create table tag
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(45) not null,
    constraint tag_pk
        primary key (id)
);
create table user
(
    id      int         NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NULL,
    surname VARCHAR(45) NULL,
    number  VARCHAR(45) NULL,
    constraint users_pk
        primary key (id)
);
create table purchase
(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    cost VARCHAR(50) NULL,
    isClosed TINYINT NULL DEFAULT 0,
    description VARCHAR(200) NULL,
    create_date datetime NULL,
    last_update_date datetime NULL,
    constraint purchase_pk
        PRIMARY KEY (id)
);


create table gift_certificate_has_tag
(
    gift_certificate_id int not null,
    tag_id              int not null
);
create table gift_certificate_has_order
(
    gift_certificate_id int not null,
    order_id int not null,
    quantity int null default 1
);
alter table purchase
    add foreign key (user_id) references user(id) on delete cascade on update cascade ;
alter table gift_certificate_has_tag
    add foreign key (gift_certificate_id) references gift_certificate (id) on delete cascade on update cascade;
alter table gift_certificate_has_tag
    add foreign key (tag_id) references tag (id) on delete cascade on update cascade;
alter table gift_certificate_has_order
    add foreign key (gift_certificate_id) references gift_certificate (id) on delete cascade on update cascade;
alter table gift_certificate_has_order
    add foreign key (order_id) references purchase (id) on delete cascade on update cascade;
