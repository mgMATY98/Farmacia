/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.Employees;
import modelo.EmployeesDao;
import vista.Login;
import vista.SystemView;

/**
 *
 * @author Usuario
 */
public class LoginController implements ActionListener {

    private Employees emp;
    private EmployeesDao emp_Dao;
    private Login login;

    public LoginController(Employees emp, EmployeesDao emp_Dao, Login login) {
        this.emp = emp;
        this.emp_Dao = emp_Dao;
        this.login = login;
        this.login.jButton1.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //obtener los datos de la vista
        String user = login.jTextField2.getText().trim();//limpiar espacios en blanco
        String pass = String.valueOf(login.jPasswordField1.getPassword());
        if (e.getSource() == login.jButton1) {
            //validar que los campos no esten vacios
            if (!user.equals("") || (!pass.equals(""))) {
                //pasar los parametros al metodo login
                emp = emp_Dao.loginQuery(user, pass);
                //verficar la existencia del usuario
                if (emp.getUser_name() != null) {//esto sirve para mostrar una ventana
                    if (emp.getRol().equals("Administrador")) {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);
                    } else {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);
                    }
                    this.login.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "usuario o contraseña incorrecta");
                }

            } else {
                JOptionPane.showMessageDialog(null, "los campos estan vacios");
            }

        }

    }

}
