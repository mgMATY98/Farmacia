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

public class SalesDao {

    connectionMYSQL con = new connectionMYSQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    // registrar una venta
    public boolean registerSaleQuery(int cus_id, int emp_id, double total) {

        String query = "INSERT INTO sale (Customer_id, Employees_id,  Total, Sale_date) VALUES (?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, cus_id);
            pst.setInt(2, emp_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    //regsitrar detalle de la venta
    public boolean registerSaleDetailQuery(int pro_id, double sale_id,
            int sale_quantity, double sale_price, double sale_subtotal) {

        String query = "INSERT INTO sale_detaills (product_id, sale_id, Sale_quantity, Sale_price, Sale_subtotal) VALUES (?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, pro_id);
            pst.setDouble(2, sale_id);
            pst.setInt(3, sale_quantity);
            pst.setDouble(4, sale_price);
            pst.setDouble(5, sale_subtotal);
            pst.execute();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

    //id maximo de la venta
    public int maxId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM sale";
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    //listar las ventas realizadas
    public List listSalesQuery() {
        List<Sales> list_s = new ArrayList();
        String query = "SELECT s.id AS invoice, c.full_name AS cus, e.Fullname AS emp, s.Total, s.Sale_date FROM sale s INNER JOIN customers c ON s.Customer_id = c.id INNER JOIN employees e ON s.Employees_id = e.id ORDER BY S.id ASC";

        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            //Recorrer 
            while (rs.next()) {
                Sales sale = new Sales();
                sale.setId(rs.getInt("invoice"));
                sale.setCustomer_name(rs.getString("cus"));
                sale.setEmployee_name(rs.getString("emp"));
                sale.setTotal_to_pay(rs.getDouble("Total"));
                sale.setSale_date(rs.getString("Sale_date"));
                list_s.add(sale);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_s;
    }
}
