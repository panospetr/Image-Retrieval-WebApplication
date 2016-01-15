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
public class ImageFeatureVector {
    private BigInteger id;
    private double[] colorhistogram;
    private double[] texturehistogram;
    private double[] shapehistogram;
    
    
    
   public ImageFeatureVector(BigInteger id,double[] colorhistogram, double[] texturehistogram,double[] shapehistogram)
    {   this.id=id;
        this.colorhistogram=colorhistogram;
        this.texturehistogram=texturehistogram;
        this.shapehistogram=shapehistogram;
    }
    
  public  ImageFeatureVector(double[] colorhistogram, double[] texturehistogram,double[] shapehistogram)
    {   this.id=BigInteger.valueOf(-1);
        this.colorhistogram=colorhistogram;
        this.texturehistogram=texturehistogram;
        this.shapehistogram=shapehistogram;
    }
    
    public double[] getColorHistogram()
    {
        return this.colorhistogram;
    }
    public double[] getTextureHistogram()
    {
        return this.texturehistogram;
    }
    public double[] getShapeHistrogram()
    {
        return this.shapehistogram;
    }
    
  
  
}
