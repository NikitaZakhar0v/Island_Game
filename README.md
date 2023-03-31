# Описание

Объекты создаются автоматически, главное указать необходимое количество в `countAnimal.properties`. 
Объекты (животные и растения) могут передвигаться из одной ячейки поля в другую, питаться другими животными и растениями, а также размножаться.
За каждое действие животное теряет одну единицу насыщения (`satiety`), в случае когда животное ест другое животное или растение, насыщение увеличивается на 3 единицы.
Растения являются Diamond потоками. Игра завершаются когда все животные умирают от голода. Каждый раз когда умирает животное,
выводится в лог сообщение о всех животных в ячейке и общее количество животных на поле, с учетом размножившихся.


## Настройка перед запуском

### `application.properties`

Задаются стартовые размеры поля:

`FieldRow` = "количество строк"

`FieldCol` = "количество столбцов"

`MultiplyPercent` = "процент на то что животные и растения будут размножаться" диапазон 0-100;

### `constructor.properties`
Задаются стартовые значения животных.

### `countAnimal.properties`
Задаются стартовые количества животных.