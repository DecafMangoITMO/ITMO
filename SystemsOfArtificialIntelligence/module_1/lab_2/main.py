import os
from knowledge_base_interactor import KnowledgeBaseInteractor
from user_interactor import UserInteractor

base_path = os.getenv('BASE_PATH')
prolog_interactor = KnowledgeBaseInteractor(base_path)
user_interactor = UserInteractor(prolog_interactor)

print("""
You are using a CLI-program to find matching games. 
You have to answer the some questions.
If you want to exit - write `exit` command.
""")

user_platforms = user_interactor.learn_user_platforms()
user_genres = user_interactor.learn_user_genres()
user_types = user_interactor.learn_user_types()

user_matching_games = prolog_interactor.look_for_matching_games(user_platforms, user_genres, user_types)
user_matching_games = list(set(user_matching_games))

if len(user_matching_games) == 0:
    print("Sorry, but there are not matching for you games :(")
else:
    print("I have just found something for you:")
    for matching_game in user_matching_games:
        print(f"- {matching_game}")