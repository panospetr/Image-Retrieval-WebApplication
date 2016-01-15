/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RankingModel;

import Common.ImageFeatureVector;
import Common.MapValueComparator;
import Storage.StorageManager;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author longbow
 */
public class VectorModel {
    
    StorageManager SM;
    /*String RetrivalString="SELECT \"ID\",cube_distance(cube(Array[";
    String ColorRetrivalString="]),\"ColorH\")  as d FROM \"ColorFeatures\" ORDER by d";
    String ShapeRetrivalString="]),\"ShapeH\")  as d FROM \"ShapeFeatures\" ORDER by d";
    String TextureRetrivalString="]),\"TexH\")  as d FROM \"TextureFeatures\" ORDER by d";*/
    String StatementStart="SELECT \"Image\".\"Path\", (";
    
    String StatementEnd="FROM \"ColorFeatures\" "
            + "INNER JOIN \"ShapeFeatures\" ON (\"ColorFeatures\".\"ID\" = \"ShapeFeatures\".\"ID\")"
            + " INNER JOIN \"TextureFeatures\" ON (\"TextureFeatures\".\"ID\" = \"ShapeFeatures\".\"ID\") "
            + "INNER JOIN \"Image\" ON (\"TextureFeatures\".\"ID\" = \"Image\".\"ID\")  ORDER BY D";
            
    
    
    Connection con;
    public VectorModel(StorageManager SM,String url,String user,String password)
    {   
        if(SM.getConnection()==null)
        {
            try
            {
            con=SM.OpenConnection(url, user, password);
            }
            catch(SQLException e)
            {
                        e.printStackTrace();
            }
        }
        else
        {
            con=SM.getConnection();
        }
        this.SM=SM;
    }
    
    public ImageFeatureVector BuildNewIFV(ImageFeatureVector original,ImageFeatureVector[] RankedPositively,ImageFeatureVector[] RankedNegatively,
                                                                                double originalWeight,double PositiveWeight,double NegativeWeight)
    {   
                double[] NewColorHistogram=new double[original.getColorHistogram().length];
                double[] NewTextureHistogram=new double[original.getTextureHistogram().length];
                double[] NewShapeHistogram=new double[original.getShapeHistrogram().length];
                double[] ColorPositive=new double[original.getColorHistogram().length];
                double[] ShapePositive=new double[original.getShapeHistrogram().length];
                double[] TexturePositive=new double[original.getTextureHistogram().length];
                
                for(int i=0;i<RankedPositively.length;i++)
                {
                    for(int j=0;j<original.getColorHistogram().length;j++)
                    {
                        ColorPositive[j]+=Math.sqrt(Math.pow(original.getColorHistogram()[j]-RankedPositively[i].getColorHistogram()[j], 2));
                    }
                    
                    for(int j=0;j<original.getShapeHistrogram().length;j++)
                    {   
                        ShapePositive[j]+=Math.sqrt(Math.pow(original.getShapeHistrogram()[j]-RankedPositively[i].getShapeHistrogram()[j], 2));
                        
                    }
                    
                    for(int j=0;j<original.getTextureHistogram().length;j++)
                    {
                        TexturePositive[j]+=Math.sqrt(Math.pow(original.getTextureHistogram()[j]-RankedPositively[i].getTextureHistogram()[j], 2));
                    }
                     
                }
                double[] ColorNegative=new double[original.getColorHistogram().length];
                double[] ShapeNegative=new double[original.getShapeHistrogram().length];
                double[] TextureNegative=new double[original.getTextureHistogram().length];
                
                for(int i=0;i<RankedNegatively.length;i++)
                {
                    for(int j=0;j<original.getColorHistogram().length;j++)
                    {
                        ColorNegative[j]+=Math.sqrt(Math.pow(original.getColorHistogram()[j]-RankedNegatively[i].getColorHistogram()[j], 2));
                    }
                    
                    for(int j=0;j<original.getShapeHistrogram().length;j++)
                    {
                        ShapeNegative[j]+=Math.sqrt(Math.pow(original.getShapeHistrogram()[j]-RankedNegatively[i].getShapeHistrogram()[j], 2));
                    }
                    
                    for(int j=0;j<original.getTextureHistogram().length;j++)
                    {
                        TextureNegative[j]+=Math.sqrt(Math.pow(original.getTextureHistogram()[j]-RankedNegatively[i].getTextureHistogram()[j], 2));
                    }
                     
                }
                
                for(int i=0;i<original.getColorHistogram().length;i++)
                {
                    NewColorHistogram[i]=originalWeight*original.getColorHistogram()[i];
                    if(RankedPositively.length>0)
                    NewColorHistogram[i]+=PositiveWeight/RankedPositively.length*ColorPositive[i];
                    if(RankedNegatively.length>0)
                    NewColorHistogram[i]-=NegativeWeight/RankedNegatively.length*ColorNegative[i];
                        
                }
                
                for(int i=0;i<original.getTextureHistogram().length;i++)
                {
                    NewTextureHistogram[i]=originalWeight*original.getTextureHistogram()[i];
                    if(RankedPositively.length>0)
                        NewTextureHistogram[i]+=PositiveWeight/RankedPositively.length*TexturePositive[i];
                    if(RankedNegatively.length>0)
                        NewTextureHistogram[i]-=NegativeWeight/RankedNegatively.length*TextureNegative[i];
                        
                }
                
                for(int i=0;i<original.getShapeHistrogram().length;i++)
                {
                    NewShapeHistogram[i]=originalWeight*original.getShapeHistrogram()[i];
                            if(RankedPositively.length>0)
                                NewShapeHistogram[i]+=PositiveWeight/RankedPositively.length*ShapePositive[i];
                            if(RankedNegatively.length>0)
                                NewShapeHistogram[i]-=NegativeWeight/RankedNegatively.length*ShapeNegative[i];
                }
              
              
              ImageFeatureVector NIFV=new ImageFeatureVector(NewColorHistogram,NewTextureHistogram,NewShapeHistogram);
              return NIFV;
    }
    
    public ArrayList<String> getKSimilar(ImageFeatureVector IFV,int k,double ColorWeight,double ShapeWeight,double TextureWeight) throws SQLException
    {   
        
       /* HashMap<BigInteger,Double> map = new HashMap<BigInteger,Double>();
        MapValueComparator bvc =  new MapValueComparator(map);
        TreeMap<BigInteger,Double> sorted_map = new TreeMap<BigInteger,Double>(bvc);
        ArrayList<BigInteger> ResultIDS=new ArrayList<BigInteger>();
        double[] ColorH=IFV.getColorHistogram();
        String ColorRetriveStatement=RetrivalString;
    for(int i=0;i<ColorH.length-1;i++)
        {
            ColorRetriveStatement+=(ColorH[i]+",");
        }
        ColorRetriveStatement+=(ColorH[ColorH.length-1]+ColorRetrivalString+" LIMIT "+k);
        
       Statement st = con.createStatement();
       //System.out.println(ColorRetriveStatement);
       
       ResultSet rs = st.executeQuery(ColorRetriveStatement);
         
        System.out.println("Color:");
           while(rs.next())
           {    
               //System.out.println(rs.getRow()+" "+rs.getString("ID")+" "+rs.getDouble(2));
               map.put(BigInteger.valueOf(Long.parseLong(rs.getString("ID"))), rs.getDouble(2));
           }
           
        double[] ShapeH=IFV.getShapeHistrogram();
        String ShapeRetriveStatement=RetrivalString;
    for(int i=0;i<ShapeH.length-1;i++)
        {
            ShapeRetriveStatement+=(ShapeH[i]+",");
        }
        ShapeRetriveStatement+=(ShapeH[ShapeH.length-1]+ShapeRetrivalString+" LIMIT "+k);
        
        st = con.createStatement();
       //System.out.println(ColorRetriveStatement);
       
        rs = st.executeQuery(ShapeRetriveStatement);
         
        System.out.println("Shape:");
           while(rs.next())
           {    
               //System.out.println(rs.getRow()+" "+rs.getString("ID")+" "+rs.getDouble(2));
               map.put(BigInteger.valueOf(Long.parseLong(rs.getString("ID"))), rs.getDouble(2));
           }
           
           double[] TexH=IFV.getShapeHistrogram();
        String TextureRetrieveStatement=RetrivalString;
    for(int i=0;i<TexH.length-1;i++)
        {
            TextureRetrieveStatement+=(TexH[i]+",");
        }
        TextureRetrieveStatement+=(TexH[TexH.length-1]+TextureRetrivalString+" LIMIT "+k);
        
        st = con.createStatement();
       //System.out.println(ColorRetriveStatement);
       
        rs = st.executeQuery(TextureRetrieveStatement);
         
        System.out.println("Texture:");
           while(rs.next())
           {    
              //System.out.println(rs.getRow()+" "+rs.getString("ID")+" "+rs.getDouble(2));
               map.put(BigInteger.valueOf(Long.parseLong(rs.getString("ID"))), rs.getDouble(2));
           }
        
           sorted_map.putAll(map);
        //System.out.println("Result:"+sorted_map);
           Collection c = sorted_map.keySet();

    //obtain an Iterator for Collection
    Iterator itr = c.iterator();

    //iterate through TreeMap values iterator
    
      
        for(int i=0;i<k;i++)
        {
            //System.out.println(itr.next());
            ResultIDS.add((BigInteger) itr.next());
        }
        
        ArrayList<String> ResultImagePaths=new ArrayList<String>();
        String ResultsRetrievalString="SELECT \"Path\" FROM \"Image\" WHERE ";
        for(int i=0;i<ResultIDS.size()-1;i++)
        {
            ResultsRetrievalString+=("\"ID\"="+ResultIDS.get(i)+" OR ");
        }
        
        ResultsRetrievalString +="\"ID\"="+ResultIDS.get(ResultIDS.size()-1)+";";
        System.out.println(ResultsRetrievalString);
        st = con.createStatement();
       //System.out.println(ColorRetriveStatement);
       
        rs = st.executeQuery(ResultsRetrievalString);
        
        while(rs.next())
        {   
            
            ResultImagePaths.add(rs.getString("Path"));
        }
        return ResultImagePaths;
    */    
      
          double[] ColorH=IFV.getColorHistogram();
          String ColorCubeCreate=ColorWeight+"*cube_distance(cube(Array[";
    for(int i=0;i<ColorH.length-1;i++)
        {
            ColorCubeCreate+=(ColorH[i]+",");
        }
        ColorCubeCreate+=(ColorH[ColorH.length-1]+"]),\"ColorH\")  + ");
        
        
          double[] ShapeH=IFV.getShapeHistrogram();
        String ShapeCubeCreate=ShapeWeight+"*cube_distance(cube(Array[";
    for(int i=0;i<ShapeH.length-1;i++)
        {
            ShapeCubeCreate+=(ShapeH[i]+",");
        }
    ShapeCubeCreate+=(ShapeH[ShapeH.length-1]+"]),\"ShapeH\") + ");
    
    
    double[] TexH=IFV.getTextureHistogram();
        String TextureCubeCreate=TextureWeight+"*cube_distance(cube(Array[";
    for(int i=0;i<TexH.length-1;i++)
        {
            TextureCubeCreate+=(TexH[i]+",");
        }
        TextureCubeCreate+=(TexH[TexH.length-1]+"]),\"TexH\") ) as D ");
        String Statement=StatementStart+ColorCubeCreate+ShapeCubeCreate+TextureCubeCreate+StatementEnd+" LIMIT "+k+" ;";
        System.out.println(Statement);
        
        ArrayList<String> ResultImagePaths=new ArrayList<String>();
        Statement st = con.createStatement();
       //System.out.println(ColorRetriveStatement);
       
        ResultSet rs = st.executeQuery(Statement);
        
        while(rs.next())
        {   
            System.out.println(rs.getString("Path")+" "+rs.getString("D"));
            ResultImagePaths.add(rs.getString("Path"));
        }
        return ResultImagePaths;
        
        
        
        
    }
    
}
