#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define dim 2048
#define lifeCycles 2000
#define SRAND_VALUE 1985
#define threadNum 8

int **grid;
int **newgrid;
int *currCell;

typedef struct pth_args{
    int start;
    int end;
    int tID;
}pth_args;

pthread_barrier_t barrier;

int getNeighbors(int i, int j){
    int neighborNum = 0, z, w;

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
            if(a == i && b == j){

            }else{
                neighborNum += grid[z][w];
            }
            
        }
    }

    return neighborNum;
}

void *run(void *arg){
    pth_args *t = (pth_args *)arg;

    for(int i = 0; i < lifeCycles; i++){
        currCell[t->tID] = 0;
        for(int j = t->start; j < t->end; j++){
            for(int k = 0; k < dim; k++){
                if(grid[j][k] == 1){
                    if(getNeighbors(j, k) == 2 || getNeighbors(j, k) == 3){
                        newgrid[j][k] = 1;
                        currCell[t->tID]++;
                    }else{
                        newgrid[j][k] = 0;
                    }
                }else{
                    if(getNeighbors(j, k) == 3){
                        newgrid[j][k] = 1;
                        currCell[t->tID]++;
                    }else{
                        newgrid[j][k] = 0;
                    }
                }
            }
        }

        pthread_barrier_wait(&barrier);

        for(int z = t->start; z < t->end; z++){
            for(int w = 0; w < dim; w++){
                grid[z][w] = newgrid[z][w];
            }
        }

        if(t->tID == 0){
            for(int foakwe = 1; foakwe < threadNum; foakwe++){
                currCell[0]+=currCell[foakwe];
            }
            printf("Geracao %d: %d\n", i+1, currCell[0]);
        }

        pthread_barrier_wait(&barrier);
    }
}


int main(){
    pth_args *t;
    t = malloc(threadNum*sizeof(pth_args));

    srand(SRAND_VALUE);

    grid = malloc(dim*sizeof(int *));
    for(int i = 0; i < dim; i++){
        grid[i] = malloc(dim*sizeof(int));
    }

    newgrid = malloc(dim*sizeof(int *));
    for(int i = 0; i < dim; i++){
        newgrid[i] = malloc(dim*sizeof(int));
    }
    
    currCell = calloc(threadNum, sizeof(int));

    for(int i = 0; i < dim; i++){
        for(int j = 0; j < dim; j++){
            grid[i][j] = rand() % 2;
            currCell[0] += grid[i][j];
        }
    }

    printf("Geracao 0: %d\n", currCell[0]);
    currCell[0] = 0;

    for(int i = 0; i < threadNum; i++){
        t[i].start = (dim/threadNum)*i;
        t[i].end = (dim/threadNum)*(i+1);
        t[i].tID = i;
    }

    pthread_t thread[threadNum];

    struct timeval startTV, endTV;
    double totalTime;

    pthread_barrier_init(&barrier, NULL, threadNum);

    gettimeofday(&startTV, NULL);
    for(int i = 0; i < threadNum; i++){
        pthread_create(&thread[i], NULL, run, &t[i]);
    }

    for(int i = 0; i < threadNum; i++){
        pthread_join(thread[i], NULL);
    }
    gettimeofday(&endTV, NULL);

    totalTime = (endTV.tv_sec - startTV.tv_sec)*1000;
    totalTime += (endTV.tv_usec - startTV.tv_usec)/1000;

    printf("Tempo: %.2f\n", totalTime/1000);

    return 0;
}