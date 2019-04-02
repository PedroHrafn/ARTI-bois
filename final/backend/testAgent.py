from game import Game
from agent import Agent
import random
import time

random.seed(time.time())

playclock = 2
number_of_games = 1
DEBUG = False

game = Game()
agent = Agent(playclock, game.state, "O")

botWins = 0

# Testing the agent with the symbol O
print("Testing the agent as O...")
for i in range(1, number_of_games + 1):
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
        if DEBUG:
            game.state.print_board()

    if(game.state.won == 'O'):
        print("bot won")
        botWins += 1

agent.printTestResults()
print(f"Won {botWins} out of {number_of_games}, Win ratio: {botWins/number_of_games:.2}")

game = Game()
agent = Agent(playclock, game.state, "X")

botWins = 0

# Testing the agent with the symbol X
print("Testing the agent as X...")
for i in range(1, number_of_games + 1):
    print('running test: ', i)
    game.reset()
    while not game.state.won:
        # switch this code out if agent.symbol is X
        bigA, smallA = agent.nextAction(game.state)
        game.make_move(bigA, smallA)
        available_moves = game.state.availableMoves()
        big_row, big_col, small_row, small_col = random.choice(available_moves)
        big = game.size*big_row + big_col
        small = game.size*small_row + small_col
        game.make_move(big, small)
        if DEBUG:
            game.state.print_board()

    if(game.state.won == 'X'):
        print("bot won")
        botWins += 1

agent.printTestResults()
print(f"Won {botWins} out of {number_of_games}")