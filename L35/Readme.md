
# Library Project

## Описание
Проект для управления библиотекой с использованием Java, Maven и Docker.

## Быстрый старт

### Сборка приложения
```bash
mvn clean package 
```
*Собирает JAR-файл без запуска тестов*


### Запуск в Docker
```bash
docker-compose up -d
```
*Запускает контейнеры в фоновом режиме*

### Остановка приложения
```bash
docker-compose down -v
```
*Останавливает контейнеры и удаляет тома*

## Доступ к приложению
После запуска приложение доступно по адресу:  
http://localhost:8080/library/library

wsdl-описание доступно по адресу:

http://localhost:8080/library/soap/LibrarySoapService?wsdl

## Тестирование в postman

1. Откройте Postman и создайте новый POST-запрос.
2. Укажите URL: http://localhost:8080/library/soap/LibrarySoapService
3. Перейдите во вкладку Headers и добавьте:

>Content-Type: text/xml; charset=utf-8

4. Перейдите во вкладку Body → выберите raw → формат XML.
Вставьте один из примеров ниже.

получить книгу по id:
><soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.example.org/">
<soapenv:Header/>
<soapenv:Body>
<soap:getBookById>
<bookId>1</bookId>
</soap:getBookById>
</soapenv:Body>
</soapenv:Envelope>

добавить новую книгу:

><soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.example.org/">
<soapenv:Header/>
<soapenv:Body>
<soap:addBook><title>Книга из Postman</title>
<author>Postman</author>
<publishedYear>2025</publishedYear>
<genre>Тест</genre>
</soap:addBook>
</soapenv:Body>
</soapenv:Envelope>