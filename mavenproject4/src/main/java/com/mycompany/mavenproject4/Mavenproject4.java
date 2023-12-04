package com.mycompany.mavenproject4;

// GitHUB

import com.mycompany.mavenproject4.exceptions.NonexistentEntityException;
import java.sql.SQLException;
import java.sql.Connection;
import java.nio.file.Path;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Mavenproject4 {

    public static void main(String[] args) 
    {
        System.out.println("Conectando....");
        
        String urlH2    = "jdbc:h2:" + Path.of("bbdd").toAbsolutePath().toString();
        String urlMySQL = "jdbc:mysql://localhost:3306/prueba?zeroDateTimeBehavior=CONVERT_TO_NULL";
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
                
        try
        {
            
            // 1. Crear Estado
            Statement estado = conn.createStatement();
        
            // 1. Departamento
            DepartamentoJpaController contDep = new DepartamentoJpaController( emf );
            
            Departamento dpt = new Departamento(444,"Dpto44");
            Departamento dpt2 = new Departamento(4555,"Dpto22");
                       
            System.out.println("DEP ID antes de crear :: " + dpt.getIdDepto() );
            
            contDep.create(dpt);
            
            System.out.println("DEP ID despues de crear " + dpt.getIdDepto() );
            contDep.create(dpt2);
            
            dpt2.setIdDepto(1);            
            dpt2.setNomDepto("AAAJAAAAK");
            contDep.edit(dpt2);
            
            System.out.println("Recupero lista de Departamentos"); 
            List<Departamento> list = contDep.findDepartamentoEntities();
            
            for( Departamento a :list )
            {
                System.out.println("Find == 1 " + a.getIdDepto() + "Dep: " + a.getNomDepto() );
            }
                        
            // 2. Empleados
            EmpleadoJpaController cont = new EmpleadoJpaController( emf );
            Empleado emp1 = new Empleado("123456789H","Alpha Apellido1 Apellido2");
            Empleado emp2 = new Empleado("987654321H","Beta Apellido1 Apellido2");
            Empleado emp3 = new Empleado("333333333H","Gamma Apellido1 Apellido2");
            cont.create(emp1);
            cont.create(emp2);
            cont.create(emp3);

            emp1.setIdDepto(dpt);
            emp2.setIdDepto(dpt);
            cont.edit(emp1);
            cont.edit(emp2);
            
            System.out.println("Datos de empleados"); 
            
            EmpleadoDatosProfJpaController contDatos = new EmpleadoDatosProfJpaController( emf );
            EmpleadoDatosProf datos = new EmpleadoDatosProf();
            
            System.out.println("UPDATE DNI");
            emp3.setDni("555555H");
            System.out.println("EDIT ");
            cont.edit(emp3);
            System.out.println("END");
                       
            
            /*
            Personas per = new Personas();
            per.setNombre("Maria");
            per.setId(1000);
            cont.edit(per);
            */
            
            /*
            // 2. Crear TABLA
            String crear = "CREATE TABLE IF NOT EXISTS Personas( id INTEGER PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(50) NOT NULL )";
            estado.execute( crear );            
            
            crear = "create table IF NOT EXISTS sede( id_sede integer auto_increment not null, nom_sede char (20) not null, primary key (id_sede) );";
            estado.execute( crear );            
            
            crear = "create table IF NOT EXISTS departamento ( id_depto integer auto_increment not null, nom_depto char (32) not null, id_sede integer not null, primary key (id_depto), CONSTRAINT fk_depto_sede foreign key  (id_sede) references sede (id_sede) );";
            estado.execute( crear );            
            
            crear = "create table IF NOT EXISTS empleado ( dni char(9) not null,nom_emp char (40) not null,id_depto integer not null, primary key (dni),CONSTRAINT fk_empleado_depto foreign key  (id_depto) references departamento (id_depto) );";
            estado.execute( crear );            
            
            crear = "create table IF NOT EXISTS empleado_datos_prof (dni char(9) not null,categoria char (2) not null,sueldo_bruto_anual decimal(8,2),primary key (dni), CONSTRAINT fk_empleado_datosprof_empl foreign key (dni) references empleado (dni) );";
            estado.execute( crear );            
            
            crear = "create table IF NOT EXISTS proyecto ( id_proy integer auto_increment not null, f_inicio date not null, f_fin date, nom_proy char (20) not null, primary key (id_proy) );";
            estado.execute( crear );            
            
            crear = "create table IF NOT EXISTS proyecto_sede ( id_proy integer not null, id_sede integer not null, f_inicio date not null, f_fin date, CONSTRAINT pk_proyecto primary key (id_proy, id_sede),CONSTRAINT fk_proysede_proy foreign key (id_proy) references proyecto (id_proy),CONSTRAINT fk_proysede_sede foreign key (id_sede) references sede (id_sede) ); ";
            estado.execute( crear );            
            
            System.out.println("Base de datos creada/actualizada con éxito.");        
            
            // 3. INSERTAR DATOS
            String insert = "INSERT INTO Personas( nombre ) VALUES ('Pepe')";
            estado.execute( insert );
            insert = "INSERT INTO Personas( nombre ) VALUES ('Juan')";
            estado.execute( insert );
            insert = "INSERT INTO Personas( nombre ) VALUES ('Manolo')";
            estado.execute( insert );
            
            System.out.println("Datos insertados con éxito.");        
            
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
            */            
        }        
        catch( SQLException ex )
        {
            System.out.println("Error creando la conexión" + ex.getMessage() );
            return;
        }
        catch( Exception ex )
        {
            System.out.println("Error creando la conexión" + ex.getMessage() );
            return;
        }
        
        System.out.println("Conexion cerrada con éxito");
        
        
    }
}
