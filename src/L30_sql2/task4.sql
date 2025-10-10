select b.title, b.author, bb.reader_id, bb.borrow_date
from lesson_29.books b
inner join lesson_29.borrowed_books bb on b.id = bb.book_id
where status like('borrowed');

select count(*) as  book_count, reader_id
from lesson_29.borrowed_books
group by reader_id;

select reader_id as reader
from lesson_29.borrowed_books
group by reader_id
having count(*) >1;

insert into lesson_29.borrowed_books
(book_id,reader_id,borrow_date, return_date,status) values
(14,2,'2025-09-08','2025-09-18','returned'),
(14,3,'2025-09-27',null,'borrowed'),
(3,2,'2025-09-22',null,'borrowed');


select b.title, b.author, bb.reader_id, bb.borrow_date, bb.return_date
from lesson_29.books b
join lesson_29.borrowed_books bb on b.id = bb.book_id
where b.genre in ('story');