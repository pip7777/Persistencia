package com.mycompany.mavenproject4;

import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import java.sql.SQLException;
import java.sql.Connection;
import java.nio.file.Path;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Mavenproject4 {

    public static void main(String[] args) 
    {
        System.out.println("Conectando....");
        
        String urlH2    = "jdbc:h2:" + Path.of("bbdd").toAbsolutePath().toString();
        String urlMySQL = "jdbc:mysql://localhost:3310/prueba?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "user";
        String pass = "root";
        
        // EntityManagerFactory emf = Persistence.createEntityManagerFactory("proj4_persistence"); 
        // EmpleadoJpaController emp = new EmpleadoJpaController(emf);
        // System.out.println( "Empleados : " + emp.getEmpleadoCount() );
        
        // 0. Crear Connexion
        // Connection conn = H2Connector.newInstance(urlH2,user,pass);
        Connection conn = H2Connector.newInstance(urlMySQL,"root","");
        
        // 0. Crear EntityManagerFactory 
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadJPA");
                
//        try
//        {
//            
//            // 1. Crear Estado
//            Statement estado = conn.createStatement();
//        
//            // 1. Creo el Controller
//            PersonasJpaController cont = new PersonasJpaController( emf );
//            
//            Personas per = new Personas();
//            per.setNombre("Maria");
//            per.setId(1000);
//            cont.edit(per);
//            
//            // 2. Crear TABLA
//            String crear = "CREATE TABLE IF NOT EXISTS Personas( id INTEGER PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(50) NOT NULL )";
//            estado.execute( crear );            
//            
//            crear = "create table IF NOT EXISTS sede( id_sede integer auto_increment not null, nom_sede char (20) not null, primary key (id_sede) );";
//            estado.execute( crear );            
//            
//            crear = "create table IF NOT EXISTS departamento ( id_depto integer auto_increment not null, nom_depto char (32) not null, id_sede integer not null, primary key (id_depto), CONSTRAINT fk_depto_sede foreign key  (id_sede) references sede (id_sede) );";
//            estado.execute( crear );            
//            
//            crear = "create table IF NOT EXISTS empleado ( dni char(9) not null,nom_emp char (40) not null,id_depto integer not null, primary key (dni),CONSTRAINT fk_empleado_depto foreign key  (id_depto) references departamento (id_depto) );";
//            estado.execute( crear );            
//            
//            crear = "create table IF NOT EXISTS empleado_datos_prof (dni char(9) not null,categoria char (2) not null,sueldo_bruto_anual decimal(8,2),primary key (dni), CONSTRAINT fk_empleado_datosprof_empl foreign key (dni) references empleado (dni) );";
//            estado.execute( crear );            
//            
//            crear = "create table IF NOT EXISTS proyecto ( id_proy integer auto_increment not null, f_inicio date not null, f_fin date, nom_proy char (20) not null, primary key (id_proy) );";
//            estado.execute( crear );            
//            
//            crear = "create table IF NOT EXISTS proyecto_sede ( id_proy integer not null, id_sede integer not null, f_inicio date not null, f_fin date, CONSTRAINT pk_proyecto primary key (id_proy, id_sede),CONSTRAINT fk_proysede_proy foreign key (id_proy) references proyecto (id_proy),CONSTRAINT fk_proysede_sede foreign key (id_sede) references sede (id_sede) ); ";
//            estado.execute( crear );            
//            
//            System.out.println("Base de datos creada/actualizada con éxito.");        
//            
//            // 3. INSERTAR DATOS
//            String insert = "INSERT INTO Personas( nombre ) VALUES ('Pepe')";
//            estado.execute( insert );
//            insert = "INSERT INTO Personas( nombre ) VALUES ('Juan')";
//            estado.execute( insert );
//            insert = "INSERT INTO Personas( nombre ) VALUES ('Manolo')";
//            estado.execute( insert );
//            
//            System.out.println("Datos insertados con éxito.");        
//            
//            // 4. INSERTAR DATOS 
//            String buscar = "SELECT id, nombre from Personas";
//            ResultSet set = estado.executeQuery( buscar );
//            
//            while( !set.isLast() )
//            {
//                set.next();                
//                System.out.println(" NOMBRE : " + set.getString(2) );            
//            }
//            
//            // 5. Cerrar Connexión
//            estado.close();
//            conn.close();
//        }        
//        catch( SQLException ex )
//        {
//            System.out.println("Error creando la conexión" + ex.getMessage() );
//            return;
//        }
//        catch( Exception ex )
//        {
//            System.out.println("Error creando la conexión" + ex.getMessage() );
//            return;
//        }
//        
//        System.out.println("Conexion cerrada con éxito");
//        
//        
        // Menú
        
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        int opcion; 

        while (!salir) {
            
            System.out.println("1. Empleados");
            System.out.println("5. Salir");

            System.out.println("Elija una de las opciones");
            opcion = scanner.nextInt();
            
            if(opcion == 5){
            
                break;
                
            }
                    
                    EmpleadoJpaController controllerE = new EmpleadoJpaController(emf);
                    
                    System.out.println("¿Que quieres hacer con los empleados?");
                    System.out.println("1. Crear");
                    System.out.println("2. Actualizar");
                    System.out.println("3. Contar");
                    System.out.println("4. Eliminar");
                    
                    int opcion1; 
                    opcion1 = scanner.nextInt();
                    
                    scanner.nextLine();
                    
                    switch (opcion1) {
                        case 1:
                            
                            String dni,nombre; 
                            
                            System.out.println("¿Que DNI tiene el trabajador?");
                            dni = scanner.nextLine();
                            System.out.println("¿Que nombre tiene el trabajador?");
                            nombre = scanner.nextLine();
                            Empleado empleado = new Empleado(dni,nombre);
                           
                            try {
                                controllerE.create(empleado);
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                           
                            break;

                        case 2:
                            
                            String nombreActualizado,dniempleado; 
                            
                            System.out.println("Dame el dni del empleado que quieres editar");
                            dniempleado = scanner.nextLine();
                            System.out.println("¿Que nombre quieres ponerle?");
                            nombreActualizado = scanner.nextLine();          
                            
                            Empleado empleadoActualizado = controllerE.findEmpleado(dniempleado);
                            
                            empleadoActualizado.setNomEmp(nombreActualizado);
                
                            try {
                                controllerE.edit(empleadoActualizado);
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                           
                        break;

                        case 3:
                            
                            System.out.println("Existen: " + controllerE.getEmpleadoCount() + " Empleados");
                           
                        break;
                        case 4:
                             
                            String eleccion,dniEmp;
                            
                            System.out.println("¿Que empleado quieres eliminar? Dime el DNI");
                            dniEmp = scanner.nextLine();
                            System.out.println("¿Quieres eliminar a...?: " + controllerE.findEmpleado(dniEmp).getNomEmp() + " (y/n)");
                            eleccion = scanner.nextLine();
                            eleccion=eleccion.toLowerCase();
                            if(eleccion.equals("y")){
                                
                                try{
                                    
                                    controllerE.destroy(dniEmp);
                            
                                }catch(Exception e){
                                    
                                    System.out.println("ERROR: " + e.getMessage());

                                }
                            
                            }
                        default:
                            System.out.println("Solo números entre 1 y 5");
                    }
        }
  
    }
}
