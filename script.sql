-- Base de datos de clientes
create table client (status boolean, id bigint not null, password varchar(255), primary key (id));
create table person (age integer, id bigint generated by default as identity, address varchar(255), gender varchar(255), identification varchar(255), name varchar(255), phone varchar(255), primary key (id));
alter table if exists client add constraint FKr1e0j10i9v9i52l6tqfa69nj0 foreign key (id) references person;

-- Base de datos de cuentas
create table account (initial_balance float(53), status boolean, client_id bigint, id bigint generated by default as identity, account_number varchar(255), account_type varchar(255), primary key (id));
create table movement (amount float(53), balance float(53), created_at date, status boolean, account_id bigint, id bigint generated by default as identity, movement_type varchar(255), primary key (id));
alter table if exists movement add constraint FKoemeananv9w9qnbcoccbl70a0 foreign key (account_id) references account;