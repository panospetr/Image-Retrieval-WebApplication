/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;
import java.io.IOException;
import java.util.List;


/**
 *
 * @author longbow
 */
public class TextureExtractor extends AbstractExtractor{

    public TextureExtractor(String imagename) throws IOException {
        super(imagename);
    }
    
    
    @Override
    public List<double[]> getFeatures()
            
    {
        
        Tamura descriptor=new Tamura();
                descriptor.run(image);
        
        List<double[]> features = descriptor.getFeatures();
        
        double sum=0;
        for(int i=0;i<features.get(0).length;i++)
            sum+=features.get(0)[i];
        
        for(int i=0;i<features.get(0).length;i++)
            features.get(0)[i]=features.get(0)[i]/sum;
        
        return features;
        
        
        
    }
    
}
