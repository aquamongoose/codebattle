/*
 * CONNECT4
 */
#ifndef GAME_H
#define GAME_H
#include <stdio.h>
#include <stdlib.h>

#define MAX_STATE_SIZE 4000
#define MAX_CMD_SIZE 200
#define MAX_FILE_SIZE 100
#define MAX_BOARD_SIZE 50
#define MIN_BOARD_SIZE 5

// The board will be N x N.
int N;
char board[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
int moves;

FILE *logfile;
char *state;

void encode_state() {
    int cur = 0;
    for (int i=0; i<N; i++) {
        for (int j=0; j<N; j++)
            state[cur++] = board[i][j];
        state[cur++] = '\n';
    }
}

void init_game(char *player1, char *player2) {
    char *fname = (char *)malloc(MAX_FILE_SIZE);
    moves = 0;
    state = (char *)malloc(MAX_STATE_SIZE);
    sprintf(fname, "log_%s_%s.txt", player1, player2);
    logfile = fopen(fname, "w");
    free(fname);
    N = rand()%(MAX_BOARD_SIZE-MIN_BOARD_SIZE + 1) + MIN_BOARD_SIZE;
    for (int i=0; i<N; i++) {
        for (int j=0; j<N; j++) {
            board[i][j] = '.';
        }
    }
    encode_state();
}

void finalize_game() {
    free(state);
    fclose(logfile);
}

char winner() {
    for (int i=0; i<N; i++) {
        for (int j=0; j<N; j++) {
            if (i+4 <= N) {
                if (board[i][j] == board[i+1][j] &&
                    board[i][j] == board[i+2][j] &&
                    board[i][j] == board[i+3][j])
                    return board[i][j];
            }
            if (j+4 <= N) {
                if (board[i][j] == board[i][j+1] &&
                    board[i][j] == board[i][j+2] &&
                    board[i][j] == board[i][j+3])
                    return board[i][j];
            }
            if (i+4 <= N && j+4 <= N) {
                if (board[i][j] == board[i+1][j+1] &&
                    board[i][j] == board[i+2][j+2] &&
                    board[i][j] == board[i+3][j+3])
                    return board[i][j];
            }
        }
    }
}

char play_move(char *movefilename) {
    FILE *movefile = fopen(movefilename, "r");
    int move;
    fscanf(movefile, "%d", &move);
    fprintf(logfile, "Move %d: %d\n", ++moves, move);
    if (move < 1 || move > N) return 1;
    int row = 0;
    while (board[row][move] == '.' && row+1 <= N) row++;
    row--;
    if (row == -1) return 1;
    if (moves%2) {
        board[row][move] = '1';
    } else {
        board[row][move] = '2';
    }
    encode_state();
    fprintf(logfile, "The current state:\n%s\n", state);
    return 0;
}
#endif
