from game import Game
from agent import Agent
import random
import time

random.seed(time.time())

playclock = 2

game = Game()
agent = Agent(playclock, game.state)

botWins = 0

for i in range(1, 51):
    print('running test: ', i)
    game.reset()
    while not game.state.won:
        available_moves = game.state.availableMoves()
        big_row, big_col, small_row, small_col = random.choice(available_moves)
        big = game.size*big_row + big_col
        small = game.size*small_row + small_col
        game.make_move(big, small)
        bigA, smallA = agent.nextAction(game.state)
        game.make_move(bigA, smallA)

        # switch this code out if agent.symbol is X
        """ bigA, smallA = agent.nextAction(game.state)
        game.make_move(bigA, smallA)
        game.state.print_board()
        available_moves = game.state.availableMoves()
        big_row, big_col, small_row, small_col = random.choice(available_moves)
        big = game.size*big_row + big_col
        small = game.size*small_row + small_col
        game.make_move(big, small)
        game.state.print_board() """

    if(game.state.won == 'O'):
        print("bot won")
        botWins += 1

print(botWins)
