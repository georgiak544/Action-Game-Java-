package ce326.hw2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DistBoard {
    
    int rows;
    int columns;
    String[][] distBoard;
    
    public DistBoard(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.distBoard = new String[rows][columns];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.distBoard[i][j] = String.valueOf(Integer.MAX_VALUE);
                
            }
        }
    }
    
    // Print the board with the distances
    public void printDistances(DistBoard distBoard){
        System.out.printf("  ");
        for(int i = 1; i <= distBoard.columns; i++ )
            System.out.printf(" %d ",i);
        
        System.out.println(" ");
        for(int i = 0; i < distBoard.rows; i++){
            
            char letter = (char) (i + 'A');
            System.out.printf("%c ",letter);
            
            for(int j = 0; j < distBoard.columns; j++){
                if(distBoard.distBoard[i][j].equals(String.valueOf(Integer.MAX_VALUE))){
                    System.out.printf(" ? ");
                }
                else{
                    System.out.printf(" %s ",distBoard.distBoard[i][j]); 
                } 
            }
            System.out.println();
        }
        for(int i = 1; i <= distBoard.columns; i++ ){
            System.out.printf(" %d ",i);
        }
    }

    // Find the positions that an element can go and put the obstacles
    public List<int[]> findMoves(int[] dis, Board board, DistBoard disBoard) {
        List<int[]> possibleMoves = new ArrayList<>();
       
        int[] CorrectRows = CorrectPositions(dis[0], board.rows);
        int[] CorrectCol= CorrectPositions(dis[1], board.columns);

        for(int r: CorrectRows){
            if(board.board[r][dis[1]].getSymbol() != '#'){
                possibleMoves.add(new int[]{r,dis[1]});
            }
            else if(board.board[r][dis[1]].getSymbol() == '#'){
                disBoard.distBoard[r][dis[1]] = "#";
            }     
        }
        for(int c: CorrectCol){
            if(dis[1]!= c && (board.board[dis[0]][c].getSymbol() != '#')){
                possibleMoves.add(new int[]{dis[0],c});
            }
            else if(board.board[dis[0]][c].getSymbol() == '#'){
                disBoard.distBoard[dis[0]][c] = "#";
            }
                
        }
        return possibleMoves;
    }
    
    public int[] CorrectPositions(int pos, int maxPos){
        int[] retPos = new int[3];
        int i = 0;
        if(pos + 1 == maxPos){
            retPos[i] = 0;
            i++;
        }
        else{
            retPos[i] = pos + 1;
            i++;
        }
        if(pos - 1 < 0){
            retPos[i] = maxPos-1;
            i++;
        }
        else{
            retPos[i] = pos - 1;
            i++;
        }
        retPos[i] = pos;
        return retPos;
    }
    
    // Calculate the distance from the player to every cell on the board
    public void findDistances(int[] pos,DistBoard distboard, Board board ){
   
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{pos[0], pos[1]});
        distboard.distBoard[pos[0]][pos[1]] = 0 + "";
                
         while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            List<int[]> neighbors = findMoves(new int[]{x, y}, board, distboard);
            
            for (int[] neighbor : neighbors) {
                int newX = neighbor[0];
                int newY = neighbor[1];

                if (!"#".equals(distboard.distBoard[newX][newY]) && (Integer.parseInt(distboard.distBoard[newX][newY]) > Integer.parseInt(distboard.distBoard[x][y]))){ // If not visited yet
                    distboard.distBoard[newX][newY] = Integer.parseInt(distboard.distBoard[x][y]) + 1 + "";
                    queue.offer(new int[]{newX, newY});
                }
            }
        }       
    }
}
    
   
