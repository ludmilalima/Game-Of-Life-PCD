#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <pthread.h>
#include <sys/time.h>

#define SRAND_VALUE 1985
#define dim 2048
#define lifeCycles 2001

int **grid;
int **newgrid;

typedef struct threadArg_t{
    int start;
    int end;
}threadArg_t;

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

void *setNextGen(void *arg){
    threadArg_t *t = (threadArg_t *) arg;

    for(int i = t->start; i < t->end; i++){
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

        //2 threads
        // pthread_t tp1, tp2;
        // threadArg_t t1, t2;
        // t1.start = 0; t1.end = dim/2;
        // t2.start = dim/2; t2.end = dim;
        // pthread_create(&tp1, NULL, setNextGen, &t1);
        // pthread_create(&tp2, NULL, setNextGen, &t2);
        // pthread_join(tp1, NULL);
        // pthread_join(tp2, NULL);

        //4 threads
        pthread_t tp1, tp2, tp3, tp4;
        threadArg_t t1, t2, t3, t4;
        t1.start = 0; t1.end = dim/4;
        t2.start = dim/4; t2.end = 2*dim/4;
        t3.start = 2*dim/4; t3.end = 3*dim/4;
        t4.start = 3*dim/4; t4.end = dim;
        pthread_create(&tp1, NULL, setNextGen, &t1);
        pthread_create(&tp2, NULL, setNextGen, &t2);
        pthread_create(&tp3, NULL, setNextGen, &t3);
        pthread_create(&tp4, NULL, setNextGen, &t4);
        pthread_join(tp1, NULL);
        pthread_join(tp2, NULL);
        pthread_join(tp3, NULL);
        pthread_join(tp4, NULL);

        //8 threads
        // pthread_t tp1, tp2, tp3, tp4, tp5, tp6, tp7, tp8;
        // threadArg_t t1, t2, t3, t4, t5, t6, t7, t8;
        // t1.start = 0; t1.end = dim/8;
        // t2.start = dim/8; t2.end = 2*dim/8;
        // t3.start = 2*dim/8; t3.end = 3*dim/8;
        // t4.start = 3*dim/8; t8.end = 4*dim/8;
        // t5.start = 4*dim/8; t5.end = 5*dim/8;
        // t6.start = 5*dim/8; t6.end = 6*dim/8;
        // t7.start = 6*dim/8; t7.end = 7*dim/8;
        // t8.start = 7*dim/8; t8.end = dim;
        // pthread_create(&tp1, NULL, setNextGen, &t1);
        // pthread_create(&tp2, NULL, setNextGen, &t2);
        // pthread_create(&tp3, NULL, setNextGen, &t3);
        // pthread_create(&tp4, NULL, setNextGen, &t4);
        // pthread_create(&tp5, NULL, setNextGen, &t5);
        // pthread_create(&tp6, NULL, setNextGen, &t6);
        // pthread_create(&tp7, NULL, setNextGen, &t7);
        // pthread_create(&tp8, NULL, setNextGen, &t8);
        // pthread_join(tp1, NULL);
        // pthread_join(tp2, NULL);
        // pthread_join(tp3, NULL);
        // pthread_join(tp4, NULL);
        // pthread_join(tp5, NULL);
        // pthread_join(tp6, NULL);
        // pthread_join(tp7, NULL);
        // pthread_join(tp8, NULL);

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

    double totalTime;

    totalTime = runLife();

    printf("\nPthread");
    printf("\nNumero de Threads: 4");
    printf("\nTempo: %.2f\n", totalTime/1000);

    return 0;
}