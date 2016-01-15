package FeatureExtraction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static com.sun.org.glassfish.external.amx.AMXUtil.prop;
import de.lmu.ifi.dbs.jfeaturelib.LibProperties;
import de.lmu.ifi.dbs.jfeaturelib.features.ColorHistogram;
import de.lmu.ifi.dbs.jfeaturelib.features.Histogram;
import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.AdaptiveGridResolution;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.PolygonEvolution;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.Profiles;
import de.lmu.ifi.dbs.jfeaturelib.shapeFeatures.SquareModelShapeMatrix;
import de.lmu.ifi.dbs.utilities.Arrays2;
import ij.process.ColorProcessor;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;


/**
 *
 * @author longbow
 */


public class JFeatureLibTest  {
    
    public void Test() throws IOException
    {       String filename="red.jpg";
          File f = new File("Images/"+filename);
        ColorProcessor image = new ColorProcessor(ImageIO.read(f));

        // load the properties from the default properties file
        // change the histogram to span just 2 bins
        // and let's just extract the histogram for the RED channel
        LibProperties prop = LibProperties.get();
        
      /*  
        // after v 1.0.1 you will be able to use this:
        // prop.setProperty(LibProperties.HISTOGRAMS_TYPE, Histogram.TYPE.Red.name());
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_BINS_X, 8);
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_BINS_Y, 8);
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_BINS_Z, 8);
                        prop.setProperty(LibProperties.COLOR_HISTOGRAMS_TYPE, ColorHistogram.TYPE.RGB.name());
        // initialize the descriptor, set the properties and run it
        ColorHistogram descriptor = new ColorHistogram();
        
        descriptor.setProperties(prop);
        descriptor.run(image);

        // obtain the features*/
        
       /* Tamura descriptor=new Tamura();
                descriptor.run(image);
        
        List<double[]> features = descriptor.getFeatures();

        
         
        // print the features to system out*/
        SquareModelShapeMatrix descriptor=new SquareModelShapeMatrix(10);
        descriptor.run(image);
        List<double[]> features = descriptor.getFeatures();
       
        double sum=0;
        for(int i=0;i<features.get(0).length;i++)
            sum+=features.get(0)[i];   
        
        for(int i=0;i<features.get(0).length;i++)
            features.get(0)[i]=features.get(0)[i]/sum;
        
        for(int i=0;i<features.size();i++)
        {   System.out.print("Size:"+features.get(i).length+ " : ");
            for(int j=0;j<features.get(i).length;j++)
            {
                System.out.printf("%.8f ",features.get(i)[j]);
            }
        System.out.println("\n");
        }
        System.out.println("\nDone!");
    }

    
}
