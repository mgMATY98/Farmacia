/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import modelo.Sales;

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
import modelo.Customers;
import modelo.CustomersDao;
import modelo.Employees;
import modelo.Sales;
import static modelo.EmployeesDao.rol_user;
import static modelo.EmployeesDao.user_id;
import modelo.Products;
import modelo.ProductsDao;
import modelo.Purchases;
import modelo.PurchasesDao;
import modelo.SalesDao;
import modelo.dinamicComboBox;
import vista.Print;
import vista.SystemView;

public class VentasController implements ActionListener, MouseListener, KeyListener {

    private Sales sale;
    private SalesDao saleDao;
    private SystemView views;
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();
    private int item = 0;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    public VentasController(Sales sale, SalesDao saleDao, SystemView views) {
        this.sale = sale;
        this.saleDao = saleDao;
        this.views = views;
        this.views.btn_sale_agregar.addActionListener(this);
        this.views.btn_sale_eliminar.addActionListener(this);
        this.views.btn_sale_nuevo.addActionListener(this);
        this.views.btn_sale_vender.addActionListener(this);
        this.views.jlabelVentas.addMouseListener(this);
        this.views.tbt_sale.addMouseListener(this);
        this.views.txt_sale_cod.addKeyListener(this);
        this.views.txt_sale_cedula.addKeyListener(this);
        this.views.txt_sale_cant.addKeyListener(this);
    }

    public void cleanFieldsSales() {
        views.txt_sale_cod.setText("");
        views.txt_sale_nombreP.setText("");
        views.txt_sale_cant.setText("");
        views.txt_sale_id.setText("");
        views.txt_sale_precio.setText("");
        views.txt_sale_subT.setText("");
        views.txt_sale_stock.setText("");
    }

    public void cleanAllFields() {
        views.txt_sale_cant.setText("");
        views.txt_sale_cedula.setText("");
        views.txt_sale_cod.setText("");
        views.txt_sale_id.setText("");
        views.txt_sale_nombreC.setText("");
        views.txt_sale_nombreP.setText("");
        views.txt_sale_precio.setText("");
        views.txt_sale_stock.setText("");
        views.txt_sale_subT.setText("");
        views.txt_sale_total.setText("");
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    private void insertVenta() {

        int customer_id = Integer.parseInt(views.txt_sale_cedula.getText());
        int emp_id = user_id;
        double total = Double.parseDouble(views.txt_sale_total.getText());

        if (saleDao.registerSaleQuery(customer_id, emp_id, total)) {

            Products pro = new Products();
            ProductsDao proDao = new ProductsDao();
            int sale_id = saleDao.maxId();

            // registerPurchaseDetailQuery();
            for (int i = 0; i < views.tbt_sale.getRowCount(); i++) {
                int product_id = Integer.parseInt(views.tbt_sale.getValueAt(i, 0).toString());
                int sale_quantity = Integer.parseInt(views.tbt_sale.getValueAt(i, 2).toString());
                double sale_price = Double.parseDouble(views.tbt_sale.getValueAt(i, 3).toString());
                double sale_subtotal = sale_quantity * sale_price;

                saleDao.registerSaleDetailQuery(product_id, sale_id, sale_quantity, sale_price, sale_subtotal);

                //Traer la cantidad de productos
                pro = proDao.searchID(product_id);
                //Obtener cantidad actual y restar la cantidad comprada
                int amount = pro.getProduct_cant() - sale_quantity;
                proDao.updatedStockQuery(amount, product_id);
            }
            JOptionPane.showMessageDialog(null, "Venta generada");
            cleanTableTemp();
            cleanAllFields();
            listAllSales();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_sale_vender) {
            insertVenta();

        } else if (e.getSource() == views.btn_sale_nuevo) {
            cleanAllFields();
            cleanTableTemp();

        } else if (e.getSource() == views.btn_sale_eliminar) {
            model = (DefaultTableModel) views.tbt_sale.getModel();
            model.removeRow(views.tbt_sale.getSelectedRow());
            calcularVentas();
            views.txt_sale_cod.requestFocus();

        } else if (!"".equals(e.getSource() == views.btn_sale_agregar)) {

            //Agregar productos a la tabla de ventas temporalmente
            int amount = Integer.parseInt(views.txt_sale_cant.getText());
            String product_name = views.txt_sale_nombreP.getText();
            double price = Double.parseDouble(views.txt_sale_precio.getText());
            int sale_id = Integer.parseInt(views.txt_sale_id.getText());
            double subtotal = amount * price;
            int stock = Integer.parseInt(views.txt_sale_stock.getText());
            String full_name = views.txt_sale_nombreC.getText();

            if (stock >= amount) {
                item = item + 1;
                temp = (DefaultTableModel) views.tbt_sale.getModel();

                for (int i = 0; i < views.tbt_sale.getRowCount(); i++) {
                    if (views.tbt_sale.getValueAt(i, 1).equals(views.txt_sale_nombreP.getText())) {
                        JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de ventas");
                        return;
                    }
                }

                ArrayList list = new ArrayList();
                list.add(item);
                list.add(sale_id);
                list.add(product_name);
                list.add(amount);
                list.add(price);
                list.add(subtotal);
                list.add(full_name);

                Object[] obj = new Object[6];
                obj[0] = list.get(1);
                obj[1] = list.get(2);
                obj[2] = list.get(3);
                obj[3] = list.get(4);
                obj[4] = list.get(5);
                obj[5] = list.get(6);

                temp.addRow(obj);
                views.tbt_sale.setModel(temp);
                calcularVentas();
                cleanFieldsSales();
                views.txt_sale_cod.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Stock no disponible");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
        }
    }

    //Calcular total a pagar tabla de ventas
    private void calcularVentas() {
        double total = 0.00;
        int numRow = views.tbt_sale.getRowCount();
        for (int i = 0; i < numRow; i++) {
            total = total + Double.parseDouble(String.valueOf(views.tbt_sale.getValueAt(i, 4)));
        }
        views.txt_sale_total.setText("" + total);
    }

    //Listar todas las ventas
    public void listAllSales() {
        if (rol.equals("Administrador")) {
            List<Sales> list = saleDao.listSalesQuery();
            model = (DefaultTableModel) views.tbt_rep_ventas.getModel();

            //Recorrer la lista
            Object[] row = new Object[5];

            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCustomer_name();
                row[2] = list.get(i).getEmployee_name();
                row[3] = list.get(i).getTotal_to_pay();
                row[4] = list.get(i).getSale_date();
                model.addRow(row);
            }
            views.tbt_rep_ventas.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.jlabelVentas) {
            if (rol.equals("Administrador")) {
                views.jTabbedPane1.setSelectedIndex(2);
                //limpiar tabla y campos
                cleanTable();
                cleanAllFields();
            } else {
                views.jTabbedPane1.setEnabledAt(2, false);
                views.jLabelCompras.setEnabled(false);
                JOptionPane.showMessageDialog(null, "no sos el administrador para acceder perri");
            }
        } else if (e.getSource() == views.jLabelRep) {
            views.jTabbedPane1.setSelectedIndex(7);
            cleanTable();
            listAllSales();
        } else if (e.getSource() == views.jlabelVentas) {
            views.jTabbedPane1.setSelectedIndex(2);
        } else if (e.getSource() == views.jLabelRep) {
            if (rol.equals("Administrador")) {
                views.jTabbedPane1.setSelectedIndex(7);
                listAllSales();
            } else {
                views.jTabbedPane1.setEnabledAt(7, false);
                views.jLabelRep.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de administrador para acceder a esta vista");
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
        if (e.getSource() == views.txt_sale_cod) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!"".equals(views.txt_sale_cod.getText())) {

                    int code = Integer.parseInt(views.txt_sale_cod.getText());
                    product = productDao.searchCodeQuery(code);

                    if (product.getName() != null) {
                        views.txt_sale_nombreP.setText(product.getName());
                        views.txt_sale_id.setText("" + product.getId());
                        views.txt_sale_stock.setText("" + product.getProduct_cant());
                        views.txt_sale_precio.setText("" + product.getUnit_price());
                        views.txt_sale_cant.requestFocus();

                    } else {
                        JOptionPane.showMessageDialog(null, "No existe ningún producto con ese código");
                        cleanFieldsSales();
                        views.txt_sale_cod.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese el código del producto a vender");
                }
            }
        } else if (e.getSource() == views.txt_sale_cedula) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                Customers customer = new Customers();
                CustomersDao customerDao = new CustomersDao();

                if (!"".equals(views.txt_sale_cedula.getText())) {
                    int customer_id = Integer.parseInt(views.txt_sale_cedula.getText());
                    customer = customerDao.searchCustomers(customer_id);

                    if (customer.getFull_name() != null) {
                        views.txt_sale_nombreC.setText("" + customer.getFull_name());
                    } else {
                        views.txt_sale_cedula.setText("");
                        JOptionPane.showMessageDialog(null, "El cliente no existe");
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_sale_cant) {

            int quantity;
            double price = Double.parseDouble(views.txt_sale_precio.getText());

            if (views.txt_sale_cant.getText().equals("")) {
                quantity = 1;
                views.txt_sale_precio.setText("" + price);
            } else {
                quantity = Integer.parseInt(views.txt_sale_cant.getText());
                price = Double.parseDouble(views.txt_sale_precio.getText());
                views.txt_sale_subT.setText("" + quantity * price);
            }
        }
    }

}
