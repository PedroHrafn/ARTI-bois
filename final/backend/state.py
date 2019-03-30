class State(object):
    def __init__(self, board, size, big_won, x_turn, game_over, next_big):
        # State variables
        self.board = board
        self.x_turn = x_turn
        self.game_over = game_over
        self.big_won = big_won

        # What cell next player does, if [] then any.
        self.next_big = next_big# [col, row]
            
        
    def availableMoves(self):
        #TODO: implement
        pass
    
    def makeMove(self):
        #TODO: implement
        pass
        
    def undoMove(self):
        #TODO: implement
        pass
