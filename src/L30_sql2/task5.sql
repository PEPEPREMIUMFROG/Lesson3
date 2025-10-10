-- предположим необходимо удалить читателя со всеми ебго данными

-- пример с нарушением атомарности
delete from lesson_29.borrowed_books where reader_id = 1;
-- на данном этапе может произойти что угодно, например отключение света
-- в таком случае следующая операция не будет завершена
delete from lesson_29.readers where id = 1;

-- пример операции с соблюдением атомарности
begin
delete from lesson_29.borrowed_books where reader_id = 1;
delete from lesson_29.readers where id = 1;
commit;