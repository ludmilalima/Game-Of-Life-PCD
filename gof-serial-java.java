import java.util.*;

public class gameOfLife {
    private static final int dim = 2048;
    private static final int lifeCycles = 500;
    private static final int SRAND_VALUE = 1985;

    private static int[][] grid; 
    private static int[][] newgrid;

    public gameOfLife(){
        this.grid = new int[dim][dim];
        this.newgrid = new int[dim][dim];
    }

    public int getNeighbors(int i, int j){
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

    public void setNextGen(){
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(grid[i][j] == 1){
                    if(this.getNeighbors(i, j) >= 2 && this.getNeighbors(i, j) <= 3){
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
    
    public int retCellNum(){
        int cellNum = 0;
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                cellNum += grid[i][j];
            }
        }
    
        return cellNum;
    }

    public double runLife(){
        double startTime, endTime, totalTime = 0;
        
        for(int i = 0; i < lifeCycles; i++){
            System.out.println("Geracao " + i + ": " + this.retCellNum());
    
            startTime = System.currentTimeMillis();

            this.setNextGen();

            endTime = System.currentTimeMillis();

            totalTime += (endTime - startTime);

            for(int z = 0; z < dim; z++){
                for(int w = 0; w < dim; w++){
                    grid[z][w] = newgrid[z][w];
                }
            }
        }

        return totalTime;
    }

    public static void main(String[] args) {
        Random gerador = new Random(SRAND_VALUE);

        double totalTime;

        gameOfLife gof = new gameOfLife();
 
        for(int i = 0; i < dim; i++) { //laço sobre as células do tabuleiro sem contar com um eventual halo
            for(int j = 0; j < dim; j++) {
                grid[i][j] = gerador.nextInt(2147483647) % 2;
            }
        }

        totalTime = gof.runLife();

        System.out.println("Tempo: " + (totalTime/1000));

    }
}