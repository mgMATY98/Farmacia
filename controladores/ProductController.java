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
import modelo.Products;
import modelo.ProductsDao;
import modelo.Proveedores;
import modelo.ProveedoresDao;
import modelo.dinamicComboBox;
import vista.SystemView;

public class ProductController implements KeyListener, MouseListener, ActionListener {

    Products pro;
    ProductsDao pro_dao;
    String rol = rol_user;
    SystemView view;
    DefaultTableModel model = new DefaultTableModel();

    public ProductController(Products pro, ProductsDao pro_dao, SystemView view) {
        this.pro = pro;
        this.pro_dao = pro_dao;
        this.view = view;
        this.view.btn_cancel_product.addActionListener(this);
        this.view.btn_update_product.addActionListener(this);
        this.view.btn_deleted_product.addActionListener(this);
        this.view.btn_registred_product.addActionListener(this);
        this.view.txt_search_product.addKeyListener(this);
        this.view.jLabelProductos.addMouseListener(this);
        this.view.tbt_product.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btn_registred_product) {
            if (view.txt_product_code.getText().equals("")
                    || view.txt_product_desc.getText().equals("")
                    || view.txt_product_name.getText().equals("")
                    || view.txt_product_precio.getText().equals("")
                    || view.cmb_product_category.getSelectedItem().equals("")) {
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            } else {
                pro.setCode(Integer.parseInt(view.txt_product_code.getText().trim()));
                pro.setDescription(view.txt_product_desc.getText().trim());
                pro.setName(view.txt_product_name.getText().trim());
                pro.setUnit_price(Double.parseDouble(view.txt_product_precio.getText().trim()));
                dinamicComboBox cat_id = (dinamicComboBox) view.cmb_product_category.getSelectedItem();
                pro.setCategory_id(cat_id.getId());
                if (pro_dao.registerProductsQuery(pro)) {
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "producto registrado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "producto registrado con ERROR");

                }
            }
        } else if (e.getSource() == view.btn_cancel_product) {
            cleanFields();
            cleanTable();
            listAllProducts();
            view.btn_registred_product.setEnabled(true);
        } else if (e.getSource() == view.btn_deleted_product) {
            int row = view.tbt_product.getSelectedRow();
            int id = Integer.parseInt(view.tbt_product.getValueAt(row, 0).toString());
            int quest = JOptionPane.showConfirmDialog(null, "Quieres eliminar la categoria seleccionado?");
            if (quest == 0 && pro_dao.deleateProductQuery(id) == true) {
                cleanFields();
                cleanTable();
                view.btn_registred_product.setEnabled(true);
                listAllProducts();
                JOptionPane.showMessageDialog(null, "producto eliminado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "ocurrio un error al eliminar el producto");
            }
        } else if (e.getSource() == view.btn_update_product) {
            if (view.txt_product_code.getText().equals("")
                    || view.txt_product_desc.getText().equals("")
                    || view.txt_product_name.getText().equals("")
                    || view.txt_product_precio.getText().equals("")
                    || view.cmb_product_category.getSelectedItem().equals("")) {

                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            } else {
                pro.setCode(Integer.parseInt(view.txt_product_code.getText().trim()));
                pro.setDescription(view.txt_product_desc.getText().trim());
                pro.setName(view.txt_product_name.getText().trim());
                pro.setUnit_price(Double.parseDouble(view.txt_product_precio.getText().trim()));
                dinamicComboBox cat_id = (dinamicComboBox) view.cmb_product_category.getSelectedItem();
                pro.setCategory_id(cat_id.getId());
                pro.setId(Integer.parseInt(view.txt_product_id.getText()));
                if (pro_dao.updatedProductsQuery(pro)) {
                    cleanFields();
                    cleanTable();
                    listAllProducts();
                    view.btn_registred_product.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "producto modificada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "ocurrio un error al rmodificar el producto");
                }
            }
        }
    }

    public void listAllProducts() {
        if (rol.equals("Administrador") || (rol.equals("Auxiliar"))) {
            List<Products> list = pro_dao.listProductsQuery(view.txt_search_product.getText());
            model = (DefaultTableModel) view.tbt_product.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCode();
                row[2] = list.get(i).getName();
                row[3] = list.get(i).getDescription();
                row[4] = list.get(i).getUnit_price();
                row[5] = list.get(i).getProduct_cant();
                row[6] = list.get(i).getCategory_name();
                model.addRow(row);
            }
            view.tbt_product.setModel(model);
            if (rol.equals("Auxiliar")) {
                view.btn_registred_product.setEnabled(false);
                view.btn_cancel_product.setEnabled(false);
                view.btn_deleted_product.setEnabled(false);
                view.btn_update_product.setEnabled(false);
                view.txt_product_code.setEditable(false);
                view.txt_product_desc.setEditable(false);
                view.txt_product_name.setEditable(false);
                view.txt_product_precio.setEditable(false);
                view.txt_product_id.setEditable(false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == view.txt_search_product) {
            cleanTable();
            listAllProducts();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.tbt_product) {
            int row = view.tbt_product.rowAtPoint(e.getPoint());
            view.txt_product_id.setText(view.tbt_product.getValueAt(row, 0).toString().trim());
            pro = pro_dao.searchProductsQuery(Integer.parseInt(view.txt_product_id.getText()));
            view.txt_product_code.setText("" + pro.getCode());
            view.txt_product_name.setText("" + pro.getName());
            view.txt_product_desc.setText("" + pro.getDescription());
            view.txt_product_precio.setText("" + pro.getUnit_price());
            view.cmb_product_category.setSelectedItem(new dinamicComboBox(pro.getCategory_id(), pro.getCategory_name()));
            view.btn_registred_product.setEnabled(false);
        } else if (e.getSource() == view.jLabelProductos) { //al presionar el label te dirije al cliente
            view.jTabbedPane1.setSelectedIndex(0);
            cleanFields();
            cleanTable();
            listAllProducts();
        } else {
            view.jTabbedPane1.setEnabledAt(6, false);
            view.tbt_product.setEnabled(false);
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

    public void cleanFields() {
        view.txt_product_code.setText("");
        view.txt_product_desc.setText("");
        view.txt_product_id.setText("");
        view.txt_product_name.setText("");
        view.txt_product_precio.setText("");
        view.cmb_product_category.setSelectedItem("");

    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

}
