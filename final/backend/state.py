class State(object):
    def __init__(self, board, size, big_won, x_turn, game_over, next_big):
        # State variables
        self.board = board
        self.x_turn = x_turn
        self.game_over = game_over
        self.big_won = big_won

        # What cell next player does, if [] then any.
        self.next_big = next_big  # [col, row]

    def availableMoves(self):
        moves = []
        if self.next_big:
            for col in range(3):
                for row in range(3):
                    if self.board[self.next_big[0]][self.next_big[1]][col][row] == '':
                        moves.append([col, row])
        else:

            pass
        return moves

    def makeMove(self):
        #TODO: implement
        pass

    def undoMove(self):
        #TODO: implement
        pass
