
package controladores;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static modelo.EmployeesDao.adress_user;
import static modelo.EmployeesDao.email_user;
import static modelo.EmployeesDao.full_name_user;
import static modelo.EmployeesDao.telephone_user;
import static modelo.EmployeesDao.user_id;
import vista.SystemView;

public class SettingsController implements MouseListener{
    private SystemView view;
    
    public SettingsController(SystemView view){
        this.view=view;
        this.view.jLabelClientes.addMouseListener(this);
        this.view.jLabelCate.addMouseListener(this);
        this.view.jLabelCompras.addMouseListener(this);
        this.view.JConfiguraciones.addMouseListener(this);
        this.view.jLabelEmpleados.addMouseListener(this);
        this.view.jLabelProductos.addMouseListener(this);
        this.view.JProveed.addMouseListener(this);
        this.view.jLabelRep.addMouseListener(this);
        this.view.jlabelVentas.addMouseListener(this);
        profile();
    }

    public SystemView getView() {
        return view;
    }

    public void setView(SystemView view) {
        this.view = view;
    }
    
    //asignar el perfil del usuario
    public void profile(){
        this.view.txt_id_perfil.setText(""+user_id);
        this.view.txt_direc_perfil.setText(adress_user);
        this.view.txt_email_perfil.setText(email_user);
        this.view.txt_tel_perfil.setText(telephone_user);
        this.view.txt_name_perfil.setText(full_name_user);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource()==view.jLabelProductos){
            view.Productos.setBackground(Color.RED);
        }
        else if(e.getSource()==view.JProveed){
            view.Proveedores.setBackground(Color.RED);
        }
        else if(e.getSource()==view.jLabelCate){
            view.Categorias.setBackground(Color.RED);
        }
        else if(e.getSource()==view.jLabelClientes){
            view.Clientes.setBackground(Color.RED);
        }
        else if(e.getSource()==view.jlabelVentas){
            view.Ventas.setBackground(Color.RED);
        }
        else if(e.getSource()==view.jLabelCompras){
            view.Compras.setBackground(Color.RED);
        }
        else if(e.getSource()==view.JConfiguraciones){
            view.Configuraciones.setBackground(Color.RED);
        }
        else if(e.getSource()==view.jLabelEmpleados){
            view.Empleados.setBackground(Color.RED);
        }
        else if(e.getSource()==view.jLabelRep){
            view.Reportes.setBackground(Color.RED);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
          if(e.getSource()==view.jLabelProductos){
            view.Productos.setBackground(new Color(0,0,255));
        }
        else if(e.getSource()==view.JProveed){
          view.Proveedores.setBackground(new Color (0,0,255));
        }
        else if(e.getSource()==view.jLabelCate){
            view.Categorias.setBackground(new Color(0,0,255));
        }
        else if(e.getSource()==view.jlabelVentas){
            view.Ventas.setBackground(new Color (0,0,255));
        }
        else if(e.getSource()==view.jLabelClientes){
            view.Clientes.setBackground(new Color(0,0,255));
        }
        else if(e.getSource()==view.jLabelCompras){
            view.Compras.setBackground(new Color(0,0,255));
        }
        else if(e.getSource()==view.JConfiguraciones){
            view.Configuraciones.setBackground(new Color(0,0,255));
        }
        else if(e.getSource()==view.jLabelEmpleados){
            view.Empleados.setBackground(new Color(0,0,255));
        }
        else if(e.getSource()==view.jLabelRep){
            view.Reportes.setBackground(new Color(0,0,255));
        }
    }
    
}
