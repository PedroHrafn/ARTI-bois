from game import Game
from agent import Agent
import random
import time

random.seed(time.time())

playclock = 3

game = Game()
agent = Agent(playclock, game.state)

botWins = 0

for i in range(1,51):
    print('running test: ' , i)
    game.reset()
    while not game.state.won:
        available_moves = game.state.availableMoves()
        #print(len(available_moves))
        big_row, big_col, small_row, small_col = random.choice(available_moves)
        big = game.size*big_row + big_col
        small = game.size*small_row + small_col
        game.make_move(big, small)
        bigA, smallA = agent.nextAction(game.state)
        game.make_move(bigA, smallA)

    if(game.state.won == 'O'):
        botWins += 1

print(botWins)
