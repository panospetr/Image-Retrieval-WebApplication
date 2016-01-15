/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import ij.process.ColorProcessor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author longbow
 */

public class AbstractExtractor {
    
    ColorProcessor image=null;
    
    public AbstractExtractor(String imagename) throws IOException
    {
          File f = new File(imagename);
         image = new ColorProcessor(ImageIO.read(f));
        
    }
    
     public List<double[]> getFeatures()  throws IOException
    {
        return null;
        
    }
    
}
