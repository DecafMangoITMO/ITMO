from pyswip import Prolog
from pyswip.prolog import PrologError

class KnowledgeBaseInteractor:
    def __init__(self, path_to_base):
        self.prolog = Prolog()
        try:
            self.prolog.consult(path_to_base)
        except PrologError:
            print(f"Could not read knowledge base by path: {path_to_base}.")
            exit(22)

    def get_available_game_platforms(self):
        request = "platform(Platform)."
        available_game_platforms = self.prolog.query(request)
        return [game_platform["Platform"] for game_platform in available_game_platforms]

    def get_available_game_genres(self):
        request = "genre(Genre)."
        available_game_genres = self.prolog.query(request)
        return [game_genre["Genre"] for game_genre in available_game_genres]

    def get_available_game_types(self):
        request = "type(Type)."
        available_game_types = self.prolog.query(request)
        return [game_type["Type"] for game_type in available_game_types]

    def get_game_age(self, game_name):
        query = f"game_age('{game_name}', Age)."
        return (list(self.prolog.query(query)))[0]["Age"]

    def look_for_matching_games(self, platforms, genres, types):
        request = "game(Game),"
        request += "("
        for platform in platforms:
            request += f"game_platform(Game, {platform});"
        request = request[:len(request) - 1] + "),"
        request += "("
        for genre in genres:
            request += f"game_genre(Game, {genre});"
        request = request[:len(request) - 1] + "),"
        request += "("
        for type in types:
            request += f"game_type(Game, {type});"
        request = request[:len(request) - 1] + ")."
        matching_games = [matching_game["Game"] for matching_game in list(self.prolog.query(request))]
        matching_games = map(lambda matching_game: matching_game + f"({self.get_game_age(matching_game)})", matching_games)
        return matching_games
