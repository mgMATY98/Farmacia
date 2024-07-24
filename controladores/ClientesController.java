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
import modelo.Customers;
import modelo.CustomersDao;
import vista.SystemView;

/**
 *
 * @author Usuario
 */
public class ClientesController implements ActionListener, MouseListener, KeyListener {

    private final Customers cus;
    private final CustomersDao cus_dao;
    private final SystemView view;
    DefaultTableModel model = new DefaultTableModel(); //para interactuar con la tabla

    public ClientesController(Customers cus, CustomersDao cus_dao, SystemView view) {
        this.cus = cus;
        this.cus_dao = cus_dao;
        this.view = view;
        //poner en escucha el boton de registrar cliente
        this.view.btn_register_clie.addActionListener(this);
        //poner en escucha el boton de modificar cliente
        this.view.btn_comprar_clie.addActionListener(this);
        //tabla en escucha
        this.view.tbt_clie.addMouseListener(this);
        //buscador de la tabla
        this.view.txt_clie_buscar.addKeyListener(this);
        //escucha del boton eliminar
        this.view.btn_eliminar_clie.addActionListener(this);
        //escucha boton de cancelar
        this.view.btn_cancel_clie.addActionListener(this);
        //poner en escucha al label
        this.view.jLabelClientes.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btn_register_clie) {
            if (view.txt_clie_id.getText().equals("")
                    || view.txt_clie_adrees.getText().equals("")
                    || view.txt_clie_tel.getText().equals("")
                    || view.txt_clie_email.getText().equals("")
                    || view.txt_clie_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            } else { //realizar la insercion
                cus.setAdress(view.txt_clie_adrees.getText().trim());
                cus.setEmail(view.txt_clie_email.getText().trim());
                cus.setFull_name(view.txt_clie_name.getText().trim());
                cus.setId(Integer.parseInt(view.txt_clie_id.getText().trim()));
                cus.setTelephone(view.txt_clie_tel.getText().trim());
                if (cus_dao.registerCustomersQuery(cus)) {
                    cleanFields();
                    cleanTable();
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "el cliente se registro con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "fallo al registrar el cliente");
                }
            }
        }
        if (e.getSource() == view.btn_comprar_clie) {
            if (view.txt_clie_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "selecciona un cliente a modificar");
            } else {
                if (view.txt_clie_adrees.getText().equals("") || view.txt_clie_email.getText().equals("") || view.txt_clie_name.getText().equals("") || view.txt_clie_tel.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
                } else {
                    cus.setId(Integer.parseInt(view.txt_clie_id.getText().trim()));
                    cus.setEmail(view.txt_clie_email.getText().trim());
                    cus.setAdress(view.txt_clie_adrees.getText().trim());
                    cus.setFull_name(view.txt_clie_name.getText().trim());
                    cus.setTelephone(view.txt_clie_tel.getText().trim());
                    if (cus_dao.updatedCustomersQuery(cus)) {
                        cleanFields();
                        cleanTable();
                        listAllCustomers();
                        view.btn_register_clie.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "cliente modificado con exito");

                    } else {
                        JOptionPane.showMessageDialog(null, "ocurrio un error al modificar al empleado");
                    }
                }
            }
        } else if (e.getSource() == view.btn_eliminar_clie) {
            int row = view.tbt_clie.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "debes seleccionar un cliente a eliminar");
            } else {
                int id = Integer.parseInt(view.tbt_clie.getValueAt(row, 0).toString());
                int quest = JOptionPane.showConfirmDialog(null, "Quieres eliminar al cliente seleccionado?");
                if (quest == 0 && cus_dao.deleteCustomersQuery(id) == true) {
                    cleanFields();
                    cleanTable();
                    view.btn_register_clie.setEnabled(true);
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "cliente eliminado con exito");
                }
            }
        }else if(e.getSource()==view.btn_cancel_clie){
            cleanFields();
            view.btn_register_clie.setEnabled(true);
        }
    }

    //listar cliente
    public void listAllCustomers() {
        List<Customers> list = cus_dao.listCustomersQuery(view.txt_clie_buscar.getText());
        model = (DefaultTableModel) view.tbt_clie.getModel();
        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAdress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);
        }
        view.tbt_clie.setModel(model);
    }

    //limpiar campos
    public void cleanFields() {
        view.txt_clie_adrees.setText("");
        view.txt_clie_tel.setText("");
        view.txt_clie_name.setText("");
        view.txt_clie_email.setText("");
        view.txt_clie_id.setText("");
        view.txt_clie_id.setEditable(true);

    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.tbt_clie) {
            int row = view.tbt_clie.rowAtPoint(e.getPoint()); //esto es para saber en que fila se hizo click
            view.txt_clie_id.setText(view.tbt_clie.getValueAt(row, 0).toString());
            view.txt_clie_name.setText(view.tbt_clie.getValueAt(row, 1).toString());
            view.txt_clie_adrees.setText(view.tbt_clie.getValueAt(row, 2).toString());
            view.txt_clie_tel.setText(view.tbt_clie.getValueAt(row, 3).toString());
            view.txt_clie_email.setText(view.tbt_clie.getValueAt(row, 4).toString());
            //deshabilitar
            view.txt_clie_id.setEditable(false);
            view.btn_register_clie.setEnabled(false);
        }
        else if(e.getSource()==view.jLabelClientes){ //al presionar el label te dirije al cliente
            view.jTabbedPane1.setSelectedIndex(3);
            cleanFields();
            cleanTable();
            listAllCustomers();
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
        if (e.getSource() == view.txt_clie_buscar) {
            cleanTable();
            listAllCustomers();
        }
    }
}
