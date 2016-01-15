import Common.ImageFeatureVector;
import Common.PostgresTest;
import Crawler.CrawlerSync;
import FeatureExtraction.ColorExtractor;
import FeatureExtraction.JFeatureLibTest;
import FeatureExtraction.ShapeExtractor;
import FeatureExtraction.TextureExtractor;
import RankingModel.VectorModel;
import Storage.ImageStorage;
import Storage.StorageManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PANOS
 */
@ManagedBean(name = "demo")
@ViewScoped
public class DemoBeans {

    private StorageManager SM;
    private VectorModel VM;
    private ImageStorage IS;
    private ImageFeatureVector CIFV;
    
    
    private  ArrayList<String> imagelist = new ArrayList<>();
    private  ArrayList<String> goodfeedbacklist = new ArrayList<>();
    private ArrayList<String> badfeedbacklist = new ArrayList<>();
    private ArrayList<String> imagesOnDb = new ArrayList<>();
    private List<Part> files = new ArrayList<>();
    private Part uploadedFile;
    private Part queryFile;
    private int switcher;
    private String imagenumber;
    private String coulorWeight="33.0";
    private String textureWeight="33.0";
    private String shapeWeight="33.0";
    
     private String crawlURL;

    public String getCrawlURL() {
        return crawlURL;
    }

    public void setCrawlURL(String crawlURL) {
        this.crawlURL = crawlURL;
    }

    public Part getQueryFile() {
        return queryFile;
    }


   

    public void setQueryFile(Part queryFile) {
        this.queryFile = queryFile;
    }
   
    public String getCoulorWeight() {
        return coulorWeight;
    }

    public void setCoulorWeight(String coulorWeight) {
        this.coulorWeight = coulorWeight;
    }

    public String getTextureWeight() {
        return textureWeight;
    }

    public void setTextureWeight(String textureWeight) {
        this.textureWeight = textureWeight;
    }

    public String getShapeWeight() {
        return shapeWeight;
    }

    public void setShapeWeight(String shapeWeight) {
        this.shapeWeight = shapeWeight;
    }

    public ArrayList<String> getImagesOnDb() {

        imagesOnDb = new ArrayList<>();

        File file = new File("Images");

        String[] mylist = file.list();
        for (int i = 0; i < 20; i++) {
            imagesOnDb.add(mylist[i]);
        }
        return imagesOnDb;
    }

    public void setImagesOnDb(ArrayList<String> imagesOnDB) {
        this.imagesOnDb = imagesOnDB;
    }

    public String getImagenumber() {
        return imagenumber;
    }

    public void setImagenumber(String imagenumber) {
        this.imagenumber = imagenumber;
    }

    /**
     *
     */
    @PostConstruct
    public void init()  {
        String url = "jdbc:postgresql://localhost/Multimedia Databases Assignment";
        String user = "postgres";
        String password = "postgres";
        SM = new StorageManager();
        IS = new ImageStorage(SM, url, user, password);
        VM=new VectorModel(SM, url, user, password);
        
    }

    public ArrayList<String> getGoodfeedbacklist() {
        return goodfeedbacklist;
    }

    public void setGoodfeedbacklist(ArrayList<String> goodfeedbacklist) {
        this.goodfeedbacklist = goodfeedbacklist;
    }

    public ArrayList<String> getBadfeedbacklist() {
        return badfeedbacklist;
    }

    public void setBadfeedbacklist(ArrayList<String> badfeedbacklist) {
        this.badfeedbacklist = badfeedbacklist;
    }

    public ArrayList<String> getImagelist() {
        return imagelist;
    }

    public void addFeedback(String img) {

        if (switcher == 1 && !goodfeedbacklist.contains(img)) {
            goodfeedbacklist.add(img);
        } else if (switcher == 2 && !badfeedbacklist.contains(img)) {
            badfeedbacklist.add(img);
        }

    }

    public void clearAll() {

        badfeedbacklist = new ArrayList<>();
        goodfeedbacklist = new ArrayList<>();
        imagelist=new ArrayList<>();
    }

    public void selectGood() {
        switcher = 1;
    }

    public void selectBad() {
        switcher = 2;
    }

    public void action() {

         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(imagenumber));
        imagelist = new ArrayList();
        imagelist.add("Images/1.jpg");
          imagelist.add("Images/2.jpg");
        switcher = 0;
  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("YES"));
        //return imagelist;
    }

    public void setImagelist(ArrayList<String> imagelist) {
        this.imagelist = imagelist;
    }

    public List getUploadedFile() {
        return files;
    }

    public void setUploadedFile(List<Part> uploadedFiles) {
        this.files = uploadedFiles;
    }

    public List<Part> getFiles() {
        return files;
    }

    public void setFiles(List<Part> files) {
        this.files = files;
    }

    public void upload() throws IOException, SQLException, ClassNotFoundException {
         
        for (Part file : files) {
            try (
                    InputStream inputStream = file.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream("Images/" + getFilename(file))) {

                int bytesRead = 0;
                final byte[] chunck = new byte[1024];
                while ((bytesRead = inputStream.read(chunck)) != -1) {
                    outputStream.write(chunck, 0, bytesRead);
                }
                String filename = getFilename(file);
                filename=filename.replaceAll("'", "\"");
                AddToDB("Images/" + filename);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload successfully ended: " + filename));
            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.toString()));
            }
        }
        
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    public void AddToDB(String image) throws SQLException, ClassNotFoundException, IOException {
        
        BigInteger id= IS.StoreImage(image);
         if(id.compareTo(BigInteger.valueOf(-1))!=0)
         {
        ColorExtractor CE = new ColorExtractor(image);
        double[] ColorH = CE.getFeatures().get(0);
        if(!IS.StoreColorFeatures(id, ColorH))
                return;
        TextureExtractor TE = new TextureExtractor(image);
        double[] TexH = TE.getFeatures().get(0);
        if(!IS.StoreTextureFeatures(id, TexH))
            return;
        ShapeExtractor SE = new ShapeExtractor(image);
        double ShapeH[] = SE.getFeatures().get(0);
        if(!IS.StoreShapeFeatures(id, ShapeH))
            return;
        
         }

    }

    public void GetTopK() throws IOException, ClassNotFoundException, SQLException {
        
       
        
        //Save image to imagefolder
       Part queryfile=files.get(0);
       String image ="Images/"+ getFilename(queryfile);
       
       
       try (
                InputStream inputStream = queryfile.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(image)) {

                int bytesRead = 0;
                final byte[] chunck = new byte[1024];
                while ((bytesRead = inputStream.read(chunck)) != -1) {
                    outputStream.write(chunck, 0, bytesRead);
                }
            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.toString()));
            }
        
       
          int k = Integer.valueOf(imagenumber);
          double ColorWeight = Double.valueOf(coulorWeight)/100;
          double TextureWeight = Double.valueOf(textureWeight)/100;
          double ShapeWeight = Double.valueOf(shapeWeight)/100;
         
        //check if relevance feedbacklists are empty
        if (goodfeedbacklist.isEmpty() && badfeedbacklist.isEmpty()) {          
            StorageManager SM = new StorageManager();

            ColorExtractor CE = new ColorExtractor(image);
            TextureExtractor TE = new TextureExtractor(image);
            ShapeExtractor SE = new ShapeExtractor(image);

            CIFV = new ImageFeatureVector(CE.getFeatures().get(0), TE.getFeatures().get(0), SE.getFeatures().get(0));
            
            
        } 
        else {
            ImageFeatureVector[] PositiveImgs=new ImageFeatureVector[goodfeedbacklist.size()];
            ImageFeatureVector[] NegativeImgs=new ImageFeatureVector[badfeedbacklist.size()];
            double[] ColorH;
            double[] ShapeH;
            double[] TexH;
            for(int i=0;i<PositiveImgs.length;i++)
            {
                ColorH=IS.RetrieveColorFeatures(goodfeedbacklist.get(i),CIFV.getColorHistogram().length );
                ShapeH=IS.RetrieveShapeFeatures(goodfeedbacklist.get(i),CIFV.getShapeHistrogram().length );
                TexH=IS.RetrieveTextureFeatures(goodfeedbacklist.get(i),CIFV.getTextureHistogram().length );
                
                PositiveImgs[i]=new ImageFeatureVector(ColorH,TexH,ShapeH   );
            }
            
            for(int i=0;i<NegativeImgs.length;i++)
            {
                ColorH=IS.RetrieveColorFeatures(badfeedbacklist.get(i),CIFV.getColorHistogram().length );
                ShapeH=IS.RetrieveShapeFeatures(badfeedbacklist.get(i),CIFV.getShapeHistrogram().length );
                TexH=IS.RetrieveTextureFeatures(badfeedbacklist.get(i),CIFV.getTextureHistogram().length );
                
                NegativeImgs[i]=new ImageFeatureVector(ColorH,TexH,ShapeH);
                
            }
            CIFV=VM.BuildNewIFV(CIFV, PositiveImgs, NegativeImgs, 0.5, 0.3, 0.2);
            //relevance feeback
        }
         
        imagelist=VM.getKSimilar(CIFV, k, ColorWeight, ShapeWeight, TextureWeight);
        //imagelist= Returned-result-pathList
    }
    
     public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        UploadedFile file = event.getFile();
        try{
          //  Log("fileName: " + file.getFileName());
            FileOutputStream fos = new FileOutputStream(new File(file.getFileName()));
            InputStream is = file.getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while(true){
                a = is.read(buffer);
                if(a < 0) break;
                fos.write(buffer, 0, a);
                fos.flush();
            }
            fos.close();
            is.close();
        }catch(IOException e){
        }
    }
     public void crawl()
     {
         //go crawl and then
         new Thread(new CrawlerSync(crawlURL)).start();
         
     }
    
    
}
