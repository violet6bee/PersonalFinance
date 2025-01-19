Чтобы запустить приложение надо:
1. Создать контейнер с БД:
docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres

2. Зпустить приложение. В приложении доступны следующие команды:
create user - создать пользователя
login - зайти на аккаунт
create wallet - создать кошелек
add revenue - добавить доходы
add expenses - добавить траты
create a category - создать категорию
set a budget - установить бюджет
output data - вывести данные
exit - выйти из приложения
