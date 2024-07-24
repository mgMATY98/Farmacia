/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLException;

/**
 *
 * @author Usuario
 */
public class connectionMYSQL {
    private String database_Name="farmacy_databasse";
    private String user="root";
    private String password="Mantraskypea";
    private String URL="jdbc:mysql://localhost:3306/"+ database_Name;
    Connection conn=null;
    
    public Connection getConnection(){
        try{
          //obtener valor del driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //obtener la conexion
            conn=DriverManager.getConnection(URL, user, password);
        }
        catch(ClassNotFoundException e){
            System.err.println("ha ocurrido un ClassNotFoundException"+e.getMessage());
            
        }
        catch(SQLException e){
            System.err.println("Ha ocurrido un SQLException"+e.getMessage());
        }
        return conn;
    }
}
