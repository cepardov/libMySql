/*
 * Copyright (C) 2015 cepardov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package conexion;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import javax.swing.JOptionPane;
import sistema.Service;

/**
 * @author cepardov <cepardov@gmail.com>
 */

public final class DataBaseInstance {
    private static Connection cn = null;
    private static Connection conn;
    
    public static String basedatosConf=null;
    public static String usuarioConf=null;
    public static String contraseñaConf=null;
    public static String hostConf=null;
    public static String portConf=null;
    
    private static final String basedatos = "neomarket-v2";//Nombre de base de datos
    private static final String usuario = "neomarket";//Usuario de base de datos
    private static final String contraseña = "neomarket2015";//Contraseña de base de datos
    private static final String host = "localhost";//host
    private static final String port="3306";//Puerto de base de datos
    
    //Driver
    private static final String driver="com.mysql.jdbc.Driver";
    
    //Conexión con bases de datos mysql
    private static final String url = "jdbc:mysql://"+host+"/"+basedatos;
    
    //Metodo conexion a base de datos [Seccion getInstanceConnection]
    public static Connection getInstanceConnection() {
        System.out.println("[i] Conectar base de datos mysql...");
        if (!(conn instanceof Connection)) {
            try {
                Class.forName(""+driver+"");
                conn = DriverManager.getConnection(url, usuario, contraseña);
                System.out.println("LISTO");
            } catch (ClassNotFoundException se) {
                System.out.println(" [!] Error: Módulo Base de datos  (Derby:getInstanceConnection)\n\tErr.:" + se);
                JOptionPane.showMessageDialog(null,"Error de controlador durante la comunicacion con base de datos "+basedatos+"\n\n\tDriver Instalado:\n\t"+driver+"\nCodigo de error:\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
                new Service().cerrarApp();
                System.exit(0);
            }catch (SQLException se) {
                System.out.println(" [!] Error: Módulo Base de datos  (Derby:getInstanceConnection)\n\tErr.:" + se + " Cod:"+se.getErrorCode());
                String msg = "";
                if(se.getErrorCode() == 1049){
                    msg = "Error en base de datos:\nLa base de datos: "+basedatos+" no existe.";
                }else if(se.getErrorCode() == 1044){
                    msg = "Error en base de datos:\nEl usuario: "+usuario+" no existe.";
                }else if(se.getErrorCode() == 1045){
                    msg = "Error en base de datos:\nContraseña incorrecta.";
                }else if(se.getErrorCode() == 0){
                    msg = "La coneccion con la base de datos no se puede realizar.\nParece que el servidor de base de datos no esta activo.";
                }
                JOptionPane.showMessageDialog(null, msg, "¡ups! Algo inesperado ha pasado", JOptionPane.ERROR_MESSAGE);
                new Service().cerrarApp();
            }
        }
        System.out.println(conn);
        return conn;
    }
    
    public static boolean conectar(){
        try{
            Class.forName(""+driver+"");

            String link = "jdbc:mysql://"+hostConf+":3306/?user="+usuarioConf+"&password="+contraseña;
            cn = DriverManager.getConnection(link);
            
        }catch(ClassNotFoundException ex){}        
        catch(SQLException ex){
            String msg = "";
            if(ex.getErrorCode() == 1049)
            {
                msg = "La base de datos: "+basedatosConf+" no existe.";
            }else if(ex.getErrorCode() == 1044)
            {
                msg = "El usuario: "+usuario+" no existe.";
            }else if(ex.getErrorCode() == 1045)
            {
                msg = "Contraseña incorrecta.";
            }else if(ex.getErrorCode() == 0)
            {
                msg = "La coneccion con la base de datos no se puede realizar.\nParece que el servidor de base de datos no esta activo.";
            }
            JOptionPane.showMessageDialog(null, msg, ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        
        if(cn != null)
        {
             System.out.println("Coneccion Exitosa.... XD");
             return true;
        }
        return false;
           
    }
    
    //Metodo conexion a base de datos [Seccion 45]
    public static void resultadoConexion(){
        JOptionPane.showMessageDialog(null,"Datos de la conexión:\n"
                + "URL:"+url+"\n"
                + "Puerto:"+port+"\n"
                + "Usuario:"+usuario+"\n"
                + "Codigo:\n"+conn,"Resultado de Pruebas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Metodo conexion a base de datos [Seccion 54]
    public static void resultadoDesconexion(){
        if(conn!=null){
            JOptionPane.showMessageDialog(null,"La conexion no ha terminado correctamente\n"
                    + "aun conectado con codigo:\n"
                    + conn+ "\n"
                    + "ha base de datos"+basedatos,"Resultado de Pruebas", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null,"La conexion ha terminado con exito","Resultado de Pruebas", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    //Metodo conexion a base de datos [Seccion 66]
    public static void closeConnection() {
        try {
            if (conn instanceof Connection) {
                conn.close();
                conn = null;
                System.out.println("[i] Base de datos desconectada con exito.");
            }

        } catch (SQLException se) {
            System.out.println(" [!] Error: Módulo Base de datos  (Derby:closeConnection)\n\tCod.:" + se);
            
            System.err.println("[i] Error al procesar cierre de conexion.");
            
            JOptionPane.showMessageDialog(null,"Error mientras se terminaba la comunicación con base de datos "+basedatos+"\nCodigo de error:\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.WARNING_MESSAGE);
            System.err.println(se.getMessage());
        }
    }
    /**
     * Verifica conexión a internet especificamente a base de datos
     * @return <b>true</b> si la conexion es verdadera | <b>false</b>
     * si la conexión es falsa.
     */
    public boolean internet(){
        try{
            Socket s = new Socket(host,Integer.parseInt(port));
            if(s.isConnected()){
                System.out.println("Conexión Internet="+s.isConnected());
                s.close();
                return true;//Existe conección internet
            } else {
                System.out.println("Conexión Internet="+s.isConnected());
                s.close();
                return false;//Sin Internet
            }
        } catch (NumberFormatException | IOException se) {
            System.out.println("Conexión Internet="+se);
            return false;//Sin Internet
        }
    }
}