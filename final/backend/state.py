class State(object):
    # State variables
    board = []
    size = 3
    big_won = {}
    x_turn = True
    game_over = False
    move_counter = 0

    # What cell next player does, if [] then any.
    next_big = []  # [col, row]
 
    def __init__(self, size=3):
        self.reset(size)

    def _initBoard(self):
        self.board = []
        # For each row in big board
        for _ in range(self.size):
            big_row = []
            # For each cell in row in big Board
            for _ in range(self.size):
                big_row.append(self._makeSmallBoard())
            self.board.append(big_row)

        self.big_won["board"] = self._makeSmallBoard()

    def _makeSmallBoard(self):
        row = ["" for _ in range(self.size)]
        small_board = []
        # For each row in small board
        for _ in range(self.size):
            # slice for deep copy
            small_board.append(row[:])
        return small_board
    
    def reset(self, size=3):
        self.size = size
        self.big_won = {"board": [], "tiles": [], "winner": ""}
        self._initBoard()
        self.x_turn = True
        self.game_over = False
        self.move_counter = 0

        # What cell next player does, if [] then any.
        self.next_big = []  # [col, row]
    
    def availableMoves(self):
        #TODO: implement
        pass