/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author longbow
 */
public class CrawlerSync implements Runnable{

    ServerSocket UploadServer = null;
        String line;
        DataInputStream is;
        PrintStream os;
        Socket clientSocket = null;
        String StartUrl;
        public CrawlerSync(String StartUrl)
        {
            this.StartUrl=StartUrl;
        }
    @Override
    public void run() {
        try {
            System.out.println(System.getProperty("user.dir"));
            System.out.println(StartUrl);
            Process p = Runtime.getRuntime().exec("python crawler.py "+StartUrl);
            
            
        
// Try to open a server socket on port 9999
// Note that we can't choose a port less than 1023 if we are not
// privileged users (root)
        try {
           UploadServer = new ServerSocket(9999);
        }
        catch (IOException e) {
           System.out.println(e);
        }   
// Create a socket object from the ServerSocket to listen and accept 
// connections.
// Open input and output streams
    try {
       
        clientSocket = UploadServer.accept();
        System.out.println("Socket Connected");
        is = new DataInputStream(clientSocket.getInputStream());
           os = new PrintStream(clientSocket.getOutputStream());
        
           
// As long as we receive data, echo that data back to the client.
           while( (line=is.readLine()).compareTo("Done")!=0)
           {System.out.println("Reading from socket");
            
             System.out.println("Read");
            System.out.println(line);
            StringTokenizer tok=new StringTokenizer(line,"(,)");
            while(tok.hasMoreTokens())
            {
                saveImage(tok.nextToken());
                
            }
             os.println("OK");
           } 
            
        
         
        }   
    catch (IOException e) {
           System.out.println(e);
        }
    }   catch (IOException ex) {
            Logger.getLogger(CrawlerSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println("Closing Sockets");
            UploadServer.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(CrawlerSync.class.getName()).log(Level.SEVERE, null, ex);
        }
}
 
    public static void saveImage(String imageUrl ) throws MalformedURLException  {
        try {
            
            
            URL url = new URL(imageUrl);
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            uc.connect();
            
            InputStream is = uc.getInputStream();
            OutputStream os = new FileOutputStream(new File("Images/Crawler/"+imageUrl.replaceAll("/", ".")));
            
            byte[] b = new byte[2048];
            int length;
            
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            
            is.close();
            os.close();
            
            //ImageIO.write(image, "jpg",new File("Images/Crawler/\"+imageUrl"));
        } catch (IOException ex) {
            Logger.getLogger(CrawlerSync.class.getName()).log(Level.SEVERE, null, ex);
        }

 
       
       
        
    }
}
