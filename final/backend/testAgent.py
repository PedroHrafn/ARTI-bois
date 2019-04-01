from game import Game
from agent import Agent
import random, time

random.seed(time.time())

playclock = 360

game = Game()
agent = Agent(playclock, game.state)

while not game.state.won:
    for row in game.state.big_board:
        print(row)
    available_moves = game.state.availableMoves()
    print(len(available_moves))
    big_row, big_col, small_row, small_col = random.choice(available_moves)
    big = game.size*big_row + big_col
    small = game.size*small_row + small_col
    game.make_move(big, small)
    bigA, smallA = agent.nextAction(game.state)
    game.make_move(bigA, smallA)