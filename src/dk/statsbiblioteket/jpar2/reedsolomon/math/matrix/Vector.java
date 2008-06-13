package dk.statsbiblioteket.jpar2.reedsolomon.math.matrix;

/**
 *
 * @author abr
 */
public class Vector<N extends Number> {

    Object[] vector;

    public Vector(Object[] vector) {
        this.vector = vector;
    }

    public void set(int index, N value){
        vector[index] = value;
    }
    
    @SuppressWarnings("unchecked")
    public N get(int index){
        return (N)vector[index];
    }

    public int length() {
        return vector.length;
    }
    
    
}
