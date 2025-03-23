
package ce326.hw2;

public class Fish extends BoardElement implements Eatable{
    private final char symbol;
    private final int energy;
    
    public Fish(){
        this.symbol = 'f';
        this.energy = this.eaten();
    }
    
    @Override
    public final int eaten(){
        return 10;
    }

    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
