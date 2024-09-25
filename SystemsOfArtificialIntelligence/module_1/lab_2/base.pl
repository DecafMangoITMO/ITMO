% Факты с одним аргументом
% Игра

game('The Witcher 3').
game('Doom').
game('Civilization VI').
game('Minecraft').
game('Portal 2').
game('Outlast').
game('Beat Saber').
game('Stardew Valley').
game('Half-Life: Alyx').
game('Factorio').

% Жанр игры

genre(action).
genre(adventure).
genre(strategy).
genre(simulator).
genre(role_playing).
genre(puzzle).
genre(shooter).
genre(musical).
genre(horror).
genre(casual).
genre(education).

% Игровая платформа

platform(pc).
platform(console).
platform(mobile).
platform(vr).

% Тип игры

type(singleplayer).
type(multiplayer).
type(cooperative).

% Факты с двумя аргументами
% Игра и ее жанр

game_genre('The Witcher 3', role-playing).
game_genre('The Witcher 3', adventure).
game_genre('The Witcher 3', strategy).

game_genre('Doom', shooter).
game_genre('Doom', action).
game_genre('Doom', horror).

game_genre('Civilization VI', strategy).
game_genre('Civilization VI', simulator).
game_genre('Civilization VI', education).

game_genre('Minecraft', simulator).
game_genre('Minecraft', adventure).
game_genre('Minecraft', strategy).

game_genre('Portal 2', puzzle).
game_genre('Portal 2', education).
game_genre('Portal 2', simulator).

game_genre('Outlast', horror).
game_genre('Outlast', adventure).
game_genre('Outlast', action).

game_genre('Beat Saber', musical).
game_genre('Beat Saber', education).
game_genre('Beat Saber', action).

game_genre('Stardew Valley', casual).
game_genre('Stardew Valley', simulator).
game_genre('Stardew Valley', adventure).

game_genre('Half-Life: Alyx', adventure).
game_genre('Half-Life: Alyx', role-playing).
game_genre('Half-Life: Alyx', action).

game_genre('Factorio', simulator).
game_genre('Factorio', adventure).
game_genre('Factorio', puzzle).

% Игра и ее платформа

game_platform('The Witcher 3', pc).
game_platform('The Witcher 3', console).

game_platform('Doom', console).
game_platform('Doom', pc).

game_platform('Civilization VI', pc).

game_platform('Minecraft', mobile).
game_platform('Minecraft', pc).
game_platform('Minecraft', console).

game_platform('Portal 2', pc).

game_platform('Outlast', pc).
game_platform('Outlast', console).

game_platform('Beat Saber', vr).

game_platform('Stardew Valley', pc).

game_platform('Half-Life: Alyx', vr).

game_platform('Factorio', pc).

% Игра и ее тип

game_type('The Witcher 3', singleplayer).

game_type('Doom', singleplayer).

game_type('Civilization VI', singleplayer).
game_type('Civilization VI', multiplayer).

game_type('Minecraft', singleplayer).
game_type('Mivecraft', cooperative).
game_type('Minecraft', multiplayer).

game_type('Portal 2', singleplayer).
game_type('Portal 2', cooperative).

game_type('Outlast', singleplayer).

game_type('Beat Saber', singleplayer).

game_type('Stardew Valley', singleplayer).
game_type('Stardew Valley', cooperative).

game_type('Half-Life: Alyx', singleplayer).

game_type('Factorio', singleplayer).
game_type('Factorio', cooperative).

% Игра и ее год выпуска

game_age('Factorio', 2020).
game_age('Half-Life: Alyx', 2020).
game_age('Beat Saber', 2018).
game_age('Doom', 2016).
game_age('Civilization VI', 2016).
game_age('Stardew Valley', 2016).
game_age('The Witcher 3', 2015).
game_age('Portal 2', 2013).
game_age('Outlast', 2013).
game_age('Minecraft', 2011).

% Правила

% Игра вышла раньше
game_created_earlier(Game1, Game2) :- game(Game1), game(Game2), game_age(Game1, Age1), game_age(Game2, Age2), Age1 < Age2.

% Игра доступна на ПК
game_on_pc(Game) :- game(Game), game_platform(Game, pc).

% Многопользовательская игра-шутер
game_multiplayer_and_shooter(Game) :- game(Game), game_genre(Game, shooter).

% Игра для компании
game_for_company(Game) :- game(Game), (game_type(Game, cooperative); game_type(Game, multiplayer)).

% Игра не экшен
game_not_action(Game) :- game(Game), \+game_genre(Game, action).

% Найти игру такого же жанра
search_same_genre_game(Game1, Game2) :- game(Game1), game(Game2), \+(Game1=Game2), game_genre(Game1, Genre), game_genre(Game2, Genre).