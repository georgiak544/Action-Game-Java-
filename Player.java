package ce326.hw2;

import java.util.ArrayList;
import java.util.List;

public class Player extends BoardElement implements Movable{

    private final char symbol;
    int energy;

    public Player(int energy){
        this.symbol = 'X';
        this.energy = energy;
    }

    @Override
    public List<int[]> moveOptions(Board board) {
        List<int[]> possibleMoves = new ArrayList<>();

        int[] CorrectRows = CorrectPositions(this.row, board.rows);
        int[] CorrectCol= CorrectPositions(this.column, board.columns);

        for(int rows: CorrectRows){
            for(int columns: CorrectCol){
                
                if(board.board[rows][columns].getSymbol() != '#')
                    possibleMoves.add(new int[]{rows,columns});
            }
        }
        return possibleMoves; 
    }
    
    @Override
    public char getSymbol(){
        return this.symbol;
    }       
}
    

