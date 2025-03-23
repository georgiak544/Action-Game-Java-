package ce326.hw2;

import java.util.ArrayList;
import java.util.List;

public class Ghost extends BoardElement implements Movable{
    private final char symbol;
    
    public Ghost(){
        this.symbol = '@';
    }
    
    @Override
    public char getSymbol(){
        return this.symbol;
    }
    
    @Override
    public List<int[]> moveOptions(Board board) {
        List<int[]> possibleMoves = new ArrayList<>();
       
        int[] CorrectRows = CorrectPositions(this.row, board.rows);
        int[] CorrectCol= CorrectPositions(this.column, board.columns);

        for(int rows: CorrectRows){
   
            if(board.board[rows][this.column].getSymbol() != '#' && board.board[rows][this.column].getSymbol() != '@')
                possibleMoves.add(new int[]{rows,this.column});      
        }
        for(int columns: CorrectCol){
       
            if(this.column != columns && (board.board[this.row][columns].getSymbol() != '#'))
                possibleMoves.add(new int[]{this.row,columns});
        }
        return possibleMoves; 
    }  
}
