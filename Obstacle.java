
package ce326.hw2;


public class Obstacle extends BoardElement{
    private final char symbol;
    
    public Obstacle(){
        this.symbol = '#';
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
    
}
