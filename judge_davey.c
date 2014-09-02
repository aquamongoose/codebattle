
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include "game.h"

void play_game(char *program) {
    init_game();
    while (!winner()) {
        FILE *infile = fopen("in.txt", "w");
        char *cmd = (char *)malloc(MAX_CMD_SIZE);
        int dmove;
        FILE *dfile = fopen("out.txt", "w");
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
        printf("\n\n");
        scanf("%d", &dmove);
        fprintf(dfile, "%d\n", dmove);
        fclose(dfile);
        if (play_move("out.txt")) {
            printf("Davey played an illegal move, so %s wins by default.\n",
                    program);
            return;
        }
        fprintf(infile, "1\n");
        fprintf(infile, "%s\n", state);
        fclose(infile);
        sprintf(cmd, "timeout 2s %s < in.txt > out.txt", program);
        if (system(cmd) != 0) {
            printf("%s took too much time, so Davey wins by default.\n",
                    program);
            return;
        }
        if (play_move("out.txt")) {
            printf("%s played an illegal move, so Davey wins by default.\n",
                    program);
            return;
        }
    }
    if (winner() == 'T') {
        printf("The winner is... nobody. The game was a tie. Everyone loses.\n");
    } else {
        printf("The winner is %s!\n", winner() == '1' ? "Davey" : program);
    }
    finalize_game();
}

int main(int argc, char **argv) {
    if (argc != 2) {
        printf("You must call %s with exactly one arguments, the path to the "
               "executable that should play Davey.\n", argv[0]);
        return 1;
    }
    play_game(argv[1]);
    return 0;
}
