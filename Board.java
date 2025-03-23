
package ce326.hw2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Board{
    int rows;
    int columns;
    int energy;
    BoardCell board[][];

    // Create the Board
    public Board(int rows,int columns, String Element ,int energy){
        this.rows  = rows;
        this.columns = columns;
        this.board = new BoardCell[rows][columns];
        this.energy = energy;
        
        int pos = 0;
        
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                char value = Element.charAt(pos);
                pos++;
                switch (value) {
                    case 'X':{
                            Player newElement = new Player(energy);
                            board[i][j] = new BoardCell(newElement, true);
                            newElement.row = i;
                            newElement.column = j;
                            break;
                        }
                    case '@':{
                            Ghost newElement = new Ghost();
                            board[i][j] = new BoardCell(newElement, false);
                            newElement.row = i;
                            newElement.column = j;
                            break;
                        }
                    case '#':{
                            Obstacle newElement = new Obstacle();
                            board[i][j] = new BoardCell(newElement, true);
                            newElement.row = i;
                            newElement.column = j;
                            break;
                        }
                    case ' ':{
                            board[i][j] = new BoardCell(null, true);
                            break;
                        }
                    case '-': {
                        board[i][j] = new BoardCell(null, false);
                        break;
                        }
                    case 'v':{
                        Vegetable newElement = new Vegetable();
                        board[i][j] = new BoardCell(newElement, false);
                        break;
                        }
                    case 'f' :{
                        Fish newElement = new Fish();
                        board[i][j] = new BoardCell(newElement, false);
                        break;
                        }
                    default:{
                        Meat newElement = new Meat();
                        board[i][j] = new BoardCell(newElement, false);
                        break;
                    }
                }
            }
        }  
    }

    public void PrintBoard(Board board){

        System.out.printf("  ");
        for(int i = 1; i <= board.columns; i++ ){
            System.out.printf(" %d ",i);
        }
        System.out.println(" ");
        for(int i = 0; i < board.rows; i++){
            char letter = (char) (i + 'A');
            System.out.printf("%c ",letter);
            for(int j = 0; j < board.columns; j++){
                System.out.printf(" %c ",board.board[i][j].getSymbol());
            }
            System.out.println();
        }
        System.out.printf("  ");
        for(int i = 1; i <= board.columns; i++ ){
            System.out.printf(" %d ",i);
        }
    }
    
    public List <BoardCell> findElement(Board board, char symbol){
        List<BoardCell> returnCells = new ArrayList<>();
        
        for(int i = 0; i < board.rows; i++){
            for(int j = 0; j < board.columns; j++){
                if(board.board[i][j].getSymbol() == symbol){
                    returnCells.add(board.board[i][j]);
                }
            }
        }
        return returnCells;
    }
    // Move the table, print the appropriate messages and return the player's moves
    public String MovePlayer(Board board, String move, List<String> movesPlayer){
        int[] newMove = new int[2];
        
        if(move.length() <= 1 || move.length() > 3){
            System.out.println("Invalid input. Try again...");
            System.out.println();
            return "-1";
        }
        String numbers = "123456789";
        // Knowing that the max number of rows and columns is 20
        if(!(numbers.contains(move.substring(1,2)))){
            System.out.println("Invalid input. Try again...\n");
            return "-1";
        }
        if(move.length() ==  3 && !(numbers.contains(move.substring(2,3)))){
            System.out.println("Invalid input. Try again...\n");
            return "-1";
        }
        newMove[0] = move.charAt(0) - 'A';
        newMove[1] = Integer.parseInt(move.substring(1)) - 1 ;
        
        List<BoardCell> Cell = findElement(board,'X');
        
        Player player = (Player) Cell.get(0).elements[1];
        String firstMove = String.valueOf((char) (player.row + 'A')) + (String) String.valueOf(player.column + 1);
        
        List<int[]> possibleMoves = player.moveOptions(board);

        for(int[] pos: possibleMoves){
            if(Arrays.equals(pos, newMove)){
                
                if(board.board[pos[0]][pos[1]].getSymbol() == '@'){
                    System.out.println("YOU LOST");
                    System.out.println();
                    return null;
                }
                // There is food
                else if(board.board[pos[0]][pos[1]].elements[0] != null){
                        int newEnergy = board.board[pos[0]][pos[1]].eaten();
                        player.energy = player.energy + newEnergy ;
                        board.board[pos[0]][pos[1]].elements[0] = null; 
                }
                // The player doesn't move, so his energy is not consumed
                if(board.board[pos[0]][pos[1]].getSymbol() == 'X'){
                    return move;
                }
                // The players moves
                board.board[pos[0]][pos[1]].elements[1] = player;
                player.row = pos[0];
                player.column = pos[1];
                player.energy = player.energy - 1;

                if(player.energy == 0){
                    System.out.println("YOU LOST");
                    System.out.println();
                    return null;
                }
                movesPlayer.add(firstMove);
                movesPlayer.add(move);
             
                board.board[pos[0]][pos[1]].visited = true;
                Cell.get(0).elements[1] = null;
                return move;
            }          
        }
        System.out.println("Invalid input. Try again...");
        System.out.println();
        return "-1";
    }
    // Move the ghosts to the positions with the shortest distance from the player
    public boolean moveGhosts(Board board, DistBoard distBoard, List<String> movesGhost){
        List <BoardCell> cell = board.findElement(board, '@');

        for(BoardCell pos: cell){
            Ghost ghost = (Ghost) pos.elements[1];
            List<int[]> possibleMoves = ghost.moveOptions(board);
            int[] bestMove = null;
            
            for(int[] move: possibleMoves){
                if(bestMove == null)
                    bestMove = move;
                
                if(board.board[move[0]][move[1]].getSymbol() == 'X'){
                    System.out.println("YOU LOST");
                    System.out.println();
                    return false;
                }
                if(Integer.parseInt(distBoard.distBoard[bestMove[0]][bestMove[1]]) > Integer.parseInt(distBoard.distBoard[move[0]][move[1]]))
                    bestMove = move;

            }
            String moves = String.valueOf((char) (ghost.row + 'A')) + (String) String.valueOf(ghost.column + 1);
            movesGhost.add(moves);
            // The ghosts move
            ghost.row = bestMove[0];
            ghost.column = bestMove[1];
            board.board[bestMove[0]][bestMove[1]].elements[1] = ghost;
            
            moves = String.valueOf((char) (bestMove[0] + 'A')) +  (String) String.valueOf(bestMove[1] + 1);
            movesGhost.add(moves);
            
            pos.elements[1] = null;
        }
        return true;
    }
    
    public boolean checkWin(Board board){
        for(int i = 0; i < board.rows; i++){
            for(int j = 0; j < board.columns; j++){
                if(!board.board[i][j].visited){
                    return false;
                }
            }
        }
        return true;
    }
    // Save the information for the present stage of the game 
    public JSONObject saveBoard(Board board){
       JSONObject stepObj = new JSONObject(); 
       List<Character> firstLayer = new ArrayList<>();
       List<Character> secondLayer = new ArrayList<>();
       List<Boolean> visited = new ArrayList<>();

       for(int i = 0; i < board.rows; i++){
            for(int j = 0; j < board.columns; j++){
                visited.add(board.board[i][j].visited);
                
                if(board.board[i][j].getSymbol() == '@'){
                    
                    firstLayer.add(board.board[i][j].getSymbol());

                    if(board.board[i][j].elements[0] == null)
                        secondLayer.add(null);
                    
                    else
                        secondLayer.add(board.board[i][j].elements[0].getSymbol());    
                }
                else{
                    firstLayer.add(board.board[i][j].getSymbol());
                    secondLayer.add(null);
                }
                if(board.board[i][j].getSymbol() == 'X'){
                    Player player = (Player) board.board[i][j].elements[1];
                    stepObj.put("energy", player.energy);   
                }
            }
        } 
       stepObj.put("firstLayer", firstLayer);
       stepObj.put("secondLayer", secondLayer);
       stepObj.put("visited", visited);

       return stepObj;
    }
    // Load the Board using the information taken from the JSON file with the steps
    public Board loadBoard(JSONArray firstLayer, List<Boolean> visited,JSONArray secondLayer, int energy){
        String letters = "";
        int i = 0;
        
        for(Object element: firstLayer){
            char character = (char) element;
            letters += character;
            i++;    
        }
        Board newBoard = new Board(this.rows,this.columns,letters,energy);
        
        int pos = 0;
        for(i = 0; i < newBoard.rows; i++){
            for(int j = 0; j < newBoard.columns; j++){
                
                if(!secondLayer.isNull(pos)){

                    if(secondLayer.get(pos).equals('v')){
                        Vegetable newElement = new Vegetable();
                        newBoard.board[i][j].elements[0] = newElement;
                    } 
                    else if(secondLayer.get(pos).equals('f')){
                        Fish newElement = new Fish();
                        newBoard.board[i][j].elements[0] = newElement; 
                    }
                    else{
                        Meat newElement = new Meat();
                        newBoard.board[i][j].elements[0] = newElement;
                    }
                }
                newBoard.board[i][j].visited = visited.get(pos);
                pos++;
            }
        }
        return newBoard;
    }
}
    

        
        
    
    
    
    
         
    
    
    
    
