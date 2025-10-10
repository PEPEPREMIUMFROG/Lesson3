drop schema if exists lesson_29 cascade;
create schema if not exists lesson_29;

CREATE table if not exists lesson_29.books (
    id bigserial primary key,
    title varchar(255) not null,
    author varchar(255) not null,
    published_year int check (published_year > 0) not null,
    genre varchar(100) not null
);

create table if not exists lesson_29.readers(
    id bigserial primary key,
    name varchar(100) not null,
    email varchar(255) not null unique,
    phone varchar(15) unique not null
);

create table if not exists lesson_29.borrowed_books(
    id bigserial primary key,
    book_id bigint references lesson_29.books(id) not null,
    reader_id bigint references lesson_29.readers(id) not null,
    borrow_date date not null,
    return_date date,
    status varchar(20) check (status in ('borrowed', 'returned')) not null
);

create index idx_title on lesson_29.books using btree(title);
create index idx_reader_status_borrowed on lesson_29.borrowed_books using btree (reader_id)
where status = 'borrowed';

select * from lesson_29.borrowed_books;