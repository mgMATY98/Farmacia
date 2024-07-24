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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static modelo.EmployeesDao.rol_user;
import static modelo.EmployeesDao.user_id;
import modelo.Products;
import modelo.ProductsDao;
import modelo.Purchases;
import modelo.PurchasesDao;
import modelo.dinamicComboBox;
import vista.Print;
import vista.SystemView;

public class ComprasController implements MouseListener, ActionListener, KeyListener {

    private final Purchases pur;
    private final PurchasesDao pur_dao;
    private final SystemView view;
    //instanciar el modelo productos
    Products pro = new Products();
    ProductsDao pro_dao = new ProductsDao();
    String rol = rol_user;
    private int id_sup = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    public ComprasController(Purchases pur, PurchasesDao pur_dao, SystemView view) {
        this.pur = pur;
        this.pur_dao = pur_dao;
        this.view = view;
        view.btn_pur_comprar.addActionListener(this);
        view.btn_pur_elim.addActionListener(this);
        view.btn_pur_nuevo.addActionListener(this);
        view.btn_pur_regis.addActionListener(this);
        view.jLabelCompras.addMouseListener(this);
        view.tbt_pur.addMouseListener(this);
        view.txt_purcha_codP.addKeyListener(this);
        view.txt_purcha_precio.addMouseListener(this);
        this.view.jLabelRep.addMouseListener(this);
    }

    private void insertCompra() {
        double total = Double.parseDouble(view.txt_purcha_total.getText());
        int emp_id = user_id;
        if (pur_dao.registerPurchasesQuery(id_sup, emp_id, total)) {
            int pur_id = pur_dao.PurchaseId();
            for (int i = 0; i < view.tbt_pur.getRowCount(); i++) {
                int pro_id = Integer.parseInt(view.tbt_pur.getValueAt(i, 0).toString());
                int pur_cant = Integer.parseInt(view.tbt_pur.getValueAt(i, 2).toString());
                double pur_price = Double.parseDouble(view.tbt_pur.getValueAt(i, 3).toString());
                double pur_sub = pur_cant * pur_price;

                //registrar detalle de la compra
                pur_dao.registerPurchasesDetail(pur_id, pur_price, pur_cant, pur_sub, pro_id);
                //traer la cantidad de productos
                pro = pro_dao.searchID(pur_id);
                int cant = pro.getProduct_cant() + pur_cant;
                pro_dao.updatedStockQuery(cant, pro_id);
            }
            cleanTableTemp();
            JOptionPane.showMessageDialog(null, "compra generada con exito");
            cleanFields();
            listAllCompras();
            Print print= new Print(pur_id);
            print.setVisible(true);
        }
    }

    //metodo para listar las compras realizadas
    public void listAllCompras() {
        if (rol.equals("Administrador") || (rol.equals("Auxiliar"))) {
            List<Purchases> list = pur_dao.listAllPurchasesQuery();
            model = (DefaultTableModel) view.tbt_rep_compras.getModel();
            Object[] row = new Object[4];
            //recorrer
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSupplier_name_product();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCreated();
                model.addRow(row);
            }
            view.tbt_rep_compras.setModel(model);
        }
    }

    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btn_pur_regis) {
            dinamicComboBox prove_cmb_id = (dinamicComboBox) view.cmb_prove_pur.getSelectedItem(); //obtengo la lista de proveedores
            int prove_id = prove_cmb_id.getId(); //asigno el id del proveedor a una varible
            if (id_sup == 0) {
                id_sup = prove_id; //si no seleeciono un proveedor se le asigna 1, id es 0 por lo tanto no tiene nada seleccionado
            } else if (id_sup != prove_id) {
                JOptionPane.showMessageDialog(null, "no puede realizar una misma compra a varios proveedores");
            } else {
                int cant = Integer.parseInt(view.txt_purcha_cant.getText());
                String pro_name = view.txt_purcha_nomP.getText();
                Double precio = Double.parseDouble(view.txt_purcha_precio.getText());
                int id = Integer.parseInt(view.txt_purcha_id.getText());
                String sup_name = view.cmb_prove_pur.getSelectedItem().toString();
                if (cant > 0) {
                    temp = (DefaultTableModel) view.tbt_pur.getModel();
                    for (int i = 0; i < view.tbt_pur.getRowCount(); i++) {
                        if (view.tbt_pur.getValueAt(i, 1).equals(view.txt_purcha_nomP.getText())) {
                            JOptionPane.showMessageDialog(null, "el producto ya esta registrado en la tabla de compras");
                            return;
                        }

                    }
                    ArrayList list = new ArrayList();
                    item = 1;
                    list.add(item);
                    list.add(id);
                    list.add(pro_name);
                    list.add(cant);
                    list.add(precio);
                    list.add(cant * precio);
                    list.add(sup_name);

                    Object[] obj = new Object[6];
                    obj[0] = list.get(1);
                    obj[1] = list.get(2);
                    obj[2] = list.get(3);
                    obj[3] = list.get(4);
                    obj[4] = list.get(5);
                    obj[5] = list.get(6);
                    temp.addRow(obj);
                    view.tbt_pur.setModel(temp);
                    cleanFields();
                    view.cmb_prove_pur.setEditable(false);
                    view.txt_purcha_codP.requestFocus();
                    calcularPrecio();

                }
            }
        } else if (e.getSource() == view.btn_pur_comprar) {
            insertCompra();
        } else if (e.getSource() == view.btn_pur_elim) {
            model = (DefaultTableModel) view.tbt_pur.getModel();
            model.removeRow(view.tbt_pur.getSelectedRow());
            calcularPrecio();
            view.txt_purcha_codP.requestFocus();
        } else if (e.getSource() == view.btn_pur_nuevo) {
            cleanFields();
            cleanTableTemp();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.txt_purcha_precio) {
            int cant;
            double precio = Double.parseDouble(view.txt_purcha_precio.getText());
            if (view.txt_purcha_cant.getText().equals("")) {
                cant = 1;
                view.txt_purcha_sub.setText("" + precio);
            } else {
                cant = Integer.parseInt(view.txt_purcha_cant.getText());
                view.txt_purcha_sub.setText("" + precio * cant);
            }
        } else if (e.getSource() == view.jLabelCompras) {
            if (rol.equals("Administrador")) {
                view.jTabbedPane1.setSelectedIndex(1);
                //limpiar tabla y campos
                cleanTable();
                cleanFields();
            } else {
                view.jTabbedPane1.setEnabledAt(1, false);
                view.jLabelCompras.setEnabled(false);
                JOptionPane.showMessageDialog(null, "no sos el administrador para acceder perri");
            }
        } else if (e.getSource() == view.jLabelRep) {
                view.jTabbedPane1.setSelectedIndex(7);
                cleanTable();
                listAllCompras();
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
        if (e.getSource() == view.txt_purcha_codP) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { //si la persona presion enter realiza lo siguiente
                if (view.txt_purcha_codP.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "ingresa el codigo del producto a comprar");
                } else {
                    int id = Integer.parseInt(view.txt_purcha_codP.getText());
                    pro = pro_dao.searchCodeQuery(id);
                    view.txt_purcha_nomP.setText(pro.getName());
                    view.txt_purcha_id.setText("" + pro.getId());
                    view.txt_purcha_precio.setText("" + pro.getUnit_price());
                    view.txt_purcha_cant.requestFocus();
                }
            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void cleanFields() {
        view.txt_purcha_cant.setText("");
        view.txt_purcha_codP.setText("");
        view.txt_purcha_id.setText("");
        view.txt_purcha_nomP.setText("");
        view.txt_purcha_precio.setText("");
        view.txt_purcha_sub.setText("");
        view.txt_purcha_total.setText("");
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    //calcular total a pagar
    public void calcularPrecio() {
        double total = 0.0;
        int numRow = view.tbt_pur.getRowCount();
        for (int i = 0; i < numRow; i++) {
            //indice de la columna que se sumara
            total = total + Double.parseDouble(String.valueOf(view.tbt_pur.getValueAt(i, 4)));
        }
        view.txt_purcha_total.setText("" + total);
    }
}
