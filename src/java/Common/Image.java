/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.math.BigInteger;

/**
 *
 * @author longbow
 */
public class Image {
    private BigInteger ID;
    private String path;
    
    Image(BigInteger ID , String path)
    {
        this.ID=ID;
        this.path=path;
    }
    
    Image(String path)
    {
        this.ID=BigInteger.valueOf(-1);
        this.path=path;
    }
    
}
