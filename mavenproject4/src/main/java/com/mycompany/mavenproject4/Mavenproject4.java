/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.mavenproject4;

import java.sql.SQLException;
import java.sql.Connection;
import java.nio.file.Path;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author administrador
 */
public class Mavenproject4 {

    public static void main(String[] args) 
    {
        System.out.println("Conectando....");
        
        String url = "jdbc:h2:" + Path.of("bbdd").toAbsolutePath().toString();
        String user = "user";
        String pass = "root";
        
        // 0. Crear Connexion
        Connection conn = H2Connector.newInstance(url,user,pass);
        
        try
        {
            // 1. Crear Estado
            Statement estado = conn.createStatement();
            
            // 2. Crear TABLA
            String crear = "CREATE TABLE Personas( id INTEGER PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(50) NOT NULL )";
            estado.execute( crear );            
            
            crear = "create table sede( id_sede integer auto_increment not null, nom_sede char (20) not null, primary key (id_sede) );";
            estado.execute( crear );            
            
            crear = "create table departamento ( id_depto integer auto_increment not null, nom_depto char (32) not null, id_sede integer not null, primary key (id_depto),foreign key fk_depto_sede (id_sede) references sede (id_sede) );";
            estado.execute( crear );            
            
            crear = "create table empleado ( dni char(9) not null,nom_emp char (40) not null,id_depto integer not null, primary key (dni),foreign key fk_empleado_depto (id_depto) references departamento (id_depto) );";
            estado.execute( crear );            
            
            crear = "create table empleado_datos_prof (dni char(9) not null,categoria char (2) not null,sueldo_bruto_anual decimal(8,2),primary key (dni),foreign key fk_empleado_datosprof_empl (dni) references empleado (dni) );";
            estado.execute( crear );            
            
            crear = "create table proyecto (id_proy integer auto_increment not null,f_inicio date not null,f_fin date,nom_proy char (20) not null,primary key (id_proy)";
            estado.execute( crear );            
            
            crear = "create table proyecto_sede ( id_proy integer not null, id_sede integer not null, f_inicio date not null,f_fin date,primary key (id_proy, id_sede),foreign key fk_proysede_proy (id_proy) references proyecto (id_proy), foreign key fk_proysede_sede (id_sede) references sede (id_sede) ); ";
            estado.execute( crear );            
            
            // 3. INSERTAR DATOS
            String insert = "INSERT INTO Personas( nombre ) VALUES ('Pepe')";
            estado.execute( insert );
            insert = "INSERT INTO Personas( nombre ) VALUES ('Juan')";
            estado.execute( insert );
            insert = "INSERT INTO Personas( nombre ) VALUES ('Manolo')";
            estado.execute( insert );
            
            // 4. INSERTAR DATOS 
            String buscar = "SELECT id, nombre from Personas";
            ResultSet set = estado.executeQuery( buscar );
            
            while( !set.isLast() )
            {
                set.next();                
                System.out.println(" NOMBRE : " + set.getString(2) );            
            }
            
            // 5. Cerrar Connexión
            estado.close();
            conn.close();
        }
        catch(SQLException ex)
        {
            System.out.println("Error creando la conexión" + ex.getMessage() );
            return;
        }
        
        System.out.println("Conexion cerrada con éxito");
        
        
    }
}
