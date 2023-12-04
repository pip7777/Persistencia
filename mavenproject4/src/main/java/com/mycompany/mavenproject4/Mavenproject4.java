/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.mavenproject4;

import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import java.sql.SQLException;
import java.sql.Connection;
import java.nio.file.Path;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrador
 */
public class Mavenproject4 {

    public static void main(String[] args) {
        System.out.println("Conectando....");

        String urlH2 = "jdbc:h2:" + Path.of("bbdd").toAbsolutePath().toString();
        String urlMySQL = "jdbc:mysql://localhost:3306/prueba?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "user";
        String pass = "root";

        //   EntityManagerFactory emf = Persistence.createEntityManagerFactory("proj4_persistence"); 
//        EmpleadoJpaController emp = new EmpleadoJpaController(emf);
        // System.out.println( "Empleados : " + emp.getEmpleadoCount() );
        // 0. Crear Connexion
        // Connection conn = H2Connector.newInstance(urlH2,user,pass);
        Connection conn = H2Connector.newInstance(urlMySQL, "root", "");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("proj4_persistence");
//        try
//        {

//            // 1. Crear Estado
//            Statement estado = conn.createStatement();
//            
//            PersonasJpaController cont =new PersonasJpaController(emf);
//            
//            Personas per= new Personas (23,"pepita");
//            cont.create(per);
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
//        catch(SQLException ex)
//        {
//            System.out.println("Error creando la conexión" + ex.getMessage() );
//            return;
//        }
//        
//        System.out.println("Conexion cerrada con éxito");
        DepartamentoJpaController departamentoController = new DepartamentoJpaController(emf);
        Scanner entrada = new Scanner(System.in);
        boolean salir = false;
        int opcion;

        while (!salir) {
            System.out.println("1. Gestionar Empleados");
            System.out.println("2. Salir");

            System.out.println("Elija una de las opciones");
            opcion = entrada.nextInt();

            switch (opcion) {
                case 1:
                    boolean volver = false;
                    while (!volver) {
                        try {
                            System.out.println("Que deseas hacer? pulsa una de las opciones disponibles");
                            System.out.println("1. Crear empleados");
                            System.out.println("2. Eliminar empleados");
                            System.out.println("3. Editar empleados");
                            System.out.println("4. Contar empleados");
                            System.out.println("5. Volver al menú principal");

                            int opcionEmpleados = entrada.nextInt();
                            switch (opcionEmpleados) {
                                case 1:
                                    //crear nuevos empleados
                                    entrada.nextLine();
                                    Empleado empleado = new Empleado();
                                    System.out.println("Introduzca el DNI");
                                    String dni = entrada.nextLine();
                                    empleado.setDni(dni);
                                    entrada.nextLine();
                                    System.out.println("Introduce el nombre y apellidos del nuevo empleado");
                                    String nombreEmpleados = entrada.nextLine();
                                    empleado.setNomEmp(nombreEmpleados);
                                    System.out.println("Introduzca el departamento al que pertenece ");
                                    int idDepartamento = entrada.nextInt();
                                    Departamento depto = departamentoController.findDepartamento(idDepartamento);
                                    empleado.setIdDepto(depto);

                                    try {
                                        EmpleadoJpaController cntrlEmpleado = new EmpleadoJpaController(emf);
                                        cntrlEmpleado.create(empleado);
                                        System.out.println("Empleado creado con éxito.");
                                    } catch (Exception e) {
                                        System.out.println("Error al crear la empleado: " + e.getMessage());
                                    }
                                    break;
                                case 2:
                                    // eliminar empleado
                                    System.out.println("Introduzca el DNI del empleado a eliminar");
                                    String dniEliminar = entrada.next();
                                    EmpleadoJpaController cntrlEmpleadoEliminar = new EmpleadoJpaController(emf);

                                    try {
                                        cntrlEmpleadoEliminar.destroy(dniEliminar);
                                        System.out.println("Empleado eliminado con éxito.");
                                    } catch (Exception e) {
                                        System.out.println("Error: al eliminar el empleado");

                                    }
                                    break;
                                case 3:
                                    //editar empleados
                                    EmpleadoJpaController cntrlEmpleado = new EmpleadoJpaController(emf);
                                    Empleado empleadoUpdate = new Empleado();
                                    entrada.nextLine();
                                    System.out.println("Introduce el DNI del empleado para actualizar.");
                                    dni = entrada.nextLine();
                                    Empleado empleadoActualizar = cntrlEmpleado.findEmpleado(dni);
                                    System.out.println("Introduce el nombre actualizado.");
                                    nombreEmpleados = entrada.nextLine();
                                    empleadoActualizar.setNomEmp(nombreEmpleados);

                                    try {
                                        cntrlEmpleado.edit(empleadoActualizar);
                                        System.out.println("Empleado actualizado con éxito.");
                                    } catch (Exception e) {
                                        System.out.println("Error: al actualizar el empleado");

                                    }

                                    break;
                                case 4:
                                    // contar empleados
                                    EmpleadoJpaController cntrlEmpleadoContar = new EmpleadoJpaController(emf);
                                    try {
                                        int totalEmpleados = cntrlEmpleadoContar.getEmpleadoCount();
                                        System.out.println("Total de empleados: " + totalEmpleados);
                                    } catch (Exception e) {
                                        System.out.println("Error al contar los empleados: " + e.getMessage());
                                    }
                                    break;
                                case 5:
                                    volver = true;
                                    break;
                                default:
                                    System.out.println("Solo números entre 1 y 5");
                            }

                        } catch (Exception ex) {
                            Logger.getLogger(Mavenproject4.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                case 2:
                    salir = true;
                    break;

            }
        }

    }
}
