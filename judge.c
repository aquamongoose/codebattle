#include <unistd.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include "game.h"

void play_game(char *player1, char *player2) {
    init_game(-1);
    while (!winner()) {
        FILE *infile = fopen("in.txt", "w");
        char *cmd = (char *)malloc(MAX_CMD_SIZE);
        char *wat = malloc(12);
        printf("\n\n\n\n\n\n\n\n\n");
        for (int i=0; i<N; i++) {
            printf("%d   ", i+1);
            if (i < 9) printf(" ");
        }
        printf("\n");
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                printf("%c    ", board[i][j]);
            }
            printf("\n\n");
        }
        for (int i=0; i<N; i++) {
            printf("%d   ", i+1);
            if (i < 9) printf(" ");
        }
        scanf("%s", &wat);
        printf("\n\n");
        fprintf(infile, "1\n");
        fprintf(infile, "%s\n", state);
        fclose(infile);
        sprintf(cmd, "timeout 2s %s < in.txt > out.txt", player1);
        if (system(cmd) != 0) {
            printf("%s took too much time, so %s wins by default.\n",
                    player1, player2);
            return;
        }
        if (play_move("out.txt")) {
            printf("%s played an illegal move, so %s wins by default.\n",
                    player1, player2);
            return;
        }
        printf("\n\n\n\n\n\n\n\n\n");
        for (int i=0; i<N; i++) {
            printf("%d   ", i+1);
            if (i < 9) printf(" ");
        }
        printf("\n");
        for (int i=0; i<N; i++) {
            for (int j=0; j<N; j++) {
                printf("%c    ", board[i][j]);
            }
            printf("\n\n");
        }
        for (int i=0; i<N; i++) {
            printf("%d   ", i+1);
            if (i < 9) printf(" ");
        }
        scanf("%s", &wat);
        if (winner()) break;
        infile = fopen("in.txt", "w");
        fprintf(infile, "2\n");
        fprintf(infile, "%s\n", state);
        fclose(infile);
        sprintf(cmd, "timeout 2s %s < in.txt > out.txt", player2);
        if (system(cmd) != 0) {
            printf("%s took too much time, so %s wins by default.\n",
                    player2, player1);
            return;
        }
        if (play_move("out.txt")) {
            printf("%s played an illegal move, so %s wins by default.\n",
                    player2, player1);
            return;
        }
        free(cmd);
    }
    if (winner() == 'T') {
        printf("The winner is... nobody. The game was a tie. Everyone loses.\n");
    } else {
        printf("The winner is %s!\n", winner() == '1' ? player1 : player2);
    }
    finalize_game();
}

int main(int argc, char **argv) {
    srand(time(NULL));
    if (argc != 3) {
        printf("You must call %s with exactly two arguments, the paths to the "
                "two executables that should play eachother.\n", argv[0]);
        return 1;
    }
    play_game(argv[1], argv[2]);
    return 0;
}
