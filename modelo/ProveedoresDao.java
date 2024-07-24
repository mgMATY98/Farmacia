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

public class ProveedoresDao {

    connectionMYSQL con = new connectionMYSQL();
    //estas 3 variables nos ayudan a conectarnos
    Connection conn; //conexion
    PreparedStatement pst; //sirve para las consultas
    ResultSet rs; // sirve para obtener los datos de la consulta
    //variables para enviar datos entre intefaces

    public boolean registerProveedoresQuery(Proveedores proveedores) {
        String query = "INSERT INTO proveedores (Name,Description,Addres,Telephone,Email,City,Created,Updated)" + " VALUES(?,?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);//preparon el server
            pst.setString(1, proveedores.getName());
            pst.setString(2, proveedores.getDescription());
            pst.setString(3, proveedores.getAddres());
            pst.setString(4, proveedores.getTelephone());
            pst.setString(5, proveedores.getEmail());
            pst.setString(6, proveedores.getCity());
            pst.setTimestamp(7, datetime);
            pst.setTimestamp(8, datetime);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al registrar al proveedor " + e);
            return false;
        }
    }

    public List listProveedoresQuery(String value) {
        List<Proveedores> list = new ArrayList();
        String query = "SELECT * FROM proveedores"; //aca listamos todos los empleados ordenados por rol de forma ascendente
        String query_search = "SELECT * FROM proveedores WHERE name LIKE '%" + value + "%'"; // listamos los empleados que su nombre coincida con el nombre a buscar
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
                Proveedores pro = new Proveedores();
                pro.setId(rs.getInt("id"));
                pro.setName(rs.getString("Name"));
                pro.setAddres(rs.getString("Addres"));
                pro.setEmail(rs.getString("Email"));
                pro.setTelephone(rs.getString("Telephone"));
                pro.setCity(rs.getString("City"));
                pro.setDescription(rs.getString("Description"));
                list.add(pro);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list;
    }

    public boolean updatedProveedoresQuery(Proveedores proveedores) {
        String query = " UPDATE proveedores SET Name=?,Description=?,Addres=?,Telephone=?,Email=?,City=?,Updated=? WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);//preparon el server
            pst.setString(1, proveedores.getName());
            pst.setString(2, proveedores.getAddres());
            pst.setString(3, proveedores.getTelephone());
            pst.setString(4, proveedores.getEmail());
            pst.setString(5, proveedores.getCity());
            pst.setString(6, proveedores.getDescription());
            pst.setTimestamp(7, datetime);
            pst.setInt(8, proveedores.getId());
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al modificar al proveedor " + e);
            return false;
        }
    }

    public boolean deleteProveedoresQuery(int id) {
        String query = "DELETE FROM proveedores WHERE id=" + id;
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no puedes eliminar un proveedor que tiene relacion con otra tabla" + e);
            return false;
        }
    }
}
