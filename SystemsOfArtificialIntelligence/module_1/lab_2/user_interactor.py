class UserInteractor:
    learn_user_platforms_message_format = "I play on:"
    learn_user_genres_message_format = "I like:"
    learn_user_types_message_format = "I prefer:"

    def __init__(self, knowledge_base_interactor):
        self.knowledge_base_interactor = knowledge_base_interactor

    def validate_user_message(self, message : str, message_start_format):
        if len(message) == 0:
            return False
        if not message.startswith(message_start_format.lower()):
            return False
        return True

    def check_user_choice(self, chosen_items, available_items):
        checked_items = []
        for chosen_item in chosen_items:
            if chosen_item not in available_items:
                print(f"Warning: '{chosen_item}' is unknown.")
            else:
                checked_items.append(chosen_item)
        return checked_items

    def read_user_message(self, message_start_format):
        while True:
            message = input("> ")
            message = message.strip().replace("  ", " ").lower()
            if message == "exit":
                exit(1)
            if self.validate_user_message(message, message_start_format):
                break
            print("Invalid input. Please try again.")
        return message


    def learn_user_platforms(self):
        print("Enter game platforms you use. List of available platforms:\n")
        available_game_platforms = self.knowledge_base_interactor.get_available_game_platforms()
        for available_game_platform in available_game_platforms:
            print(f"- {available_game_platform}")
        print(f"\nEnter a message of format: \"{self.learn_user_platforms_message_format} <your platforms>\"")
        print(f"Example: {self.learn_user_platforms_message_format} pc, mobile\n")
        while True:
            message = self.read_user_message(self.learn_user_platforms_message_format)
            user_game_platforms = message[len(self.learn_user_platforms_message_format)::].strip().replace(" ", "").split(",")
            user_game_platforms = self.check_user_choice(user_game_platforms, available_game_platforms)
            if len(user_game_platforms) != 0:
                return user_game_platforms
            print("Not enough game platforms available. Please try again.")

    def learn_user_genres(self):
        print("Enter game genres you like. List of available genres:\n")
        available_game_genres = self.knowledge_base_interactor.get_available_game_genres()
        for available_game_genre in available_game_genres:
            print(f"- {available_game_genre}")
        print(f"\nEnter a message of format: \"{self.learn_user_genres_message_format} <your genres>\"")
        print(f"Example: {self.learn_user_genres_message_format} action, horror\n")
        while True:
            message = self.read_user_message(self.learn_user_genres_message_format)
            user_game_genres = message[len(self.learn_user_genres_message_format):].strip().replace(" ", "").split(",")
            user_game_genres = self.check_user_choice(user_game_genres, available_game_genres)
            if len(user_game_genres) != 0:
                return user_game_genres
            print("Not enough game genres available. Please try again.")

    def learn_user_types(self):
        print("Enter game types you prefer. List of available types:\n")
        available_game_types = self.knowledge_base_interactor.get_available_game_types()
        for available_game_type in available_game_types:
            print(f"- {available_game_type}")
        print(f"\nEnter a message of format: \"{self.learn_user_types_message_format} <your types>\"")
        print(f"Example: {self.learn_user_types_message_format} singleplayer, multiplayer\n")
        while True:
            message = self.read_user_message(self.learn_user_types_message_format)
            user_game_types = message[len(self.learn_user_types_message_format):].strip().replace(" ", "").split(",")
            user_game_types = self.check_user_choice(user_game_types, available_game_types)
            if len(user_game_types) != 0:
                return user_game_types
            print("Not enough game types available. Please try again.")
