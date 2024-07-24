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
import static modelo.EmployeesDao.rol_user;
import modelo.Proveedores;
import modelo.ProveedoresDao;
import modelo.dinamicComboBox;
import vista.SystemView;

public class ProveedoresController implements ActionListener, MouseListener, KeyListener {

    private final Proveedores pro;
    private final ProveedoresDao pro_dao;
    private final SystemView view;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public ProveedoresController(Proveedores pro, ProveedoresDao pro_dao, SystemView view) {
        this.pro = pro;
        this.pro_dao = pro_dao;
        this.view = view;
        //poner en escucha a los botones
        this.view.btn_canecl_pro.addActionListener(this);
        this.view.btn_eliminar_pro.addActionListener(this);
        this.view.btn_mod_pro.addActionListener(this);
        this.view.btn_registrar_pro.addActionListener(this);
        //poner en escucha al buscar y tabla y label
        this.view.txt_pro_buscar.addKeyListener(this);
        this.view.tbt_provee.addMouseListener(this);
        this.view.JProveed.addMouseListener(this);
        this.getProveedorName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btn_registrar_pro) {
            if (view.txt_pro_direc.getText().equals("")
                    || view.txt_pro_tel.getText().equals("")
                    || view.txt_pro_emal.getText().equals("")
                    || view.txt_pro_name.getText().equals("")
                    || view.txt_pro_desc.getText().equals("")
                    || view.Cm_pro_ciudad.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            } else { //realizar la insercion
                pro.setName(view.txt_pro_name.getText().trim());
                pro.setDescription(view.txt_pro_desc.getText().trim());
                pro.setAddres(view.txt_pro_direc.getText().trim());
                pro.setTelephone(view.txt_pro_tel.getText().trim());
                pro.setEmail(view.txt_pro_emal.getText().trim());
                pro.setCity(view.Cm_pro_ciudad.getSelectedItem().toString().trim());
                if (pro_dao.registerProveedoresQuery(pro)) {
                    cleanFields();
                    cleanTable();
                    listAllProveedores();
                    JOptionPane.showMessageDialog(null, "el proveedor se registro con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "el proveedor no pudo registrarse");
                }
            }
        } else if (e.getSource() == view.btn_canecl_pro) {
            cleanFields();
            view.btn_registrar_pro.setEnabled(true);
        } else if (e.getSource() == view.btn_eliminar_pro) {
            int row = view.tbt_provee.getSelectedRow();
            int id = Integer.parseInt(view.tbt_provee.getValueAt(row, 0).toString());
            int quest = JOptionPane.showConfirmDialog(null, "Quieres eliminar al proveedor seleccionado?");
            if (quest == 0 && pro_dao.deleteProveedoresQuery(id) == true) {
                cleanFields();
                cleanTable();
                view.btn_registrar_pro.setEnabled(true);
                listAllProveedores();
                JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito");
            }
        } else if (e.getSource() == view.btn_mod_pro) {
            if (view.txt_pro_id.equals("")) {
                JOptionPane.showMessageDialog(null, "selecciona una fila para continuar");
            } else {
                //verificar si los campos estan vacios
                if (view.txt_pro_direc.getText().equals("")
                        || view.txt_pro_name.getText().equals("")
                        || view.Cm_pro_ciudad.getSelectedItem().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
                } else {
                    pro.setName(view.txt_pro_name.getText().trim());
                    pro.setDescription(view.txt_pro_desc.getText().trim());
                    pro.setAddres(view.txt_pro_direc.getText().trim());
                    pro.setTelephone(view.txt_pro_tel.getText().trim());
                    pro.setEmail(view.txt_pro_emal.getText().trim());
                    pro.setCity(view.Cm_pro_ciudad.getSelectedItem().toString().trim());
                    pro.setId(Integer.parseInt(view.txt_pro_id.getText().trim()));
                    if (pro_dao.updatedProveedoresQuery(pro)) {
                        cleanTable();
                        cleanFields();
                        listAllProveedores();
                        view.Register_emp.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "proveedor modificado con exito");
                    } else {
                        JOptionPane.showMessageDialog(null, "ocurrio un error al modificar al empleado");
                    }
                }
            }
        }
    }

    public void listAllProveedores() {
        List<Proveedores> list = pro_dao.listProveedoresQuery(view.txt_pro_buscar.getText());
        model = (DefaultTableModel) view.tbt_provee.getModel();
        Object[] row = new Object[7];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getName();
            row[2] = list.get(i).getDescription();
            row[3] = list.get(i).getAddres();
            row[4] = list.get(i).getTelephone();
            row[5] = list.get(i).getEmail();
            row[6] = list.get(i).getCity();
            model.addRow(row);
        }
        view.tbt_provee.setModel(model);
    }

    //limpiar campos
    public void cleanFields() {
        view.txt_pro_direc.setText("");
        view.txt_pro_tel.setText("");
        view.txt_pro_name.setText("");
        view.txt_pro_emal.setText("");
        view.txt_pro_desc.setText("");
        view.Cm_pro_ciudad.setSelectedItem("");
        view.txt_pro_id.setText("");
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.tbt_provee) {
            int row = view.tbt_provee.rowAtPoint(e.getPoint()); //esto es para saber en que fila se hizo click
            view.txt_pro_id.setText(view.tbt_provee.getValueAt(row, 0).toString());
            view.txt_pro_name.setText(view.tbt_provee.getValueAt(row, 1).toString());
            view.txt_pro_desc.setText(view.tbt_provee.getValueAt(row, 2).toString());
            view.txt_pro_direc.setText(view.tbt_provee.getValueAt(row, 3).toString());
            view.txt_pro_tel.setText(view.tbt_provee.getValueAt(row, 4).toString());
            view.txt_pro_emal.setText(view.tbt_provee.getValueAt(row, 5).toString());
            view.Cm_pro_ciudad.setSelectedItem(view.tbt_provee.getValueAt(row, 6).toString());

            //deshabilitar
            view.btn_registrar_pro.setEnabled(false);
        } else if (rol.equals("Administrador")) {
            if (e.getSource() == view.JProveed) { //al presionar el label te dirije al cliente
                view.jTabbedPane1.setSelectedIndex(5);
                cleanFields();
                cleanTable();
                listAllProveedores();
            }
        } else {
            view.jTabbedPane1.setEnabledAt(5, false);
            view.tbt_provee.setEnabled(false);
            JOptionPane.showMessageDialog(null, "no tenes permiso papurri");
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
        if (e.getSource() == view.txt_pro_buscar) {
            cleanTable();
            listAllProveedores();
        }
    }

    public void getProveedorName() {
        List<Proveedores> list = pro_dao.listProveedoresQuery(view.txt_pro_buscar.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            view.cmb_prove_pur.addItem(new dinamicComboBox(id, name));
        }
    }

}
