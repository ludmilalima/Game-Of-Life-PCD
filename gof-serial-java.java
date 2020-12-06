import java.util.*;

public class gameOfLife {
    private static final int dim = 2048;
    private static final int lifeCycles = 2001;
    private static final int SRAND_VALUE = 1985;

    private int[][] grid; 
    private int[][] newgrid;

    public gameOfLife(){
        this.grid = new int[dim][dim];
        this.newgrid = new int[dim][dim];
    }

    public void setGrid(int data, int i, int j){
        this.grid[i][j] = data;
    }

    public int[][] getGrid(){
        return this.grid;
    }

    public void setNewGrid(int data, int i, int j){
        this.newgrid[i][j] = data;
    }

    public int[][] getNewGrid(){
        return this.newgrid;
    }

    public int getNeighbors(int i, int j){
        int neighborNum = 0, z, w, aux;

        aux = this.getGrid()[i][j];
        this.setGrid(0, i, j);
    
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
    
                neighborNum += this.getGrid()[z][w];
            }
        }
    
        this.getGrid()[i][j] = aux;
    
        return neighborNum;
    }

    public void setNextGen(){
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                if(this.getGrid()[i][j] == 1){
                    if(this.getNeighbors(i, j) >= 2 && this.getNeighbors(i, j) <= 3){
                        this.setNewGrid(1, i, j);
                    }else{
                        this.setNewGrid(0, i, j);
                    }
                }else{
                    if(getNeighbors(i, j) == 3){
                        this.setNewGrid(1, i, j);
                    }
                }
            }
        }
    
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                this.setGrid(this.getNewGrid()[i][j], i, j);
            }
        }
    }    
    
    public int retCellNum(){
        int cellNum = 0;
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                cellNum += this.getGrid()[i][j];
            }
        }
    
        return cellNum;
    }

    public void runLife(){
        for(int i = 0; i < lifeCycles; i++){
            System.out.println("Geracao " + i + ": " + this.retCellNum());
    
            this.setNextGen();
        }
    }

    public static void main(String[] args) {
        Random gerador = new Random(SRAND_VALUE);

        gameOfLife gof = new gameOfLife();
 
        for(int i = 0; i < dim; i++) { //laço sobre as células do tabuleiro sem contar com um eventual halo
            for(int j = 0; j < dim; j++) {
                gof.setGrid(gerador.nextInt(2147483647) % 2, i, j);
            }
        }

        gof.runLife();

        // for(i = 0; i < dim; i++) { //laço sobre as células do tabuleiro sem contar com um eventual halo
        //     for(j = 0; j < dim; j++) {
        //         System.out.print("->" + gof.getGrid()[i][j] + "<-");
        //     }
        // }

    }
}