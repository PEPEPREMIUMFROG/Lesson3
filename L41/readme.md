## инструкция по запуску приложения
### 1. Запустить БД в Docker
```bash
docker-compose up -d
```


### 2. Скомпилировать и запустить java проект 
```bash
mvn -X exec:java -Dexec.mainClass="org.example.App
```

### 3. Остановить БД в Docker после выполнения программы
```bash
docker-compose down -v
```
