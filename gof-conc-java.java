import java.util.*;
import java.lang.Thread;

public class gameOfLife extends Thread{
    private static final int dim = 2048;
    private static final int lifeCycles = 2001;
    private static final int SRAND_VALUE = 1985;

    private volatile static int[][] grid; 
    private volatile static int[][] newgrid;
    private int forStart;
    private int forLimit;

    public gameOfLife(int forStart, int forLimit){
        this.forStart = forStart;
        this.forLimit = forLimit;
    }

    public void setForStart(int data){
        this.forStart = data;
    }

    public int getForStart(){
        return this.forStart;
    }

    public void setForLimit(int data){
        this.forLimit = data;
    }

    public int getForLimit(){
        return this.forLimit;
    }

    public void run(){
        for(int i = this.getForStart(); i < this.getForLimit(); i++){
            for(int j = 0; j < dim; j++){
                if(this.grid[i][j] == 1){
                    if(this.getNeighbors(i, j) >= 2 && this.getNeighbors(i, j) <= 3){
                        this.newgrid[i][j] = 1;
                    }else{
                        this.newgrid[i][j] = 0;
                    }
                }else{
                    if(getNeighbors(i, j) == 3){
                        this.newgrid[i][j] = 1;
                    }
                }
            }
        }

        
    }

    public int getNeighbors(int i, int j){
        int neighborNum = 0, z, w, aux;

        aux = this.grid[i][j];
        this.grid[i][j] = 0;
    
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
    
                neighborNum += this.grid[z][w];
            }
        }
    
        this.grid[i][j] = aux;
    
        return neighborNum;
    } 
    
    public int retCellNum(){
        int cellNum = 0;
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                cellNum += this.grid[i][j];
            }
        }
    
        return cellNum;
    }

    public static void main(String[] args) {
        Random gerador = new Random(SRAND_VALUE);
 
        grid = new int[dim][dim];
        newgrid = new int[dim][dim];

        for(int i = 0; i < dim; i++) { //laço sobre as células do tabuleiro sem contar com um eventual halo
            for(int j = 0; j < dim; j++) {
                grid[i][j] = gerador.nextInt(2147483647) % 2;
            }
        }

        double startTime, endTime, totalTime = 0;

        for(int i = 0; i < lifeCycles; i++){
        
            // gameOfLife gof[] = {
            //     new gameOfLife(0, dim)
            // };
            // gameOfLife gof[] = {
            //     new gameOfLife(0, dim/2),
            //     new gameOfLife(dim/2, dim)
            // };
            // gameOfLife gof[] = {
            //     new gameOfLife(0, dim/4),
            //     new gameOfLife(dim/4, 2*dim/4),
            //     new gameOfLife(2*dim/4, 3*dim/4),
            //     new gameOfLife(3*dim/4, dim)
            // };
            gameOfLife gof[] = {
                new gameOfLife(0, dim/8),
                new gameOfLife(dim/8, 2*dim/8),
                new gameOfLife(2*dim/8, 3*dim/8),
                new gameOfLife(3*dim/8, 4*dim/8),
                new gameOfLife(4*dim/8, 5*dim/8),
                new gameOfLife(5*dim/8, 6*dim/8),
                new gameOfLife(6*dim/8, 7*dim/8),
                new gameOfLife(7*dim/8, dim)
            };

            startTime = System.currentTimeMillis();

            for(gameOfLife g : gof){
                g.start();
            }

            for(gameOfLife g : gof){
                try{
                    g.join();
                }catch(InterruptedException e){
                    System.out.println("Exception!");
                }
            }
    
            endTime = System.currentTimeMillis();

            totalTime += (endTime-startTime);

            System.out.println("Geracao " + i + ": " + gof[0].retCellNum());
            
            for(int w = 0; w < dim; w++){
                for(int z = 0; z < dim; z++){
                    grid[w][z] = newgrid[w][z];
                }
            }
        }

        System.out.println("Tempo: " + (totalTime/1000));

    }
}