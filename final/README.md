# Ultimate Tic Tac Toe

Here you will find instructions on how to run the web interface.

## Backend

Begin by running the backend, which is written in Flask.

To setup the backend you must have the following installed:

- python3.7
- pip3
- virtualenv

Begin by setting the virtual environment up, this you only have to do one time:

```bash
virtualenv --no-site-packages servervenv
```

Then when running the server you enter the following in the backend directory:

```bash
source servervenv/bin/activate
FLASK_APP=server.py flask run
```

## Frontend

When the backend is up and running you can enter the following in the frontend directory:

```bash
yarn install
yarn start
```

## Tests

To run the tests simply run the following command in the backend directory

```bash
python testAgent.py
```

This command test the agent against a random agent as well as an instance of the agent itself.

## Note

This should get the system up and running, if not please contact us through alexmargunn@gmail.com
