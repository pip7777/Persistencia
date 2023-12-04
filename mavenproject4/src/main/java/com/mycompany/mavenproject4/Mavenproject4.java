package com.mycompany.mavenproject4;

import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Sarah Delgado Martin
 */
public class Mavenproject4 {

    // Menú
    public static void main(String[] args) {
        /*
        Connection conn = H2Connector.newInstance("jdbc:mysql://localhost:3306/prueba?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "");

        // 0. Crear EntityManagerFactory 
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadJPA");

        try {
            // 1. Crear Estado
            Statement estado = conn.createStatement();
        

            // 1. Departamento
            DepartamentoJpaController contD = new DepartamentoJpaController(emf);
            
            Departamento departamento = new Departamento(1, "Departamento1 ");
            contD.create(departamento);
            
            // 2. Empleado
            EmpleadoJpaController contE = new EmpleadoJpaController(emf);
             
            Empleado empleado = new Empleado("123456789Z", "Antonio");
            contE.create(empleado);
            
            empleado.setIdDepto(departamento);
            contE.edit(empleado);
            
            // 3. Datos empleado
            EmpleadoDatosProfJpaController contDatos = new EmpleadoDatosProfJpaController(emf);
            
            EmpleadoDatosProf datos = new EmpleadoDatosProf();
        

        } catch (SQLException ex) {
            Logger.getLogger(Mavenproject4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Mavenproject4.class.getName()).log(Level.SEVERE, null, ex);
        }
         */

        // Variables
        int eleccion = -1; // Guarda la elección del usuario
        Scanner teclado = new Scanner(System.in); // Para leer por teclado

        do {
            System.out.println("Escriba una opción del menú, 0 para salir:\n"
                    + "1. Empleado.\n"
                    + "2. Departamento.\n"
                    + "3. Proyecto.\n"
                    + "4. Sede.\n");
            eleccion = teclado.nextInt();
            teclado.nextLine();
            
            switch (eleccion) {
                case 1:
                    menuEmpleado();
                    break;
                case 2:
                    menuDepartamento();
                    break;
                case 3:
                    menuProyecto();
                    break;
                case 4:
                    menuSede();
                    break;

            }

        } while (eleccion != 0);

    }

    public static void menuEmpleado() {
        // Variables
        int eleccion = -1; // Guarda la elección del usuario
        Scanner teclado = new Scanner(System.in); // Para leer por teclado
        String dni;
        String nombre;

        // Crea EntityManagerFactory 
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadJPA");

        // Crea el controlador de empleado
        EmpleadoJpaController ejc = new EmpleadoJpaController(emf);

        // Crea un empleado
        Empleado empleado = new Empleado();

        do {
            System.out.println("Escriba una opción del menú, 0 para salir:\n"
                    + "1. Añadir empleado.\n"
                    + "2. Modificar empleado.\n"
                    + "3. Borrar empleado.\n"
                    + "4. Buscar empleado.\n"
                    + "5. Mostrar número de empleados totales.\n");
            eleccion = teclado.nextInt();
            teclado.nextLine();

            switch (eleccion) {
                case 1:
                    System.out.println("Introduzca el dni del empleado: ");
                    dni = teclado.nextLine();
                    System.out.println("Introduzca el nombre del empleado: ");
                    nombre = teclado.nextLine();
                    System.out.println("Introduzca el código del departamento: ");
                    Integer iddpto = teclado.nextInt();
                    teclado.nextLine();
                    DepartamentoJpaController djc = new DepartamentoJpaController(emf);
                    Departamento departamento = djc.findDepartamento(iddpto);
                    empleado.setDni(dni);
                    empleado.setNomEmp(nombre);
                    empleado.setIdDepto(departamento);
                    try {
                        ejc.create(empleado);
                        System.out.println("Empleado añadido con éxito.");
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible añadir al empleado.");
                    }
                    break;
                case 2:
                    System.out.println("Introduzca el DNI del empleado a modificar: ");
                    dni = teclado.nextLine();
                    System.out.println("Introduzca el nuevo nombre del empleado: ");
                    nombre = teclado.nextLine();
                    empleado.setDni(dni);
                    empleado.setNomEmp(nombre);
                    try {
                        ejc.edit(empleado);
                        System.out.println("Empleado modificado con éxito.");
                    } catch (NonexistentEntityException ex) {
                        System.out.println("No existe el empleado.");
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible modificar el empleado.");
                    }
                    break;
                case 3:
                    System.out.println("Introduzca el DNI del empleado a borrar: ");
                    dni = teclado.nextLine();
                    try {
                        ejc.destroy(dni);
                        System.out.println("Empleado borrado con éxito.");
                    } catch (NonexistentEntityException ex) {
                        System.out.println("No existe el empleado.");
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible borrar el empleado.");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el DNI del empleado a buscar: ");
                    dni = teclado.nextLine();
                    try {
                        empleado = ejc.findEmpleado(dni);
                        System.out.println("DNI: " + empleado.getDni() + "\n" + "Nombre: " + empleado.getNomEmp());
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible encontrar el empleado.");
                    }
                    break;
                case 5:
                    System.out.println("El número total de empleados es " + ejc.getEmpleadoCount() + ".");
                    break;

            }

        } while (eleccion != 0);
    }

    public static void menuDepartamento() {
        // Variables
        int eleccion = -1; // Guarda la elección del usuario
        Scanner teclado = new Scanner(System.in); // Para leer por teclado
        Integer idDpto;
        String nomDepto;
        Sede sede;

        // Crea EntityManagerFactory 
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadJPA");

        // Crea el controlador de departamento
        DepartamentoJpaController djc = new DepartamentoJpaController(emf);

        // Crea un empleado
        Departamento departamento = new Departamento();

        do {
            System.out.println("Escriba una opción del menú, 0 para salir:\n"
                    + "1. Añadir departamento.\n"
                    + "2. Modificar departamento.\n"
                    + "3. Borrar departamento.\n"
                    + "4. Buscar departamento.\n"
                    + "5. Mostrar número de departamentos totales.\n");
            eleccion = teclado.nextInt();
            teclado.nextLine();

            switch (eleccion) {
                case 1:
                    System.out.println("Introduzca la id del departamento: ");
                    idDpto = teclado.nextInt();
                    teclado.nextInt();
                    System.out.println("Introduzca el nombre del departamento: ");
                    nomDepto = teclado.nextLine();
                    System.out.println("Introduzca el código de la sede: ");
                    Integer iddpto = teclado.nextInt();
                    teclado.nextLine();
                    SedeJpaController sjc = new SedeJpaController(emf);
                    sede = sjc.findSede(iddpto);
                    departamento.setIdDepto(iddpto);
                    departamento.setNomDepto(nomDepto);
                    departamento.setIdSede(sede);
                    try {
                        djc.create(departamento);
                        System.out.println("Departamento añadido con éxito.");
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible añadir el departamento.");
                    }
                    break;
                case 2:
                    System.out.println("Introduzca la id del departamento: ");
                    idDpto = teclado.nextInt();
                    teclado.nextInt();
                    System.out.println("Introduzca el nuevo nombre del departamento: ");
                    nomDepto = teclado.nextLine();
                    
                    try {
                        ejc.edit(empleado);
                        System.out.println("Empleado modificado con éxito.");
                    } catch (NonexistentEntityException ex) {
                        System.out.println("No existe el empleado.");
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible modificar el empleado.");
                    }
                    break;
                case 3:
                    System.out.println("Introduzca el DNI del empleado a borrar: ");
                    dni = teclado.nextLine();
                    try {
                        ejc.destroy(dni);
                        System.out.println("Empleado borrado con éxito.");
                    } catch (NonexistentEntityException ex) {
                        System.out.println("No existe el empleado.");
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible borrar el empleado.");
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el DNI del empleado a buscar: ");
                    dni = teclado.nextLine();
                    try {
                        empleado = ejc.findEmpleado(dni);
                        System.out.println("DNI: " + empleado.getDni() + "\n" + "Nombre: " + empleado.getNomEmp());
                    } catch (Exception ex) {
                        System.out.println("No ha sido posible encontrar el empleado.");
                    }
                    break;
                case 5:
                    System.out.println("El número total de empleados es " + ejc.getEmpleadoCount() + ".");
                    break;

            }

        } while (eleccion != 0);
    }
    

    public static void menuProyecto() {

    }

    public static void menuSede() {

    }

} // Ebd class
