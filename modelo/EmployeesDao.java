/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class EmployeesDao { //aca java interactua con mysql
    //instaciamos la conexion

    connectionMYSQL con = new connectionMYSQL();
    //estas 3 variables nos ayudan a conectarnos
    Connection conn; //conexion
    PreparedStatement pst; //sirve para las consultas
    ResultSet rs; // sirve para obtener los datos de la consulta
    //variables para enviar datos entre intefaces
    public static int user_id = 0;
    public static String full_name_user = "";
    public static String userName_user = "";
    public static String adress_user = "";
    public static String rol_user = "";
    public static String email_user = "";
    public static String telephone_user = "";

    // metodo del login

    public Employees loginQuery(String user, String password) {
        String query = "SELECT * FROM employees WHERE username=? AND Password=?";
        Employees emp = new Employees();
        try {
            conn = con.getConnection();
            pst = conn.prepareStatement(query);
            // enviar parametros
            pst.setString(1, user);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if (rs.next()) {
                emp.setID(rs.getInt("id"));
                user_id = emp.getID();
                emp.setName(rs.getString("Fullname"));
                full_name_user = emp.getName();
                emp.setUser_name(rs.getString("username"));
                userName_user = emp.getUser_name();
                emp.setAdress(rs.getString("Adress"));
                adress_user = emp.getAdress();
                emp.setTelephone(rs.getString("Telephone"));
                telephone_user = emp.getTelephone();
                emp.setEmail(rs.getString("Email"));
                email_user = emp.getEmail();
                emp.setRol(rs.getString("Rol"));
                rol_user = emp.getRol();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener al empleado " + e);
        }
        return emp;
    }

    //registrar empleado
    public boolean registerEmployeesQuery(Employees employees) {
        String query = " INSERT INTO employees(id,Fullname,username,Adress,Telephone,Email,Password,Rol,Created,Updated) VALUES(?,?,?,?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);//preparon el server
            pst.setInt(1, employees.getID());
            pst.setString(2, employees.getName());
            pst.setString(3, employees.getUser_name());
            pst.setString(4, employees.getAdress());
            pst.setString(5, employees.getTelephone());
            pst.setString(6, employees.getEmail());
            pst.setString(7, employees.getPassword());
            pst.setString(8, employees.getRol());
            pst.setTimestamp(9, datetime);
            pst.setTimestamp(10, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al registrar al empleado " + e);
            return false;
        }
    }

    //lista empleado
    public List listEmployeesQuery(String value) {
        List<Employees> list = new ArrayList();
        String query = "SELECT * FROM employees ORDER BY Rol ASC"; //aca listamos todos los empleados ordenados por rol de forma ascendente
        String query_search = "SELECT * FROM employees WHERE id LIKE '%" + value + "%'"; // listamos los empleados que su id coincida con el id a buscar
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
                Employees employees = new Employees();
                employees.setID(rs.getInt("Id"));
                employees.setName(rs.getString("Fullname"));
                employees.setUser_name(rs.getString("username"));
                employees.setAdress(rs.getString("Adress"));
                employees.setEmail(rs.getString("Email"));
                employees.setPassword(rs.getString("Password"));
                employees.setRol(rs.getString("Rol"));
                employees.setTelephone(rs.getString("Telephone"));
                list.add(employees);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list;
    }

    //modificar empleado
    public boolean updatedEmployeesQuery(Employees employees) {
        String query = " UPDATE  employees SET Fullname=?,username=?,Adress=?,Telephone=?,Email=?,Rol=?,Updated=? WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);//preparon el server
            pst.setString(1, employees.getName());
            pst.setString(2, employees.getUser_name());
            pst.setString(3, employees.getAdress());
            pst.setString(4, employees.getTelephone());
            pst.setString(5, employees.getEmail());
            pst.setString(6, employees.getRol());
            pst.setTimestamp(7, datetime);
            pst.setInt(8, employees.getID());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error al modificar los datos del empleado " + e);
            return false;
        }
    }

    //eliminar empleado
    public boolean deleteEmployeesQuery(int id) {
        String query = "DELETE FROM employees WHERE id= " + id;
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "no puede eliminar un empleado que tenga relacion con otra tabla " + e);
            return false;
        }
    }

    //cambiar contraseña
    public boolean updateEmployeesPassword(Employees employees) {
        String query = "UPDATE employees SET Password=? WHERE username= '" + userName_user+"'";
        try {
            conn = con.getConnection(); //obtengo la conexion
            pst = conn.prepareStatement(query);
            pst.setString(1, employees.getPassword());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ha ocurrido un error al modificar la contraseña " + e);
            return false;
        }
    }

}
