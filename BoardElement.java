
package ce326.hw2;

public abstract class BoardElement {
    int row;
    int column;

    public abstract char getSymbol();
    
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
}

    


