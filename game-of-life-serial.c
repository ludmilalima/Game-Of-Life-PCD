#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define SRAND_VALUE 1985
#define dim 2048

int grid[dim][dim];

int getNeighbors(int **grid, int i, int j){
    
}

int main(){
    srand(SRAND_VALUE);
    
    for(int i = 0; i < dim; i++){
        for(int j = 0; j < dim; j++){
            grid[i][j] = rand() % 2;
        }
    }

    return 0;
}