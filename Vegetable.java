
package ce326.hw2;

public class Vegetable extends BoardElement implements Eatable{
    private final char symbol;
    private final int energy;
    
    public Vegetable(){
        this.symbol = 'v';
        this.energy = this.eaten();
    }
    
    @Override
    public final int eaten(){
        return 6;
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    } 
}
