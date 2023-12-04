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
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.jpa.jpql.Assert;

/**
 *
 * @author administrador
 */
public class Mavenproject4 {

    public static void main(String[] args) throws Exception {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Conectando....");

        String urlH2 = "jdbc:h2:" + Path.of("bbdd").toAbsolutePath().toString();
        String urlMySQL = "jdbc:mysql://localhost:3306/prueba?zeroDateTimeBehavior=CONVERT_TO_NULL";
        String user = "user";
        String pass = "root";

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidadJPA");

        // Crear Connexion
        // Connection conn = H2Connector.newInstance(urlH2,user,pass);
        Connection conn = H2Connector.newInstance(urlMySQL, "root", "");
        Statement estado = conn.createStatement();

        //controladores
        DepartamentoJpaController dpaCont = new DepartamentoJpaController(emf);
        EmpleadoJpaController emp = new EmpleadoJpaController(emf);
        PersonasJpaController cont = new PersonasJpaController(emf);

        boolean control = true;
        while (control) {

            //menu
            System.out.println("***Menu***");
            System.out.println("¿sobre que tabla vamos a actuar?");
            System.out.println("1: Empleado");
            System.out.println("2: Continuará");
            int op;
            op = entrada.nextInt();
            switch (op) {
                case 1:
                    //menu de empleados
                    entrada.nextLine();
                    System.out.println("Opciones en empleados: ");
                    System.out.println("1: Mostrar empleados");
                    System.out.println("2: Crear un empleado");
                    System.out.println("3: Editar un empleado");
                    System.out.println("4: Mostrar un empleado");
                    System.out.println("5: Eliminar un empleado");
                    System.out.println("6: Contar empleados");
                    System.out.println("7: salir");
                    op = entrada.nextInt();
                    switch (op) {
                        case 1:
                            //Mostrar empleados
                            List<Empleado> listaEmpleados = emp.findEmpleadoEntities();
                            System.out.println("Empleados:");
                            for (Empleado empleado : listaEmpleados) {
                                System.out.println(empleado);
                            }
                            break;
                        case 2:
                            //Crear empleados
                            entrada.nextLine();
                            Empleado empleado = new Empleado();
                            System.out.println("Introduzca el DNI");
                            String dni = entrada.nextLine();
                            empleado.setDni(dni);
                            entrada.nextLine();
                            System.out.println("Introduzca el nombre y los apellidos");
                            String nombreApellidos = entrada.nextLine();
                            empleado.setNomEmp(nombreApellidos);
                            System.out.println("Introduzca el departamento al que pertenece ");
                            int idDepartamento = entrada.nextInt();
                            Departamento depto = dpaCont.findDepartamento(idDepartamento);
                            empleado.setIdDepto(depto);
                            try {
                                emp.create(empleado);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            System.out.println("Hecho");
                            break;
                        case 3:
                            //Editar empleados
                            Empleado ampleadoAactualizar = new Empleado();
                            entrada.nextLine();
                            System.out.println("Dime el DNI del empleado a actualizar.");
                            dni = entrada.nextLine();
                            Empleado empleadoactualizado = emp.findEmpleado(dni);
                            System.out.println("Introduce el nombre actualizado.");
                            nombreApellidos = entrada.nextLine();
                            empleadoactualizado.setNomEmp(nombreApellidos);
                            try {
                                emp.edit(empleadoactualizado);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            System.out.println("Hecho");

                            break;
                        case 4:
                            //Mostrar un empleado
                            entrada.nextLine();
                            System.out.println("introduzca el DNI del empleadpo");
                            dni = entrada.nextLine();
                            entrada.nextLine();
                            Empleado encontrarEmp = emp.findEmpleado(dni);
                            System.out.println(encontrarEmp.toString());
                            break;
                        case 5:
                            //Eliminar un empleado
                            entrada.nextLine();
                            System.out.println("introduzca el DNI del empleadpo");
                            dni = entrada.nextLine();
                            try {
                                emp.destroy(dni);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            System.out.println("hecho");
                            break;
                        case 6:
                            //Contar empleados 
                            int totalEmpl = emp.getEmpleadoCount();
                            System.out.println("En total hay " + totalEmpl + " empleados.");
                            break;
                        case 7:
                            //salir de este menu
                            control = false;
                            break;
                    }

                case 2:
                    control = false;
                    break;

                default:
                    throw new AssertionError();
            }
        }

        try {
            estado.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error creando la conexión" + ex.getMessage());
            return;
        }

        System.out.println("Conexion cerrada con éxito");

    }
}
