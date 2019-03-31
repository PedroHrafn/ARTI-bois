from flask import Flask, jsonify, request
from flask_cors import CORS
import json
app = Flask(__name__)
CORS(app)

# Our classes
from game import Game
from agent import Agent

curr_game = Game()
agent = Agent()


@app.route("/board", methods=['GET'])
def get_board():
    return jsonify({
        "board": curr_game.state.extract_board(),
        "winnerBoard": curr_game.state.big_to_small(),
        "nextBig": curr_game.state.next_big,
        })


@app.route("/board/move", methods=['POST'])
def make_move():
    json_data = json.loads(request.data)

    big = int(json_data["big"])
    small = int(json_data["small"])
    success, winner = curr_game.make_move(big, small)
    if not success:
        return "Invalid input", 400
    return jsonify({
        "board": curr_game.state.extract_board(),
        "winnerBoard": curr_game.state.big_to_small(),
        "nextBig": curr_game.state.next_big,
        "winner": winner
    })

@app.route("/agent/move", methods=['GET'])
def agent_move():
    big, small = agent.nextAction(curr_game.state)
    success, winner = curr_game.make_move(big, small)
    if not success:
        return "Invalid input", 400
    return jsonify({
        "board": curr_game.state.extract_board(),
        "winnerBoard": curr_game.state.big_to_small(),
        "nextBig": curr_game.state.next_big,
        "winner": winner
    })


@app.route("/board/reset", methods=['GET'])
def reset_board():
    curr_game.reset()
    return jsonify({
        "board": curr_game.state.extract_board(),
        "winnerBoard": curr_game.state.big_to_small(),
        "nextBig": curr_game.state.next_big,
        "winner": ""
    })
