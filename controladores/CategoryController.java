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
import modelo.Categories;
import modelo.CategoriesDao;
import static modelo.EmployeesDao.rol_user;
import modelo.dinamicComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import vista.SystemView;

public class CategoryController implements ActionListener, MouseListener, KeyListener {

    private final Categories cat;
    private final CategoriesDao cat_dao;
    SystemView view;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public CategoryController(Categories cat, CategoriesDao cat_dao, SystemView view) {
        this.cat = cat;
        this.cat_dao = cat_dao;
        this.view = view;
        this.view.btn_cate_can.addActionListener(this);
        this.view.btn_cate_elim.addActionListener(this);
        this.view.btn_cate_mod.addActionListener(this);
        this.view.btn_cate_regis.addActionListener(this);
        this.view.jLabelCate.addMouseListener(this);
        this.view.tbt_cate.addMouseListener(this);
        this.view.txt_cate_buscar.addKeyListener(this);
        getCategoryName();
        AutoCompleteDecorator.decorate(view.cmb_product_category);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btn_cate_regis) {
            if (((view.txt_cate_name.equals("")))) {
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            } else {
                cat.setName(view.txt_cate_name.getText().trim());

                if (cat_dao.registerCategoriesQuery(cat)) {
                    cleanTable();
                    cleanFields();
                    listAllCategories();
                    view.Register_emp.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "categoria registrada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "ocurrio un error al registrar la categoria");
                }
            }
        } else if (e.getSource() == view.btn_cate_can) {
            cleanFields();
            cleanTable();
            view.btn_cate_regis.setEnabled(true);
            listAllCategories();
        } else if (e.getSource() == view.btn_cate_mod) {
            if (view.txt_cate_id.equals("")) {
                JOptionPane.showMessageDialog(null, "debe seleccionar una fila");
            } else {
                cat.setId(Integer.parseInt(view.txt_cate_id.getText().trim()));
                cat.setName(view.txt_cate_name.getText().trim());
                if (cat_dao.updateCategoriesQuery(cat)) {
                    cleanFields();
                    cleanTable();
                    listAllCategories();
                    view.btn_cate_regis.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "categoria modificada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "ocurrio un error al rmodificar la categoria");
                }
            }
        } else if (e.getSource() == view.btn_cate_elim) {
            int row = view.tbt_cate.getSelectedRow();
            int id = Integer.parseInt(view.tbt_cate.getValueAt(row, 1).toString());
            int quest = JOptionPane.showConfirmDialog(null, "Quieres eliminar la categoria seleccionado?");
            if (quest == 0 && cat_dao.deleteCategoriesQuery(id) == true) {
                cleanFields();
                cleanTable();
                view.btn_cate_regis.setEnabled(true);
                listAllCategories();
                JOptionPane.showMessageDialog(null, "categoria eliminado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "ocurrio un error al eliminar la categoria");
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.tbt_cate) {
            int row = view.tbt_cate.rowAtPoint(e.getPoint());
            view.txt_cate_name.setText(view.tbt_cate.getValueAt(row, 0).toString().trim());
            view.txt_cate_id.setText(view.tbt_cate.getValueAt(row, 1).toString().trim());
            view.btn_cate_regis.setEnabled(false);
        } else if (e.getSource() == view.jLabelCate) { //al presionar el label te dirije al cliente
            view.jTabbedPane1.setSelectedIndex(6);
            cleanFields();
            cleanTable();
            listAllCategories();
        } else {
            view.jTabbedPane1.setEnabledAt(6, false);
            view.tbt_cate.setEnabled(false);
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
        if(e.getSource()==view.txt_cate_buscar){
            cleanTable();
            listAllCategories();
        }
    }

    public void listAllCategories() {
        List<Categories> list = cat_dao.listCategorieQuery(view.txt_cate_buscar.getText());
        model = (DefaultTableModel) view.tbt_cate.getModel();
        Object[] row = new Object[2];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getName();
            row[1] = list.get(i).getId();
            model.addRow(row);
        }
        view.tbt_cate.setModel(model);
    }

    public void cleanFields() {
        view.txt_cate_id.setText("");
        view.txt_cate_name.setText("");
        view.txt_id.setEditable(true);

    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    //metodo para mostrar el nombre de las categorias
    public void getCategoryName() {
        List<Categories> list = cat_dao.listCategorieQuery(view.txt_cate_buscar.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            view.cmb_product_category.addItem(new dinamicComboBox(id, name));
        }
    }
}
