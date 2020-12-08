import java.util.*;
import java.lang.*;
import java.util.concurrent.*;

public class gofConcJava implements Runnable{
    private static final int dim = 2048;
    private static final int lifeCycles = 2001;
    private static final int SRAND_VALUE = 1985;
    private static final int threadNum = 8;
    private static CyclicBarrier barrier;

    class Shared{
        public volatile int[][] grid, newgrid;
        public volatile int[] currCell;
        public volatile int currCycle;

        Shared(){
            grid = new int[dim][dim];
            newgrid = new int[dim][dim];
            currCell = new int [threadNum];
        }
    }

    Shared shared = new Shared();

    private static int start, end, tID;

    public gofConcJava(int start, int end, int tID){
        this.start = start;
        this.end = end;
        this.tID = tID;
    }

    public void run(){
        for(int i = 0; i < lifeCycles; i++){
            shared.currCell[tID] = 0;
            for(int j = start; j < end; j++){
                for(int k = 0; k < dim; k++){
                    if(shared.grid[j][k] == 1){
                        if(getNeighbors(j, k) == 2 || getNeighbors(j, k) == 3){
                            shared.newgrid[j][k] = 1;
                            shared.currCell[tID]++;
                        }else{
                            shared.newgrid[j][k] = 0;
                        }
                    }else{
                        if(getNeighbors(j, k) == 3){
                            shared.newgrid[j][k] = 1;
                            shared.currCell[tID]++;
                        }else{
                            shared.newgrid[j][k] = 0;
                        }
                    }
                }
            }

            shared.currCycle = i;
            try{
                barrier.await();
            }catch(InterruptedException e){
                System.out.println("!");
                return;
            }catch(BrokenBarrierException ex){
                System.out.println("!");
                return;
            }

        }

    }

    public int getNeighbors(int i, int j){
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
                    neighborNum += shared.grid[z][w];
                }
                
            }
        }
    
        return neighborNum;
    }

    public static void main(String[] args) {
        // shared.grid = new int[dim][dim];
        // shared.newgrid = new int[dim][dim];
        // shared.currCell = new int[threadNum];

        Random gerador = new Random(SRAND_VALUE);
 
        for(int i = 0; i<dim; i++) { //laço sobre as células do tabuleiro sem contar com um eventual halo
            for(int j = 0; j<dim; j++) {
                shared.grid[i][j] = gerador.nextInt(2147483647) % 2;
                // shared.currCell += shared.grid[i][j];
            }
        }
        // System.out.println("Geracao 0: " + shared.currCell);

        Runnable barrierAction = new Runnable() {
            public void run(){
                // shared.currCell = 0;
                // for(int i = 0; i < dim; i++){
                //     for(int j = 0; j < dim; j++){
                //         shared.grid[i][j] = shared.newgrid[i][j];
                //         shared.currCell += shared.grid[i][j];
                //     }
                // }
                // Thread currThread = Thread.currentThread();
                // System.out.println("Geracao " + (shared.currCycle+1) + ": " + shared.currCell);
                for(int i = 0; i < threadNum; i++){
                    System.out.println(shared.currCell[i]);
                }
            }
        };

        barrier = new CyclicBarrier(threadNum, barrierAction);

        List<Thread> threads = new ArrayList<Thread>(threadNum);

        for(int i = 0; i < threadNum; i++){
            Thread thread = new Thread(new gofConcJava((dim/threadNum)*i, (dim/threadNum)*(i+1), i));
            threads.add(thread);
        }

        for(Thread t : threads){
            t.start();
        }

        for(Thread t: threads){
            try{
                t.join();
            }catch(InterruptedException e){
                
            }
        }
    }

}