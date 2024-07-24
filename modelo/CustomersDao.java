/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class CustomersDao {

    connectionMYSQL con = new connectionMYSQL();
    //estas 3 variables nos ayudan a conectarnos
    Connection conn; //conexion
    PreparedStatement pst; //sirve para las consultas
    ResultSet rs; // sirve para obtener los datos de la consulta

    public boolean registerCustomersQuery(Customers customers) {
        String query = " INSERT INTO customers (id,full_name,adress,telephone,email,created,updated)" + " VALUES(?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);//preparon el server
            pst.setInt(1, customers.getId());
            pst.setString(2, customers.getFull_name());
            pst.setString(3, customers.getAdress());
            pst.setString(4, customers.getTelephone());
            pst.setString(5, customers.getEmail());
            pst.setTimestamp(6, datetime);
            pst.setTimestamp(7, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al registrar al cliente " + e);
            return false;
        }
    }

    public boolean updatedCustomersQuery(Customers customers) {
        String query = " UPDATE  customers SET full_name=?,adress=?,telephone=?,email=?,updated=? WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);//preparon el server
            pst.setString(1, customers.getFull_name());
            pst.setString(2, customers.getAdress());
            pst.setString(3, customers.getTelephone());
            pst.setString(4, customers.getEmail());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, customers.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al modificar al cliente " + e);
            return false;
        }
    }

    public List listCustomersQuery(String value) {
        List<Customers> list = new ArrayList();
        String query = "SELECT * FROM customers"; //aca listamos todos los empleados ordenados por rol de forma ascendente
        String query_search = "SELECT * FROM customers WHERE id LIKE '%" + value + "%'"; // listamos los empleados que su id coincida con el id a buscar
        try {
            conn = con.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Customers customers = new Customers();
                customers.setId(rs.getInt("id"));
                customers.setFull_name(rs.getString("full_name"));
                customers.setAdress(rs.getString("adress"));
                customers.setEmail(rs.getString("email"));
                customers.setTelephone(rs.getString("telephone"));
                list.add(customers);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list;
    }
    
    public boolean deleteCustomersQuery(int id) {
        String query = "DELETE FROM customers WHERE id= " + id;
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no puede eliminar un cliente que tenga relacion con otra tabla " + e);
            return false;
        }
    }
    
     public Customers searchCustomers(int id) {
        String query = "SELECT cus.id,cus.full_name FROM Customers cus WHERE cus.id=?";
        Customers pro = new Customers();
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                pro.setId(rs.getInt("id"));
                pro.setFull_name(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        return pro;
    }
}
