/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeatureExtraction;

import de.lmu.ifi.dbs.jfeaturelib.LibProperties;
import de.lmu.ifi.dbs.jfeaturelib.features.ColorHistogram;
import ij.process.ColorProcessor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author longbow
 */
public class ColorExtractor extends AbstractExtractor{

    public ColorExtractor(String imagename) throws IOException {
        super(imagename);
    }
    
    @Override
    public List<double[]> getFeatures() throws IOException
    {
        List<double[]> features = null;

        // load the properties from the default properties file
        // change the histogram to span just 2 bins
        // and let's just extract the histogram for the RED channel
        LibProperties prop = LibProperties.get();
        
        
        // after v 1.0.1 you will be able to use this:
        // prop.setProperty(LibProperties.HISTOGRAMS_TYPE, Histogram.TYPE.Red.name());
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_BINS_X, 6);
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_BINS_Y, 6);
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_BINS_Z, 6);
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_TYPE, ColorHistogram.TYPE.RGB.name());
        // initialize the descriptor, set the properties and run it
        ColorHistogram descriptor = new ColorHistogram();
        
        descriptor.setProperties(prop);
        System.out.println("Image:"+image);
        descriptor.run(image);

        // obtain the features
         features= descriptor.getFeatures();
        
        return features;
        
    
    
}

    
    }