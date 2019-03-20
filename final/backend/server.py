from flask import Flask, jsonify, request
from flask_cors import CORS
import json
app = Flask(__name__)
CORS(app)

from game import Game

curr_game = Game()


@app.route("/board", methods=['GET'])
def get_board():
    return jsonify({"board": curr_game.board, "winnerBoard": curr_game.big_won["board"]})


@app.route("/move", methods=['POST'])
def make_move():
    json_data = json.loads(request.data)

    big = int(json_data["big"])
    small = int(json_data["small"])
    board = curr_game.make_move(big, small)
    if not board:
        return "Invalid input", 400
    return jsonify({"board": curr_game.board, "winnerBoard": curr_game.big_won["board"]})
