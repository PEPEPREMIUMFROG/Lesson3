insert into lesson_29.books (title, author, published_year, genre)
values
('War and peace', 'Lev Tolstoy', 1869, 'novel'),
('Crime and punishment','Fyodor Dostoevsky', 1866, 'novel'),
('Tale of Tsar Saltan', 'Alexander Pushkin',1831, 'fairy tale' ),
('Death of Ivan Ilyich','Lev Tolstoy',1886,'story');

insert into lesson_29.readers (name, email, phone) values
( 'Ivan Ivanov', 'ivanivanov@ya.ru','89528773930'),
( 'Ivan Petrov', 'ivanpetrov@ya.ru','89528773931'),
( 'Sergei Andreev', 'sergeiandreev@ya.ru','89528773933'),
( 'Andrei Smirnov', 'andreismrn@ya.ru','89528773932');

insert into lesson_29.borrowed_books
(book_id,reader_id,borrow_date, return_date,status) values
(1,1,'2025-10-08',null,'borrowed'),
(2,2,'2025-08-08','2025-09-10','returned'),
(3,3,'2025-09-22',null,'borrowed'),
(4,4,'2025-09-21',null,'borrowed');


update  lesson_29.books set published_year = '1888',genre = 'novel'
where title like ('War%');
update lesson_29.borrowed_books set return_date ='2025-10-09', status = 'returned'
where id = 4;

delete from lesson_29.borrowed_books where book_id = 1;
delete from lesson_29.readers where id = 1;

select * from lesson_29.borrowed_books;
select * from lesson_29.books;
select * from lesson_29.readers;

