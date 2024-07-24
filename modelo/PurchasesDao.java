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
public class PurchasesDao {

    connectionMYSQL con = new connectionMYSQL();
    //estas 3 variables nos ayudan a conectarnos
    Connection conn; //conexion
    PreparedStatement pst; //sirve para las consultas
    ResultSet rs;

    public boolean registerPurchasesQuery(int supp_id, int emp_id, double total) {
        String query = "INSERT INTO purchases(suppliers_id,employee_id,total,created)VALUES (?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, supp_id);
            pst.setInt(2, emp_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al insertar la compra" + e);
            return false;
        }
    }

    public boolean registerPurchasesDetail(int pur_id, double pur_price, int pur_amount, double pur_subtotal, int pro_id) {
        String query = "INSERT INTO purchase_details(purchase_id,purchase_price,purchase_amount,purchase_subTotal,product_id) VALUES (?,?,?,?,?)";
        
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, pur_id);
            pst.setDouble(2, pur_price);
            pst.setInt(3, pur_amount);
            pst.setDouble(4, pur_subtotal);
            pst.setInt(5, pro_id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al insertar los detalles de la compra" + e);
            return false;
        }
    }

    public int PurchaseId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM purchases";
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    public List listAllPurchasesQuery() {
        List<Purchases> list = new ArrayList();
        String query = "SELECT pu.*, su.name AS supplier_name FROM purchases pu, proveedores su WHERE pu.suppliers_id=su.id ORDER BY pu.id ASC";
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Purchases pur = new Purchases();
                pur.setId(rs.getInt("id"));
                pur.setSupplier_name_product(rs.getString("supplier_name"));
                pur.setTotal(rs.getDouble("total"));
                pur.setCreated(rs.getString("created"));
                list.add(pur);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;
    }

    public List listPurchasesDetailQuery(int id) {
        List<Purchases> list = new ArrayList();
        String query = "SELECT pu.created, pude.purchase_price, pude.purchase_amount, pude.purchase_subtotal, su.name AS supplier_name,\n"
                + "pro.name AS product_name, em.Fullname FROM purchases pu INNER JOIN purchase_details pude ON pu.id=pude.purchase_id\n"
                + "INNER JOIN products pro ON pude.product_id=pro.id INNER JOIN proveedores su ON pu.suppliers_id=su.id\n"
                + "INNER JOIN employees em ON pu.employee_id=em.id WHERE pu.id=?";
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                Purchases pur = new Purchases();
                pur.setProduct_name(rs.getString("product_name"));
                pur.setPurchase_amount(rs.getInt("purchase_amount"));
                pur.setPurchase_price(rs.getDouble("purchase_price"));
                pur.setPurchase_subtotal(rs.getDouble("purchase_subtotal"));
                pur.setSupplier_name_product(rs.getString("supplier_name"));
                pur.setCreated(rs.getString("created"));
                pur.setPurcharser(rs.getString("Fullname"));
                list.add(pur);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;
    }
}
