from game import Game
from agent import Agent
import random
import time

random.seed(time.time())

def saveToFile(name, records):
    text = ""
    for record in records:
        print(record)
        text_record = [str(r) for r in record]
        text += ','.join(text_record)
        text += ";\n" 
    with open(name, "a+") as f:
        f.write(text)

playclock = 2
number_of_games = 50
DEBUG = False

game = Game()
agent = Agent(playclock, game.state, "O")

botWins = 0

x_performance = []
o_performance = []

# Testing the agent with the symbol O
print("Testing the agent as O...")
for i in range(1, number_of_games + 1):
    print('running test: ', i)
    game.reset()
    agent.reset_test()
    while not game.state.won:
        available_moves = game.state.availableMoves()
        big_row, big_col, small_row, small_col = random.choice(available_moves)
        big = game.size*big_row + big_col
        small = game.size*small_row + small_col
        game.make_move(big, small)
        big_a, smallA = agent.nextAction(game.state)
        game.make_move(big_a, smallA)
        if DEBUG:
            game.state.print_board()

    if(game.state.won == 'O'):
        print("bot won")
        botWins += 1
    
    o_performance.append((agent.max_depth, agent.iteration_avg, agent.avg_iter_deep, agent.node_expanded))

# Save results to file
saveToFile("test-o.csv", o_performance)

print(f"Won {botWins} out of {number_of_games}, Win ratio: {botWins/number_of_games:.2}")

game = Game()
agent = Agent(playclock, game.state, "X")

botWins = 0

# Testing the agent with the symbol X
print("Testing the agent as X...")
for i in range(1, number_of_games + 1):
    print('running test: ', i)
    game.reset()
    agent.reset_test()
    while not game.state.won:
        # switch this code out if agent.symbol is X
        big_a, smallA = agent.nextAction(game.state)
        game.make_move(big_a, smallA)
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
        
    x_performance.append((agent.max_depth, agent.iteration_avg, agent.avg_iter_deep, agent.node_expanded))

# Save results to file
saveToFile("test-x.csv", x_performance)
print(f"Won {botWins} out of {number_of_games}, Win ratio: {botWins/number_of_games:.2}")