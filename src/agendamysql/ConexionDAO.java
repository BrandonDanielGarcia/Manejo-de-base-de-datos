/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agendamysql;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class ConexionDAO {
    Connection conexion = null;
    List<DatosDTO> listaDatos = new ArrayList<>();
    private void Conecta(){
        String user = "root";
        String password = "teotongo21";
        String url = "jdbc:mysql://localhost:3306/4to20201?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean Inserta(DatosDTO datos){
        boolean estado = true;
        try {
            Conecta();
            PreparedStatement ps = conexion.prepareStatement("insert into datos(nombre, edad, sexo) values(?,?,?)");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.execute();
        } catch (Exception ex) {
            estado = false;
        }finally {
            Cerrar();
        }
        return estado;
    }
    
    public boolean Cargar(){
        boolean estado = true;
        DatosDTO datos;
        try {
            Conecta();
            PreparedStatement ps = conexion.prepareStatement("select*from datos");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
                datos = new DatosDTO();
                datos.setId(resultado.getInt("id"));
                datos.setNombre(resultado.getString("nombre"));
                datos.setEdad(resultado.getString("edad"));
                datos.setSexo(resultado.getString("sexo"));
                listaDatos.add(datos);
            }
        } catch (SQLException ex) {
            estado = false;
        }finally{
            Cerrar();
        }
        return estado;
    }
    
    public boolean Actualiza(DatosDTO datos){
        boolean estado= true;
        try {
            Conecta();
            PreparedStatement ps = conexion.prepareStatement("update datos set nombre = ?, edad = ?, sexo = ? where id = ?");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.setInt(4, datos.getId());
            ps.execute();
        } catch (SQLException ex) {
            estado = false;
        }finally{
            Cerrar();
        }
        return estado;
    }
    
    public boolean Eliminar(DatosDTO datos){
        boolean estado = true;
        try {
            Conecta();
            PreparedStatement ps = conexion.prepareStatement("delete from datos where id = ?");
            ps.setInt(1, datos.getId());
            ps.execute();
        } catch (SQLException e) {
            estado = false;
        }finally{
            Cerrar();
        }
        return estado;
    }
    
    public List<DatosDTO> GetDatos(){
        return listaDatos;
    }
    
    private void Cerrar(){
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

