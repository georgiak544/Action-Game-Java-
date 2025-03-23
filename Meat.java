package ce326.hw2;


public class Meat extends BoardElement implements Eatable{
    private final char symbol;
    private final int energy;
    
    public Meat(){
        this.symbol = 'm';
        this.energy = this.eaten();
    }
    
    @Override
    public final int eaten(){
        return 14;
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
