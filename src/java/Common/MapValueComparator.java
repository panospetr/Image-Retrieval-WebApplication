/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author longbow
 */
public class MapValueComparator implements Comparator<BigInteger>{
      

    Map<BigInteger, Double> base;
    public MapValueComparator(Map<BigInteger, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    

   


    @Override
    public int compare(BigInteger a, BigInteger b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }

   
}

