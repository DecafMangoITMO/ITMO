# Отчет по модулю №1 курса 'Системы искусственного интеллекта'

---

- Студент: `Разинкин Александр Владимирович`
- Группа: `P3307`
- ИСУ: `368712`
- Выбранная предметная область: `Видеоигры: разделение по жанрам, году выпуска и пр.`

---

## Описание целей проекта и его значимости

Данный проект разделяется на две основные цели:

- Создание базы знаний на языке Prolog и онтологии в Protege на основе выбранной предметной области
- Используя готовую базу знаний на языке Prolog, реализовать CLI-приложение, предоставляющее из себя систему
поддержки принятия решений на основе выбранной предметной области

Значимостью данного проекта заключается во __введении в курс искусственного интеллекта__, а также в __базовом
представлении о логических языках программирования__ (относительно императивных и функциональных языков
программирования).

--- 

## Анализ требований

Требования к системе принятия решений:

1) Система должна предоставить пользователю выбрать интересующие его жанры, платформы и типы видеоигр
2) Система должна на основе проведенного с пользователем диалога предоставить ему список подходящих видеоигр
в виде 'Название (год выпуска)'
3) Система должна уметь работать с разными базами знаний, созданными на логическом языке Prolog
4) Система должна предоставить пользователю завершить работу программы, не дожидаясь конца диалога, при помощи 
текстовой команды 'exit'

Требования к базе знаний и онтологии:

1) Должны быть представлены названия видеоигр
2) Должны быть представлены названия жанров, платформ и типов видеоигр
3) У каждой игры должно быть минимум:
   - Один жанр
   - Одна платформа
   - Один тип
4) У каждой видеоигры должен быть год выпуска

Примечание: здесь представлены требования по отношению конкретно к самому проекту - основные требования 
можно посмотреть в 
[описании лабораторных работ](https://sunnysubmarines.notion.site/AI-System-a559a46cddc44363bdf27b77e10b7d85). 

--- 

## Изучение основных концепций и инструментов

Сперва стоит разобраться с двумя близкими, но разными понятиями: базой знаний и онтологией.

В информационных источниках много разных мнений насчет того, как отличать базу знаний от онтологии. Мне нравится 
следующая позиция: `Отличие базы знаний и онтологии заключается в исчерпываемости информации о представленной 
предметной области и структурированности данной информации: требования полноте информации к онтологий 
более строгие по сравнению с базой знаний; к тому же онтологии предоставляют структурированную информацию 
(например, в виде классов и соответствующих им свойств), когда базы знаний оперируют лишь множеством фактов и правил;
знаний`

Примечание: я не гарантирую на стопроцентную достоверность данного определения, но оно мне кажется более понятным.

--- 

В качестве одного из инструментов был применен логический язык программирования `Prolog`.
Данный язык оперирует следующими понятиями:

- Факт - в рамках Prolog можно привести аналогию с логическим предикатом (например: факт `use(developer, prolog).`, что 
на математическом языке будет выглядеть `Разработчик использует Prolog`)
- Правило - расширение фактов, которое позволяет нам получать новые знания на основе имеющихся фактов
(например: `hard_skilled_dev(Developer) :- use(Developer, prolog, Ages), Ages > 3.` - Разработчик считается продвинутым 
в случае, если он программирует на Prolog более 3 лет)

К тому же в Prolog константы (они пишутся с малой буквы) и переменные (они, логично, пишутся с большой буквы).

Prolog для поиска решения применяет механизм унификации (если максимально просто говорить - сопоставление с 
базой знаний), на основе которого Prolog подбирает решение и проходится по дереву фактов и правил.

--- 

Основные инструменты для работы с Prolog:

- SWI-Prolog - учебная версия Prolog (содержит консольный интерфейс)
- pyswip - библиотека на Python для работы c Prolog

--- 

## Пример работы системы

```
You are using a CLI-program to find matching games. 
You have to answer the some questions.
If you want to exit - write `exit` command.

Enter game platforms you use. List of available platforms:

- pc
- console
- mobile
- vr

Enter a message of format: "I play on: <your platforms>"
Example: I play on: pc, mobile

> I play on: mobile
Enter game genres you like. List of available genres:

- action
- adventure
- strategy
- simulator
- role_playing
- puzzle
- shooter
- musical
- horror
- casual
- education

Enter a message of format: "I like: <your genres>"
Example: I like: action, horror

> I like: adventure
Enter game types you prefer. List of available types:

- singleplayer
- multiplayer
- cooperative

Enter a message of format: "I prefer: <your types>"
Example: I prefer: singleplayer, multiplayer

> I prefer: singleplayer
I have just found something for you:
- Minecraft(2011)
```

--- 

## Оценка и интерпретация результатов

Примеры запросов непосредственно к базе знаний в Prolog:

```
game('Factorio').

game(Game).

game_genre('Factorio', Genre).

Найти первую попавшуюся игру с годом выпуска 2016 и на ПК
game(Game), game_age(Game, 2016), game_platform(Game, pc), !.

Найти игру на платформе ПК или консоли и не жанра хоррор
findall(Game, ((game_platform(Game, pc); game_platform(Game, console)), \+(game_genre(Game, horror))), Result),
    sort(Result, Set).
```

Оценка соответствия проекта поставленным требованиям: реализованная система соответствует всем пунктам.

---

## Заключение

Prolog как логический язык программирования предоставляет хорошие возможности реализации систем искусственного проекта, 
а также язык имеет довольно приемлемый порог вхождения (первичная сложность заключается лишь в понимании алгоритма
поиска решения самим Prolog).


