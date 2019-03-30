class State(object):
    def __init__(self, board, big_won, x_turn, game_over, next_big):
        # State variables
        self.board = board
        self.x_turn = x_turn
        self.game_over = game_over
        self.big_won = big_won

        # What cell next player does, if [] then any.
        self.next_big = next_big  # [col, row]

    def availableMoves(self, size):
        moves = []
        if self.next_big:
            for col in range(size):
                for row in range(size):
                    if self.board[self.next_big[1]][self.next_big[0]][row][col] == '':
                        moves.append([col, row])
        else:
            for big_col in range(size):
                for big_row in range(size):
                    for col in range(size):
                        for row in range(size):
                            # If cell has not been won
                            if self.big_won["board"][big_row][big_col] == '':
                                if self.board[big_row][big_col][row][col] == '':
                                    moves.append([col, row])
        return moves

    def makeMove(self):
        #TODO: implement
        pass

    def undoMove(self):
        #TODO: implement
        pass
