/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Employees;
import modelo.EmployeesDao;
import static modelo.EmployeesDao.rol_user;
import static modelo.EmployeesDao.user_id;
import vista.SystemView;

/**
 *
 * @author Usuario
 */
public class EmployeesController implements ActionListener, MouseListener, KeyListener {

    private final Employees emp;
    private final EmployeesDao emp_dao;
    private final SystemView view;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel(); //para interactuar con la tabla

    public EmployeesController(Employees emp, EmployeesDao emp_dao, SystemView view) {
        this.emp = emp;
        this.emp_dao = emp_dao;
        this.view = view;
        //boton de registrar empleado
        this.view.Register_emp.addActionListener(this);
        //boton de eliminar empleado
        this.view.btn_delete_emp.addActionListener(this);
        //boton de cancelar empleado
        this.view.btn_cancel_emp.addActionListener(this);
        this.view.table_emp.addMouseListener(this);
        //boton de modificar perfil
        this.view.btn_modifier_perf.addActionListener(this);
        //colocar label de empleado en escucha
        this.view.jLabelEmpleados.addMouseListener(this);
        this.view.txt_search_emp.addKeyListener(this);
        this.view.btn_mod_emp.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.Register_emp) { //registrar empleado
            //verificar si los campos estan vacios
            if (view.txt_id.getText().equals("")
                    || view.txt_emp_direcc.getText().equals("")
                    || view.txt_emp_adres.getText().equals("")
                    || view.txt_emp_direcc.getText().equals("")
                    || view.txt_emp_tel.getText().equals("")
                    || view.txt_emp_userN.getText().equals("")
                    || view.cmb_rol_emp.getSelectedItem().toString().equals("")
                    || String.valueOf(view.txt_emp_pass.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            } else {
                //realizar la insercion
                emp.setID(Integer.parseInt(view.txt_id.getText().trim()));
                emp.setName(view.txt_emp_name.getText().trim());
                emp.setUser_name(view.txt_emp_userN.getText().trim());
                emp.setRol(view.cmb_rol_emp.getSelectedItem().toString().trim());
                emp.setAdress(view.txt_emp_direcc.getText().trim());
                emp.setEmail(view.txt_emp_adres.getText().trim());
                emp.setPassword(String.valueOf(view.txt_emp_pass.getPassword()));
                emp.setTelephone(view.txt_emp_tel.getText().trim());
                if (emp_dao.registerEmployeesQuery(emp)) {
                    cleanTable();
                    cleanFields();
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "empleado registrado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "ha ocurrido un error con el registro");
                }
            }
        } else if (e.getSource() == view.btn_mod_emp) { //modificar empleado
            if (view.txt_id.equals("")) {
                JOptionPane.showMessageDialog(null, "selecciona una fila para continuar");
            } else {
                //verificar si los campos estan vacios
                if (view.txt_id.getText().equals("") || (view.txt_emp_name.getText().equals("")) || (view.cmb_rol_emp.getSelectedItem().toString().equals(""))) {
                    JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
                } else {
                    emp.setID(Integer.parseInt(view.txt_id.getText().trim()));
                    emp.setName(view.txt_emp_name.getText().trim());
                    emp.setUser_name(view.txt_emp_userN.getText().trim());
                    emp.setRol(view.cmb_rol_emp.getSelectedItem().toString().trim());
                    emp.setAdress(view.txt_emp_direcc.getText().trim());
                    emp.setEmail(view.txt_emp_adres.getText().trim());
                    emp.setPassword(String.valueOf(view.txt_emp_pass.getPassword()));
                    emp.setTelephone(view.txt_emp_tel.getText().trim());

                    if (emp_dao.updatedEmployeesQuery(emp)) {
                        cleanTable();
                        cleanFields();
                        listAllEmployees();
                        view.Register_emp.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "empleado modificado con exito");
                    } else {
                        JOptionPane.showMessageDialog(null, "ocurrio un error al modificar al empleado");
                    }
                }
            }
        } else if (e.getSource() == view.btn_delete_emp) {
            int row = view.table_emp.getSelectedRow();
            if (row == -1) { //si el empleado se selecciono asi mismo
                JOptionPane.showMessageDialog(null, "debes seleccionar un empleado para eliminar");
            } else if (view.table_emp.getValueAt(row, 0).equals(user_id)) { //para saber si se selecciono asi mismo
                JOptionPane.showMessageDialog(null, "no puede eliminar al usuario autenticado");
            } else {
                int id = Integer.parseInt(view.table_emp.getValueAt(row, 0).toString());
                int quest = JOptionPane.showConfirmDialog(null, "Quieres eliminar al empleado seleccionado?");
                if (quest == 0 && emp_dao.deleteEmployeesQuery(id) == true) {
                    cleanFields();
                    cleanTable();
                    view.Register_emp.setEnabled(true);
                    view.txt_emp_pass.setEnabled(true);
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "Empleado eliminado con exito");
                }
            }
        } else if (e.getSource() == view.btn_cancel_emp) {
            cleanFields();
            view.Register_emp.setEnabled(true);
            view.txt_emp_pass.setEnabled(true);
            view.txt_id.setEnabled(true);
        } else if (e.getSource() == view.btn_modifier_perf) {
            //obtener info de las cajas password
            String pass = String.valueOf(view.pass_perfil.getPassword());
            String confirm_pass = String.valueOf(view.pass_confirm_perfil.getPassword());
            //verificar que las contraseñas sean iguales
            if (!pass.equals("") && !confirm_pass.equals("")) { //confirmar si los txt estan vacios
                if (pass.equals(confirm_pass)) {
                    emp.setPassword(String.valueOf(view.pass_perfil.getPassword()));
                    if (emp_dao.updateEmployeesPassword(emp) == true) {
                        JOptionPane.showMessageDialog(null, "contraseña modificada con exito");
                    } else {
                        JOptionPane.showMessageDialog(null, "ocurrio un error al modificar la contraseña del empleado");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "las contraseñas no coinciden");
                }
            } else {
                JOptionPane.showMessageDialog(null, "los campos no pueden estar vacios");
            }
        }
    }

    //listar empleado
    public void listAllEmployees() {
        if (rol.equals("Administrador")) {
            List<Employees> list = emp_dao.listEmployeesQuery(view.txt_search_emp.getText());
            model = (DefaultTableModel) view.table_emp.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getID();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getUser_name();
                row[3] = list.get(i).getAdress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                model.addRow(row);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.table_emp) {
            int row = view.table_emp.rowAtPoint(e.getPoint()); //esto es para saber en que fila se hizo click
            view.txt_id.setText(view.table_emp.getValueAt(row, 0).toString());
            view.txt_emp_name.setText(view.table_emp.getValueAt(row, 1).toString());
            view.txt_emp_userN.setText(view.table_emp.getValueAt(row, 2).toString());
            view.txt_emp_direcc.setText(view.table_emp.getValueAt(row, 3).toString());
            view.txt_emp_tel.setText(view.table_emp.getValueAt(row, 4).toString());
            view.txt_emp_adres.setText(view.table_emp.getValueAt(row, 5).toString());
            view.cmb_rol_emp.setSelectedItem(view.table_emp.getValueAt(row, 6).toString());

            //deshabilitar
            view.txt_id.setEditable(false);
            view.Register_emp.setEnabled(false);
            view.txt_emp_pass.setEnabled(false);
        } else if (e.getSource() == view.jLabelEmpleados) {
            if (rol.equals("Administrador")) {
                view.jTabbedPane1.setSelectedIndex(4);
                //limpiar tabla y campos
                cleanTable();
                cleanFields();
                listAllEmployees(); //lista emp
            } else {
                view.jTabbedPane1.setEnabledAt(3, false);
                view.jLabelEmpleados.setEnabled(false);
                JOptionPane.showMessageDialog(null, "no sos el administrador para acceder perri");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == view.txt_search_emp) {
            cleanTable();
            listAllEmployees();
        }
    }

    //limpiar campos
    public void cleanFields() {
        view.txt_emp_adres.setText("");
        view.txt_emp_direcc.setText("");
        view.txt_emp_tel.setText("");
        view.txt_emp_name.setText("");
        view.txt_emp_userN.setText("");
        view.txt_emp_pass.setEnabled(true);
        view.txt_emp_pass.setText("");
        view.cmb_rol_emp.setSelectedIndex(0);
        view.txt_id.setText("");
        view.txt_id.setEditable(true);

    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
}
