// Georgia Kostopoulou 03449
package ce326.hw2;

import java.io.FileReader;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Homework2 {
    // Print the appropriate menu
    public static void showMenu(boolean gameStarted, Scanner scanner) {
        System.out.println("- Load Game   (L/l)");
        if (gameStarted) {
            System.out.println("- Debug Dijkstra (D/d)");
            System.out.println("- Show History  (H/h)");
            System.out.println("- Jump to move  (J/j)");
            System.out.println("- Continue game (C/c)");
        }
        
        System.out.println("- Quit      (Q/q)\n");
        System.out.println("Your option:");
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        boolean start = false;
        String fileName;
        int rows = 0 ;
        int columns = 0 ;
        DistBoard distBoard = null;
        Board board = null;
        boolean distCheck = false;
        boolean lost;
        String move = "-1";
        JSONObject steps = new JSONObject();
        int num = 0;
        int energy;
        File file;         
        List<List<String>> moves  = new ArrayList<>();

        System.out.println("- Load Game      (L/l)");
        System.out.println("- Quit           (Q/q)\n");
        System.out.println("Your option:");
        Scanner scanner = new Scanner(System.in);
         
        while(true){

            String input = scanner.nextLine();
            
            START:
            if(input.equals("l") || input.equals("L") || (input.equals("C") || input.equals("c") && start) 
                || (input.equals("J") || input.equals("j") && start)){

                if(input.equals("l") || input.equals("L")){
                    num = 0;
                    moves = new ArrayList<>();
                    // Problems reading the file
                    while(true){
                        
                        System.out.println("Enter input filename: ");
                        fileName = scanner.nextLine();
                        file = new File(fileName);
                        
                        if(!file.exists() || !file.canRead()){
                            System.out.printf("Unable to read %s\n\n",file.getName());
                        }
                        else if(!fileName.endsWith(".json")){
                            start = false;
                            System.out.println("File NOT in JSON format\n");
                            showMenu(start,scanner);
                            break START;
                        }
                        else{
                            break;
                        }
                    }
                    // Read the file 
                    FileReader reader = new FileReader(fileName);     
                    JSONTokener tokener = new JSONTokener(reader);
                    JSONObject data = new JSONObject(tokener);
                    
                    rows = data.getInt("rows");
                    columns = data.getInt("columns");
                    energy = data.getInt("energy");
                    JSONArray init = data.getJSONArray("init");
                    
                    // Illegal arguments
                    if(energy <= 0 || rows <= 0 || columns <= 0 || init.length() < (rows*columns)){
                        System.out.printf("Illegal arguments in %s\n\n",file.getName());
                        start = false;
                        showMenu(start,scanner);
                        break START;
                    }
                    // Take the elements for the board
                    String letters = "";
                    int i = 0;
                    for(Object element: init){
                        String character = (String) element;
                        letters += character;
                        i++;    
                    }
                    // Create the board
                    board = new Board(rows,columns,letters,energy);
                    
                    // Save the board (as the 1st step of the game)
                    JSONObject step = board.saveBoard(board);
                    steps.put(Integer.toString(num), step);
                    start = true;
                }
                // Jump move
                if(input.equals("J") || input.equals("j")){
                    
                    System.out.println("Enter move number: ");
                    String numStep = scanner.nextLine();
                    
                    int number = Integer.parseInt(numStep);
                    
                    int savedNumber = number;
                    if(number % 2 != 0)
                        number--;
                    
                    number = number / 2;
                    numStep = number + "";
                    
                    try {
                        
                        if(!steps.has(numStep)){
                            System.out.println("Wrong number\n");
                            showMenu(start,scanner);
                            break START;
                        }
                        num = savedNumber - 1;
                        // Delete the non-needed information for the files
                        while(steps.has(number + 1 + "")){
                            steps.remove(number + 1 + "");
                            number++;
                        }
                        try (FileWriter File = new FileWriter("steps.json")){
                            File.write(steps.toString());
                            File.flush();
                    
                        }
                        if(savedNumber == 0 || savedNumber == 1){
                            moves.clear();
                        }
                        else{
                            if(savedNumber % 2 != 0)
                                savedNumber--;

                            while(savedNumber < moves.size()){
                                moves.remove(savedNumber);

                            }
                        }
                        // Take the information for this step of the game 
                        JSONObject stepObject = steps.getJSONObject(numStep);

                        energy = stepObject.getInt("energy");
                        JSONArray firstLayer = stepObject.getJSONArray("firstLayer");
                        JSONArray visited = stepObject.getJSONArray("visited");
                        JSONArray secondLayer = stepObject.getJSONArray("secondLayer");
    
                        List<Boolean> booleanList = new ArrayList<>();
                    
                        for(int i = 0; i < visited.length(); i++)
                            booleanList.add(visited.getBoolean(i));  
                        
                        // Load the new Board
                        board = board.loadBoard(firstLayer, booleanList, secondLayer, energy);
                       
                    } catch (IOException e) {}
                }
                if(!start){
                    System.out.println("GAME OVER\n");
                    showMenu(start,scanner);
                     break START;
                }
                if(distCheck){
                    if(distBoard == null){
                        distBoard = new DistBoard(rows,columns);
                        List <BoardCell> cell = board.findElement(board, 'X');
                                
                        int[] starter = new int [2];
                        starter[0] = cell.get(0).elements[1].row;
                        starter[1] = cell.get(0).elements[1].column;
                                
                        distBoard.findDistances(starter,distBoard,board);
                    }
                    
                    distBoard.printDistances(distBoard);
                    System.out.println();
                }
                board.PrintBoard(board);
                System.out.println();
  
                while(move != null ){
                    List<String> player = new ArrayList<>();
                    List<String> ghost = new ArrayList<>();
                        
                    // The player gives the move
                    String playerMove = scanner.nextLine();
                    // The game stops
                    if(playerMove.equals("z") || playerMove.equals("Z")){
                        showMenu(start,scanner);
                        break START;
                    }
                    move = board.MovePlayer(board, playerMove,player);
                        
                    // The player lost
                    if(move == null){
                        start = false;
                        showMenu(start,scanner);
                        break START;
                    }
                    // Invalid input
                    if("-1".equals(move)){
                        continue;
                    }
                    // Valid input
                    moves.add(player);
                        
                    boolean win = board.checkWin(board);
                    if(win){
                        System.out.println("YOU WIN\n");
                        start = false;
                        showMenu(false,scanner);
                        break START;
                    }
                            
                    // Calculate distancces
                    distBoard = new DistBoard(rows,columns);
                    List <BoardCell> cell = board.findElement(board, 'X');
                                
                    int[] starter = new int [2];
                    starter[0] = cell.get(0).elements[1].row;
                    starter[1] = cell.get(0).elements[1].column;
                                
                    distBoard.findDistances(starter,distBoard,board);

                    // The Ghosts move
                    lost = board.moveGhosts(board,distBoard,ghost);
                    // The player lost
                    if(!lost){
                        start = false;
                        showMenu(start,scanner);
                        break START; 
                    }
                    // Print the board with distances
                    if(distCheck){
                        distBoard.printDistances(distBoard);
                        System.out.println();
                    }
                    // Save the board
                    num++;  
                    moves.add(ghost);
                    JSONObject step = board.saveBoard(board);
                    steps.put(Integer.toString(num), step);
                        
                    board.PrintBoard(board);
                    System.out.println();
                }
            }  
            else if((input.equals("d") || input.equals("D")) && start){
                distCheck = !distCheck;
                showMenu(start,scanner);
                break START;
            }
            // Print the history of ghost and player movements
            else if((input.equals("H") || input.equals("h")) && start){
                JSONObject movesObj = new JSONObject();
                
                for (int i = 0; i < moves.size(); i++) {
                    List<String> innerList = moves.get(i);
                    String array = "";
                    for (int j = 0; j < innerList.size(); j++) {
                        if(j % 2 != 0){
                            array = array + innerList.get(j) + ',';
                        }
                        else{
                            array = array + innerList.get(j) + '-';
                        } 
                    }
                    array = array.substring(0, array.length() - 1);
                    movesObj.put(Integer.toString(i), array);
                }
                JSONObject data = new JSONObject(); 
                data.put("moves", movesObj);
       
                // Write the file of the moves
                try (FileWriter File = new FileWriter("moves.json")) {
                    File.write(data.toString());
                    File.flush();
                } catch (IOException e) {}
                // Read the file of the moves
                try {
                    String jsonContent = new String(Files.readAllBytes(Paths.get("moves.json")));
            
                    JSONObject jsonObject = new JSONObject(jsonContent);
                    JSONObject movesObject = jsonObject.getJSONObject("moves");
                    
                    System.out.println("{");
                    System.out.println("  \"moves\": {");
                    JSONArray keys = movesObject.names();
                    if (keys != null) {
                        for (int i = 0; i < keys.length(); i++) {
                            String key = keys.getString(i);
                            String value = movesObject.getString(key);
                            System.out.println("  \"" + key + "\": \"" + value + "\",");
                        }
                    }
                    System.out.println("  }");
                    System.out.println("}");
                    } catch (IOException e) {}
                
                    showMenu(start,scanner);
                    break START;
            }
            // EXIT
            else if(input.equals("Q") || input.equals("q")){
                return;
            }
            else{
                System.out.println("Invalid option\n");
                showMenu(start,scanner);
                break START;
            
            }
        }
    }
}

    
           

    
    
   




