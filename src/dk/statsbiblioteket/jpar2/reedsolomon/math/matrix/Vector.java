package dk.statsbiblioteket.jpar2.reedsolomon.math.matrix;

import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;

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
    
    
    public static  <N extends Number> N dot(Vector<N> a, Vector<N> b, Field<N> field){
        N product = field.ZERO;
        int length = a.length();
        
        for (int i=0;i<length;i++){
            product = field.add(product, field.mult(a.get(i), b.get(i)));
        }
        
        return product;
    }
    
}
