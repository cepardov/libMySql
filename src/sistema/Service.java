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
package sistema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 * @author cepardov <cepardov@gmail.com>
 */

/**
 * Permite ejecutar solo una instancia en una estacion de trabajo
 */
public class Service {
    // Declaracion de variable archivo service.tmp
    private File fichero = new File("Service.tmp");    
    //Variable tiempo en que se actualiza el fichero TMP
    private int segundos = 20;

    /** Constructor de clase */
    public Service(){};

    /**
 * Comprueba que archivo TMP exista, sino lo crea e inicia valores
 */
    public boolean comprobar()
    {           
        if ( fichero.exists() )
        {           
            long tiempo = leer();//
            long res = restarTiempo( tiempo );           
            if( res < segundos )
            {              
                JOptionPane.showMessageDialog(null,"Estimado Usuario ya hay una instancia en ejecución.");
                return false;
            }
            else
            {        
                programar_tarea();
                return true;
            }
        }
        else// no existe fichero
        {
            crearTMP();   
            programar_tarea();
            return true;
        }            
    }

    /**
 * Lee el archivo TMP y retorna su valor 
 * @return LONG cantidad de milisegundos 
 */
    public long leer()
    {
        String linea = "0";        
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader( new FileReader( fichero ) );            
            while(bufferedReader.ready()){
                linea = bufferedReader.readLine();            
        }
        }catch (IOException e) {
            System.err.println( e.getMessage() );
        }
        return Long.valueOf(linea).longValue();
    }

    /**
 * Programa un proceso que se repite cada cierto tiempo
 */
    public void programar_tarea()
    {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate( 
            new Runnable() 
            {
                @Override
                public void run() {                   
                    crearTMP(); 
                }
              }, 1000, segundos * 1000 , TimeUnit.MILLISECONDS ); //comienza dentro de 1 segundo y luego se repite cada N segundos

    }

    /**
 * Crea un archivo TMP con un unico valor, el tiempo en milisegundos
 */
    public void crearTMP()
    {
        Date fecha=new Date();        
        try {            
            BufferedWriter writer = new BufferedWriter(new FileWriter( fichero ));                        
            writer.write(  String.valueOf( fecha.getTime() ) );                        
            writer.close();            
        } catch (IOException e) {
            System.err.println( e.getMessage() );
        }        
    }

    /**
 * Resta el tiempo expresado en milisegundos
 * @param tiempoActual el tiempo actual del sistema expresado en milisegundos
 * @return tiempo el resultado expresado en segundos
 */
    public long restarTiempo( long tiempoActual )
    {
        Date date =new Date();        
        long tiempoTMP = date.getTime();        
        long tiempo = tiempoTMP - tiempoActual;        
        tiempo = tiempo /1000;        
        return tiempo;
    }

    /**
 * Elimina el fichero TMP si es que existe
 */
    public void cerrarApp()
    {   
        if ( fichero.exists() ) { fichero.delete(); }
        System.exit(0);
    }

}//--> fin clase
