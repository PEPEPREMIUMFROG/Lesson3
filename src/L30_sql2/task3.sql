begin;
with new_book as (
    insert into lesson_29.books (title, author, published_year, genre)
    values ('Childhood', 'Lev Tolstoy', 1855, 'story')
   returning id
)
insert into lesson_29.borrowed_books (book_id, reader_id, borrow_date, return_date, status)
select id, 2, '2025-09-12', null, 'borrowed'
from new_book;
rollback;

select * from lesson_29.borrowed_books;
select * from lesson_29.books;

begin;
with new_book as (
    insert into lesson_29.books (title, author, published_year, genre)
    values ('Childhood', 'Lev Tolstoy', 1855, 'story')
   returning id
)
insert into lesson_29.borrowed_books (book_id, reader_id, borrow_date, return_date, status)
select id, 2, '2025-09-12', null, 'borrowed'
from new_book;
commit;

select * from lesson_29.borrowed_books;
select * from lesson_29.books;