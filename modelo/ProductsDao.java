/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class ProductsDao {

    connectionMYSQL con = new connectionMYSQL();
    //estas 3 variables nos ayudan a conectarnos
    Connection conn; //conexion
    PreparedStatement pst; //sirve para las consultas
    ResultSet rs;

    public boolean registerProductsQuery(Products pro) {
        String query = "INSERT INTO products(code,name,description,unit_price,created,updated,category_id) VALUES (?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, pro.getCode());
            pst.setString(2, pro.getName());
            pst.setString(3, pro.getDescription());
            pst.setDouble(4, pro.getUnit_price());
            pst.setTimestamp(5, datetime);
            pst.setTimestamp(6, datetime);
            pst.setInt(7, pro.getCategory_id());
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no se puede registrar el producto" + e);
            return false;
        }
    }

    public List listProductsQuery(String value) {
        List<Products> list = new ArrayList();
        String query = "SELECT pro.*,ca.name AS category_name FROM products pro, categories ca WHERE pro.Category_id=ca.id";
        String query_search = "SELECT pro.*,ca.name AS category_name FROM products pro INNER JOIN categories ca ON pro.category_id=ca.id WHERE pro.name LIKE '%" + value + "%'";
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
                Products pro = new Products();
                pro.setId(rs.getInt("id"));
                pro.setCode(rs.getInt("code"));
                pro.setName(rs.getString("name"));
                pro.setDescription(rs.getString("description"));
                pro.setUnit_price(rs.getDouble("unit_price"));
                pro.setProduct_cant(rs.getInt("product_quantity"));
                pro.setCategory_name(rs.getString("category_name"));
                list.add(pro);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list;
    }

    public boolean updatedProductsQuery(Products pro) {
        String query = "UPDATE products SET code=?,name=?,description=?,unit_price=?,updated=?,category_id=? WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, pro.getCode());
            pst.setString(2, pro.getName());
            pst.setString(3, pro.getDescription());
            pst.setDouble(4, pro.getUnit_price());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, pro.getCategory_id());
            pst.setInt(7, pro.getId());
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no se puede modificar el producto" + e);
            return false;
        }
    }

    public boolean deleateProductQuery(int id) {
        String query = "DELETE FROM products WHERE id= " + id;
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no puede eliminar un producto que tenga relacion con otra tabla " + e);
            return false;
        }
    }

    public Products searchProductsQuery(int id) {
        String query = "SELECT pro.*,ca.name AS category_name FROM products pro INNER JOIN categories ca ON pro.category_id=ca.id WHERE pro.id=?";
        Products pro = new Products();
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                pro.setId(rs.getInt("id"));
                pro.setCode(rs.getInt("code"));
                pro.setName(rs.getString("name"));
                pro.setDescription(rs.getString("description"));
                pro.setUnit_price(rs.getDouble("unit_price"));
                pro.setCategory_id(rs.getInt("category_id"));
                pro.setCategory_name(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        return pro;
    }
    //buscar por codigo
     public Products searchCodeQuery(int code) {
        String query = "SELECT pro.id,pro.name,pro.unit_price FROM products pro WHERE pro.code=?";
        Products pro = new Products();
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, code);
            rs = pst.executeQuery();
            if (rs.next()) {
                pro.setId(rs.getInt("id"));
                pro.setName(rs.getString("name"));
                pro.setUnit_price(Double.parseDouble(rs.getString("unit_price")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        return pro;
    }
     //traer cantidad de productos
     public Products searchID(int id){
         String query="SELECT pro.product_quantity FROM products pro WHERE pro.id=?";
         Products pro=new Products();
         try{
             conn=con.getConnection();
             pst=conn.prepareStatement(query);
             pst.setInt(1, id);
             rs=pst.executeQuery();
            if(rs.next()){
                pro.setProduct_cant(rs.getInt("product_quantity"));
            } 
         }catch(SQLException e){
           JOptionPane.showMessageDialog(null, e.getMessage());
         }
         return pro;
     }
     
     public boolean updatedStockQuery(int cant, int product_id){
         String query="UPDATE products SET product_quantity=? WHERE id=?";
         try{
             conn=con.getConnection();
             pst=conn.prepareStatement(query);
             pst.setInt(1, cant);
             pst.setInt(2, product_id);
             pst.execute();
             return true;
             
         }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
         }
     }
}
