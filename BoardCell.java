
package ce326.hw2;

class BoardCell{

    BoardElement[] elements;
    boolean visited;

    public BoardCell(BoardElement element,boolean visited){
       this.elements = new BoardElement[2];
      
        if(element != null){
            if(element.getSymbol() == 'X' || element.getSymbol() == '@'){
                this.elements[1] = element;
                this.elements[0] = null;
            }
            else{
                this.elements[0] = element;
                this.elements[1] = null;
            }
        }
        else{
            this.elements[0] = null;
            this.elements[1] = null;
        }
        
        this.visited = visited;
    }
    // Return the symbol of this cell 
    public char getSymbol(){
        if(this.elements[0] == null && this.elements[1] == null){
            if(this.visited){
                return ' ';
            }
            else{
                return '-';
            }
        }
        else if(this.elements[1] == null){
            if(this.elements[0] instanceof Obstacle){
                return '#';
            }
            else if(this.elements[0] instanceof Vegetable){
                return 'v';
            }
            else if(this.elements[0] instanceof Fish){
                return 'f';
            }
            else {
                return 'm';
            }
        }
        else{
            if(this.elements[1] instanceof Player){
                return 'X';
            }
            else if(this.elements[0] instanceof Obstacle){
                return '#';
            }
            else if(this.elements[1] instanceof Ghost){
                return '@';
            }
            
        }
        return ' ';

    }
    // Used only by foods
    public int eaten(){
        if(this.elements[0] instanceof Vegetable){
            Vegetable veg = (Vegetable) this.elements[0];
            return veg.eaten();
        }
        else if(this.elements[0] instanceof Fish){
            Fish fish = (Fish) this.elements[0];
            return fish.eaten();
        }
        else{
            Meat meat = (Meat) this.elements[0];
            return meat.eaten();
        }
    }   
}
