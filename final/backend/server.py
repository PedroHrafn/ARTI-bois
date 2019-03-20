from flask import Flask, jsonify, request
from flask_cors import CORS
import json
app = Flask(__name__)
CORS(app)

from game import Game

curr_game = Game()


@app.route("/board", methods=['GET'])
def get_board():
    return jsonify({
        "board": curr_game.board,
        "winnerBoard": curr_game.big_won["board"],
        "win": curr_game.game_over})


@app.route("/board/move", methods=['POST'])
def make_move():
    json_data = json.loads(request.data)

    big = int(json_data["big"])
    small = int(json_data["small"])
    success, winner = curr_game.make_move(big, small)
    if not success:
        return "Invalid input", 400
    return jsonify({
        "board": curr_game.board,
        "winnerBoard": curr_game.big_won["board"],
        "winner": winner
    })


@app.route("/board/reset", methods=['GET'])
def reset_board():
    curr_game.reset()
    return jsonify({
        "board": curr_game.board,
        "winnerBoard": curr_game.big_won["board"],
        "winner": ""
    })
