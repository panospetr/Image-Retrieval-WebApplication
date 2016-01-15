/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Storage;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author longbow
 */

/*
SELECT *,cube_distance(cube(
Array[0.6572751865593515,1.842561495489912E-4,0.0,0.0,0.0046147790182497345,3.3501118099816585E-5,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,8.375279524954146E-6,8.375279524954146E-6,0.0,0.0,0.003450615164281108,0.0031909814990075296,3.3501118099816585E-5,0.0,0.23710416335145187,0.0012814177673179843,8.375279524954146E-6,0.0,0.003835878022428999,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,8.375279524954146E-6,0.0,0.0019346895702644078,0.002227824353637803,0.004204390321526981,6.700223619963317E-5,1.7588087002403706E-4,0.0018090603773900957,0.001666680625465875,8.375279524954146E-5,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0027135905660851433,0.07407934739821942])
,"ColorH")  as d FROM "ColorFeatures" ORDER by d 
*/

public class ImageStorage {
    Connection con;
    public ImageStorage(StorageManager SM,String url,String user,String password) 
    {
        
        if(SM.getConnection()==null)
        {   try
        {
            con=SM.OpenConnection(url, user, password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        }
        
        
    }
    
    public BigInteger StoreImage(String path) throws SQLException
    {
        BigInteger ID = BigInteger.valueOf(-1);
       String ImageInsertStatement="INSERT INTO \"Image\"(\"Path\") VALUES('"+path+"') RETURNING \"ID\";";
       Statement st = con.createStatement();
       System.out.println(ImageInsertStatement);
           ResultSet rs = st.executeQuery(ImageInsertStatement);
         
       
           while(rs.next())
           {    ID=BigInteger.valueOf(Long.valueOf(rs.getString("ID")));
               System.out.println(rs.getString("ID"));
           }

            return ID;
           
    }
    
    public boolean StoreColorFeatures(BigInteger id,double[] ColorH) throws SQLException
    {
        
        String ColorInsertStatement="INSERT INTO \"ColorFeatures\"(\"ID\",\"ColorH\" ) VALUES("+id.toString()+",cube(Array[";
        for(int i=0;i<ColorH.length-1;i++)
        {
            ColorInsertStatement+=(ColorH[i]+",");
        }
        ColorInsertStatement+=(ColorH[ColorH.length-1]+"]) );");
        
       Statement st = con.createStatement();
       System.out.println(ColorInsertStatement);
        if(st.executeUpdate(ColorInsertStatement)>0)
            return true;
        return false;
        
        
        
    }
    
    public boolean StoreTextureFeatures(BigInteger id,double[] TexH) throws SQLException
    {
          String TextureInsertStatement="INSERT INTO \"TextureFeatures\"(\"ID\",\"TexH\" ) VALUES("+id.toString()+",cube(Array[";
        for(int i=0;i<TexH.length-1;i++)
        {
            TextureInsertStatement+=(TexH[i]+",");
        }
        TextureInsertStatement+=(TexH[TexH.length-1]+"]) );");
        
       Statement st = con.createStatement();
       System.out.println(TextureInsertStatement);
       if(st.executeUpdate(TextureInsertStatement)>0)
           return true;
       return false;
        
        
        
    }
    
    public boolean StoreShapeFeatures(BigInteger id,double[] ShapeH) throws SQLException
    {
         String ShapeInsertStatement="INSERT INTO \"ShapeFeatures\"(\"ID\",\"ShapeH\" ) VALUES("+id.toString()+",cube(Array[";
        for(int i=0;i<ShapeH.length-1;i++)
        {
            ShapeInsertStatement+=(ShapeH[i]+",");
        }
        ShapeInsertStatement+=(ShapeH[ShapeH.length-1]+"]) );");
        
       Statement st = con.createStatement();
       System.out.println(ShapeInsertStatement);
       if(st.executeUpdate(ShapeInsertStatement)>0)
        return true;
       return false;
        
    }
    
    public double[] RetrieveColorFeatures(String path,int numfeatures) throws SQLException
    {   
        double[] Result=new double[numfeatures];
        Statement st = con.createStatement();
        String ColorRetrievalStatement="SELECT \"ColorH\" FROM \"ColorFeatures\" INNER JOIN \"Image\" ON \"Image\".\"ID\"=\"ColorFeatures\".\"ID\" WHERE \"Path\"='"+path+"';";
           ResultSet rs = st.executeQuery(ColorRetrievalStatement);
         
           
           while(rs.next())
           {   StringTokenizer tok=new StringTokenizer(rs.getString(1),",()");
               for(int i=0;i<numfeatures;i++)
               {
                   Result[i]=Double.parseDouble(tok.nextToken());
               }
               
           }
           
           
           
        return Result;

          
        
    }
    
    public double[] RetrieveTextureFeatures(String path,int numfeatures) throws SQLException
    {
            double[] Result=new double[numfeatures];
        Statement st = con.createStatement();
        String TextureRetrievalStatement="SELECT \"TexH\" FROM \"TextureFeatures\" INNER JOIN \"Image\" ON \"Image\".\"ID\"=\"TextureFeatures\".\"ID\" WHERE \"Path\"='"+path+"';";
           ResultSet rs = st.executeQuery(TextureRetrievalStatement);
         
           
           while(rs.next())
           {   StringTokenizer tok=new StringTokenizer(rs.getString(1),",()");
               for(int i=0;i<numfeatures;i++)
               {
                   Result[i]=Double.parseDouble(tok.nextToken());
               }
               
           }
           
           
           
        return Result;

          
        
    }
    
    
    public double[] RetrieveShapeFeatures(String path,int numfeatures) throws SQLException
    {
           double[] Result=new double[numfeatures];
        Statement st = con.createStatement();
        String ShapeRetrievalStatement="SELECT \"ShapeH\" FROM \"ShapeFeatures\" INNER JOIN \"Image\" ON \"Image\".\"ID\"=\"ShapeFeatures\".\"ID\" WHERE \"Path\"='"+path+"';";
           ResultSet rs = st.executeQuery(ShapeRetrievalStatement);
         
           
           while(rs.next())
           {   StringTokenizer tok=new StringTokenizer(rs.getString(1),",()");
               for(int i=0;i<numfeatures;i++)
               {
                   Result[i]=Double.parseDouble(tok.nextToken());
               }
               
           }
           
           
          
        return Result;

          
        
    }
    
    public ArrayList<String> RetrieveResultImagePaths(ArrayList<BigInteger> ResultIDS)
    {   
        ArrayList<String> ResultImagePaths=new ArrayList<String>();
        String ResultsRetrievalString="SELECT path FROM Image WHERE ";
        for(int i=0;i<ResultIDS.size()-1;i++)
        {
            ResultsRetrievalString+=("\"ID\"="+ResultIDS.get(i)+" AND ");
        }
        
        ResultsRetrievalString +="\"ID\"="+ResultIDS.get(ResultIDS.size()-1)+";";
        System.out.println(ResultsRetrievalString);
        return ResultImagePaths;
    }
}
