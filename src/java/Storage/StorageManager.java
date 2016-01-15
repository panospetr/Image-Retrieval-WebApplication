/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author longbow
 */
public class StorageManager {
        
        Connection con=null;
    public StorageManager() 
    {
        try
        {
        Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
                    e.printStackTrace();
        }
    }
    
    
    public Connection OpenConnection(String url,String user,String password) throws SQLException
    {
         con = DriverManager.getConnection(url, user, password);
         return con;
    }
    
    public Connection getConnection()
    {
        return con;
    }
    
    public void CloseConnection() throws SQLException
    {
        
        con.close();
    }
    
}
