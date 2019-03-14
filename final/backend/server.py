from flask import Flask
app = Flask(__name__)

boards = {
    "alex": []
}


@app.route("/board", methods=['GET'])
def get_board(name):

    return "Hello " + name

# Geohot, George Hots, myndband: twitch chess
