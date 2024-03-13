create table products(
    id serial primary key,
    name varchar,
    category_id int not null references categories,
    price double precision
);

create table categories(
    id serial primary key,
    name varchar
);

create table options(
    id serial primary key,
    name varchar,
    category_id int references categories(id)
);


create table values(
    id serial primary key,
    name varchar,
    product_id int references products(id),
    options_id int references options(id)
);
