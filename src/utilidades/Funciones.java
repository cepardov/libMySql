

package utilidades;

import cl.cepardov.encriptar.Decript;
import cl.cepardov.encriptar.Encript;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author cepardov
 */
public class Funciones {
    String DIR_CONFIG = ".neomarket";
    String FICHERO_CONFIGURACION = "mysql.dll";
    Properties propiedades;
    String dato;
    int i = 0;
    //6

    public void crearConf(String version, String host, String port, String baseDatos, String usuario, String clave){
        System.out.println("Ejecutando gestor de ditectorios...");
        File directorio = new File(SystemUtils.USER_HOME+SystemUtils.FILE_SEPARATOR+DIR_CONFIG);
        while(directorio.exists()!=true){
            directorio.mkdirs();
            i++;
            System.out.println("Intento de escritura N°="+i);
            if (i==3) {
                JOptionPane.showMessageDialog(null,"Error al intentar escribir en disco.","Error de ejecución", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        this.escribirConf(version, host, port, baseDatos,usuario,clave);
    }

    private void escribirConf(String version, String host, String port, String baseDatos, String usuario, String clave){
        String encVersion=Encript.Encriptar(version);
        String encHost=Encript.Encriptar(host);
        String encPort=Encript.Encriptar(port);
        String encBaseDatos=Encript.Encriptar(baseDatos);
        String encUsuario=Encript.Encriptar(usuario);
        String encClave=Encript.Encriptar(clave);
        
        try{
        File archivo=new File(SystemUtils.USER_HOME+SystemUtils.FILE_SEPARATOR+DIR_CONFIG+SystemUtils.FILE_SEPARATOR+FICHERO_CONFIGURACION);
        archivo.delete();
        archivo.createNewFile();
        FileWriter escribir=new FileWriter(archivo,true);
        /*
        0=VERSION
        1=HOST
        2=PORT
        3=BASEDATOS
        4=USUARIO
        5=CLAVE
        */
        escribir.write("#ATENCION MODIFICAR ESTE ARCHIVO PROVOCARA QUE LA APLICACION DEJE DE FUNCIONAR---\n"
                + "0="+encVersion+"\n"
                + "1="+encHost+"\n"
                + "2="+encPort+"\n"
                + "3="+encBaseDatos+"\n"
                + "4="+encUsuario+"\n"
                + "5="+encClave);
        escribir.close();
        JOptionPane.showMessageDialog(null,"La configuración se ha guardado con exito.", "Gestor de Configuración", JOptionPane.INFORMATION_MESSAGE);
//        new Logger().Logger("Se crea nueva serie\nGuardo en:\n"+SystemUtils.USER_HOME+SystemUtils.FILE_SEPARATOR+DIR_CONFIG+SystemUtils.FILE_SEPARATOR+FICHERO_CONFIGURACION+"\n");
        }
        catch (IOException | HeadlessException se){
//            JOptionPane.showMessageDialog(null,"No se ha creado el archivo que contiene el UUID de software\ncorrectamente.\n\n"+se, "¡ups! Algo inesperado ha pasado", JOptionPane.WARNING_MESSAGE);
            System.out.println("Error crear archivo:"+se);
        }
    }
    
    public String getDatos(int propiedad){
        try{
            FileInputStream f = new FileInputStream(SystemUtils.USER_HOME+SystemUtils.FILE_SEPARATOR+DIR_CONFIG+SystemUtils.FILE_SEPARATOR+FICHERO_CONFIGURACION);
            propiedades = new Properties();
            propiedades.load(f);
            f.close();
            String Propiedad = propiedades.getProperty(String.valueOf(propiedad));
            if(Propiedad.isEmpty()){
                System.out.println("Archivo SWA ha sido manipulado!");
//                JOptionPane.showMessageDialog(null,"La propiedad \""+propiedad+"\" no contiene argumentos.", "¡ups! Algo inesperado ha pasado", JOptionPane.WARNING_MESSAGE);
            }
            else {
                dato=Decript.Desencriptar(Propiedad);
            }
        }
        catch(Exception e){
            System.out.println("Cod:"+e.getLocalizedMessage());
            String error = e.getLocalizedMessage();
            if(null!=error){
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "¡ups! Algo inesperado ha pasado", JOptionPane.WARNING_MESSAGE);
            } else {
            }
        }
        return dato;
    }
        
}
