/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.AdaptiveGridResolution;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.SquareModelShapeMatrix;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author longbow
 */
public class ShapeExtractor extends AbstractExtractor {

    public ShapeExtractor(String imagename) throws IOException {
        super(imagename);
    }
    
    public List<double[]> getFeatures()
    {
         SquareModelShapeMatrix descriptor=new SquareModelShapeMatrix(8);
        descriptor.run(image);
        List<double[]> features = descriptor.getFeatures();
       
        double sum=0;
        if(features.size()==1)
        {for(int i=0;i<features.get(0).length;i++)
            sum+=features.get(0)[i];   
        
        for(int i=0;i<features.get(0).length;i++)
            features.get(0)[i]=features.get(0)[i]/sum;
        }
        return features;
        
    }
    
}
