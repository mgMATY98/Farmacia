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
public class CategoriesDao {

    connectionMYSQL con = new connectionMYSQL();
    //estas 3 variables nos ayudan a conectarnos
    Connection conn; //conexion
    PreparedStatement pst; //sirve para las consultas
    ResultSet rs; // sirve para obtener los datos de la consulta

    public boolean registerCategoriesQuery(Categories cate) {
        String query = "INSERT INTO categories(name,created,updated) VALUES(?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, cate.getName());
            pst.setTimestamp(2, datetime);
            pst.setTimestamp(3, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al registrar categoria" + e);
            return false;
        }
    }

    public List listCategorieQuery(String value) {
        List<Categories> list = new ArrayList();
        String query = "SELECT * FROM categories";
        String list_query = "SELECT * FROM categories WHERE name LIKE '%" + value + "%'";
        try {
            conn = con.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(list_query);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Categories cate = new Categories();
                cate.setId(rs.getInt("id"));
                cate.setName(rs.getString("Name"));
                list.add(cate);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list;
    }

    public boolean updateCategoriesQuery(Categories cate) {
        String query = "UPDATE categories SET name=?,updated=? WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, cate.getName());
            pst.setTimestamp(2, datetime);
            pst.setInt(3, cate.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al modificar categoria" + e);
            return false;
        }
    }
    
    public boolean deleteCategoriesQuery(int id) {
        String query = "DELETE FROM categories WHERE id= " + id;
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no puede eliminar una categoria que tenga relacion con otra table " + e);
            return false;
        }
    }
}
