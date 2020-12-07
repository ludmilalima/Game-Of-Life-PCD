#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>

#define SRAND_VALUE 1985
#define dim 2048
#define lifeCycles 2001

int **grid;
int **newgrid;

int getNeighbors(int i, int j){
    int neighborNum = 0, z, w, aux;

    aux = grid[i][j];
    grid[i][j] = 0;

    for(int a = i-1; a <= i+1; a++){
        for(int b = j-1; b <= j+1; b++){
            if(a < 0){
                z = dim-1;
            }else if(a >= dim){
                z = 0;
            }else{
                z = a;
            }
            if(b < 0){
                w = dim-1;
            }else if(b >= dim){
                w = 0;
            }else{
                w = b;
            }

            neighborNum += grid[z][w];
        }
    }

    grid[i][j] = aux;

    return neighborNum;
}

void setNextGen(){
    for(int i = 0; i < dim; i++){
        for(int j = 0; j < dim; j++){
            if(grid[i][j] == 1){
                if(getNeighbors(i, j) >= 2 && getNeighbors(i, j) <= 3){
                    newgrid[i][j] = 1;
                }else{
                    newgrid[i][j] = 0;
                }
            }else{
                if(getNeighbors(i, j) == 3){
                    newgrid[i][j] = 1;
                }
            }
        }
    }

}

int retCellNum(){
    int cellNum = 0;
    for(int i = 0; i < dim; i++){
        for(int j = 0; j < dim; j++){
            cellNum += grid[i][j];
        }
    }

    return cellNum;
}

double runLife(){
    struct timeval startTV, endTV;

    double auxTime = 0, totalTime;

    for(int i = 0; i < lifeCycles; i++){
        printf("Geracao %d: %d\n", i, retCellNum(grid));

        gettimeofday(&startTV, NULL);
        setNextGen();
        gettimeofday(&endTV, NULL);

        totalTime = (endTV.tv_sec - startTV.tv_sec)*1000;
        totalTime += (endTV.tv_usec - startTV.tv_usec)/1000;

        auxTime += totalTime;

        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                grid[i][j] = newgrid[i][j];
            }
        }
    }

    return auxTime;
}

int main(){
    srand(SRAND_VALUE);

    double TV;

    grid = malloc(dim*sizeof(int *));
    for(int i = 0; i < dim; i++){
        grid[i] = malloc(dim*sizeof(int));
    }

    newgrid = malloc(dim*sizeof(int *));
    for(int i = 0; i < dim; i++){
        newgrid[i] = malloc(dim*sizeof(int));
    }
    
    for(int i = 0; i < dim; i++){
        for(int j = 0; j < dim; j++){
            grid[i][j] = rand() % 2;
        }
    }

    TV = runLife();

    printf("Tempo: %.2f\n", TV/1000);

    return 0;
}