__The Rules__

Programs should read from stdin and write to stdout. The state of the game will
be provided at the beginning of runtime, so programs should terminate after
every move. The *hard* time limit is one second, which means that you should
terminate your program after 500ms just to be safe. Davey gets 30s per move, or
as much time as it takes before we all get impatient.

* Input format

    The first line contains either a '1' or a '2', which indicates which player
    your program is. The second line contains an integer N, between 10 and 20
    inclusive, representing the side length of the board (the board is square).
    The next N lines contain strings of N characters each, where each character
    is defined as follows:
        '.': an empty cell
        '1': one of Player1's tiles
        '2': one of Player2's tiles

* Output format

    Your output should contain one line, which contains an integer from 1 to N
    inclusive. This represents which column you want to drop your tile into. If
    you drop your tile into a full column, you lose.
