/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.mavenproject4;

import Controllers.*;
import Logica.*;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.Connection;
import java.nio.file.Path;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author administrador
 */
public class Mavenproject4 {

    public static void main(String[] args) {
        boolean bSalir = false;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Scanner escaner = new Scanner(System.in);

        System.out.println("Conectando....");

        String urlH2 = "jdbc:h2:" + Path.of("bbdd").toAbsolutePath().toString();
        String urlMySQL = "jdbc:mysql://localhost:3306/pruebas?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "root";
        String pass = "";

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("proj4_persistence");
        EmpleadoJpaController emp = new EmpleadoJpaController(emf);

        //Controladores 
        DepartamentoJpaController depController = new DepartamentoJpaController(emf);
        EmpleadoJpaController emplController = new EmpleadoJpaController(emf);
        ProyectoJpaController proyectController = new ProyectoJpaController(emf);
        SedeJpaController sedeController = new SedeJpaController(emf);

        System.out.println("Empleados : " + emp.getEmpleadoCount());

        // 0. Crear Connexion
        // Connection conn = H2Connector.newInstance(urlH2,user,pass);
        Connection conn = H2Connector.newInstance(urlMySQL, user, pass);

        try {
            // 1. Crear Estado
            Statement estado = conn.createStatement();
            /*
            // 2. Crear TABLA
            String crear = "CREATE TABLE IF NOT EXISTS Personas( id INTEGER PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(50) NOT NULL )";
            estado.execute(crear);

            crear = "create table IF NOT EXISTS sede( id_sede integer auto_increment not null, nom_sede char (20) not null, primary key (id_sede) );";
            estado.execute(crear);

            crear = "create table IF NOT EXISTS departamento ( id_depto integer auto_increment not null, nom_depto char (32) not null, id_sede integer not null, primary key (id_depto), CONSTRAINT fk_depto_sede foreign key  (id_sede) references sede (id_sede) );";
            estado.execute(crear);

            crear = "create table IF NOT EXISTS empleado ( dni char(9) not null,nom_emp char (40) not null,id_depto integer not null, primary key (dni),CONSTRAINT fk_empleado_depto foreign key  (id_depto) references departamento (id_depto) );";
            estado.execute(crear);

            crear = "create table IF NOT EXISTS empleado_datos_prof (dni char(9) not null,categoria char (2) not null,sueldo_bruto_anual decimal(8,2),primary key (dni), CONSTRAINT fk_empleado_datosprof_empl foreign key (dni) references empleado (dni) );";
            estado.execute(crear);

            crear = "create table IF NOT EXISTS proyecto ( id_proy integer auto_increment not null, f_inicio date not null, f_fin date, nom_proy char (20) not null, primary key (id_proy) );";
            estado.execute(crear);

            crear = "create table IF NOT EXISTS proyecto_sede ( id_proy integer not null, id_sede integer not null, f_inicio date not null, f_fin date, CONSTRAINT pk_proyecto primary key (id_proy, id_sede),CONSTRAINT fk_proysede_proy foreign key (id_proy) references proyecto (id_proy),CONSTRAINT fk_proysede_sede foreign key (id_sede) references sede (id_sede) ); ";
            estado.execute(crear);

            System.out.println("Base de datos creada/actualizada con éxito.");

            // 3. INSERTAR DATOS
            String insert = "INSERT INTO Personas( nombre ) VALUES ('Pepe')";
            estado.execute(insert);
            insert = "INSERT INTO Personas( nombre ) VALUES ('Juan')";
            estado.execute(insert);
            insert = "INSERT INTO Personas( nombre ) VALUES ('Manolo')";
            estado.execute(insert);

            System.out.println("Datos insertados con éxito.");
             */
            // 4. MENU
            while (!bSalir) {
                menuTabla();
                int tabla = leerOpcion();
                int opcion;

                switch (tabla) {

                    //DEPARTAMENTO
                    case 1:
                        menuOpcion();
                        opcion = leerOpcion();
                        switch (opcion) {
                            case 1:
                                //Añadir
                                System.out.println("Datos del dapartamento:");
                                System.out.print("Nombre: ");
                                String nombre = escaner.nextLine();
                                System.out.print("Id de la Sede: ");
                                int idSede = escaner.nextInt();
                                escaner.nextLine();

                                Departamento dep = new Departamento(null, nombre);
                                Sede sede = sedeController.findSede(idSede);
                                dep.setIdSede(sede);
                                depController.create(dep);
                                break;
                            case 2:
                                //Modificar
                                System.out.println("Datos del dapartamento:");
                                System.out.print("Id del departamento: ");
                                int idDep = escaner.nextInt();
                                escaner.nextLine();
                                System.out.print("Nuevo nombre: ");
                                nombre = escaner.nextLine();
                                System.out.print("Id de la nueva sede: ");
                                idSede = escaner.nextInt();
                                escaner.nextLine();

                                sede = sedeController.findSede(idSede);
                                dep = depController.findDepartamento(idDep);
                                dep.setNomDepto(nombre);
                                dep.setIdSede(sede);
                                depController.edit(dep);
                                break;
                            case 3:
                                //Borrar
                                System.out.println("Departamento a borrar:");
                                System.out.print("Id: ");
                                int id = escaner.nextInt();
                                escaner.nextLine();

                                depController.destroy(id);
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtelo de nuevo.");
                        }
                        break;

                    //EMPLEADO
                    case 2:
                        menuOpcion();
                        opcion = leerOpcion();
                        switch (opcion) {
                            case 1:
                                //Añadir
                                System.out.println("Datos del empleado:");
                                System.out.print("DNI: ");
                                String dni = escaner.nextLine();
                                System.out.print("Nombre: ");
                                String nombre = escaner.nextLine();

                                Empleado empl = new Empleado(dni, nombre);
                                emplController.create(empl);
                                break;
                            case 2:
                                //Modificar
                                System.out.println("Datos del empleado:");
                                System.out.print("DNI del empleado: ");
                                dni = escaner.nextLine();
                                System.out.print("Nuevo nombre: ");
                                nombre = escaner.nextLine();
                                System.out.print("Id del nuevo departamento: ");
                                int idDep = escaner.nextInt();
                                escaner.nextLine();

                                empl = emplController.findEmpleado(dni);
                                Departamento dep = depController.findDepartamento(idDep);
                                empl.setNomEmp(nombre);
                                empl.setIdDepto(dep);
                                depController.edit(dep);
                                break;
                            case 3:
                                //Borrar
                                System.out.println("Empleado a borrar:");
                                System.out.print("DNI: ");
                                dni = escaner.nextLine();

                                emplController.destroy(dni);
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtelo de nuevo.");
                        }
                        break;

                    //PROYECTO
                    case 3:
                        menuOpcion();
                        opcion = leerOpcion();
                        switch (opcion) {
                            case 1:
                                //Añadir
                                System.out.println("Datos del proyecto:");
                                System.out.print("Nombre: ");
                                String nombre = escaner.nextLine();
                                System.out.print("Fecha inicio: ");
                                String fech = escaner.nextLine();
                                Date fecha = formato.parse(fech);

                                Proyecto proyecto = new Proyecto(null);
                                proyectController.create(proyecto);
                                break;
                            case 2:
                                //Modificar
                                System.out.println("Datos del proyecto:");
                                System.out.print("Id del proyecto: ");
                                int idProyect = escaner.nextInt();
                                escaner.nextLine();
                                System.out.print("Nuevo nombre: ");
                                nombre = escaner.nextLine();
                                System.out.print("Nueva fecha inicio: ");
                                String fechI = escaner.nextLine();
                                Date fInicio = formato.parse(fechI);
                                System.out.print("Nueva fecha fin: ");
                                String fechF = escaner.nextLine();
                                Date fFin = formato.parse(fechF);
                                escaner.nextLine();

                                proyecto = proyectController.findProyecto(idProyect);
                                proyecto.setNomProy(nombre);
                                proyecto.setFInicio(fInicio);
                                proyecto.setFFin(fFin);
                                proyectController.edit(proyecto);
                                break;
                            case 3:
                                //Borrar
                                System.out.println("Proyecto a borrar:");
                                System.out.print("Id: ");
                                int id = escaner.nextInt();
                                escaner.next();

                                proyectController.destroy(id);
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtelo de nuevo.");
                        }
                        break;

                    //SEDE
                    case 4:
                        menuOpcion();
                        opcion = leerOpcion();
                        switch (opcion) {
                            case 1:
                                //Añadir
                                System.out.println("Datos de la sede:");
                                System.out.print("Nombre: ");
                                String nombre = escaner.nextLine();

                                Sede sede = new Sede(null, nombre);
                                sedeController.create(sede);
                                break;
                            case 2:
                                //Modificar
                                System.out.println("Datos de la sede:");
                                System.out.print("Id de la sede: ");
                                int idSede = escaner.nextInt();
                                escaner.nextLine();
                                System.out.print("Nuevo nombre: ");
                                nombre = escaner.nextLine();

                                sede = sedeController.findSede(idSede);
                                sede.setNomSede(nombre);
                                sedeController.edit(sede);
                                break;
                            case 3:
                                //Borrar
                                System.out.println("Sede a borrar:");
                                System.out.print("Id: ");
                                int id = escaner.nextInt();
                                escaner.next();

                                sedeController.destroy(id);
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("Opción no válida. Inténtelo de nuevo.");
                        }
                        break;
                    case 5:
                        bSalir = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            }

            System.out.println("Saliendo del programa...");
            // ... El resto de tu código ...

            // 3. Cerrar Connexión
            estado.close();

            conn.close();
        } catch (Exception ex) {
            System.out.println("Error creando la conexión" + ex.getMessage());
            return;
        }

        System.out.println(
                "Conexion cerrada con éxito");
    }

    private static void menuTabla() {
        System.out.println("\nSelecciona la tabla a modificar");
        System.out.println("1. Departamento");
        System.out.println("2. Empleado");
        System.out.println("3. Proyecto");
        System.out.println("4. Sede");
        System.out.println("5. Salir");
    }

    private static void menuOpcion() {
        System.out.println("\nSelecciona una opción");
        System.out.println("1. Añadir");
        System.out.println("2. Modificar");
        System.out.println("3. Borrar");
        System.out.println("4. Salir");
    }

    private static int leerOpcion() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione una opción: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, introduzca un número válido.");
            scanner.next(); // Consumir la entrada no válida
        }
        return scanner.nextInt();
    }
}
