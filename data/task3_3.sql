-- TODO Task 3
drop database if exists shoppingcart;

create database shoppingcart;

use shoppingcart;

create table shopping_order (
    orderId char(26) not null,
    date date,
    name varchar(128) not null,
    address varchar(256) not null,
    priority boolean default false,
    comments text,

    primary key(orderId)
);

create table lineItem(
    id int auto_increment,
    productId varchar(64) not null,
    name varchar(256) not null,
    quantity int not null,
    price float not null,
    orderId char(26) not null,

    primary key(id),
    foreign key(orderId) references shopping_order(orderId)
);

