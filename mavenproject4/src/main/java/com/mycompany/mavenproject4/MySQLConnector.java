/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author administrador
 */
public class MySQLConnector {
    
    // TRUCO >> CONSTRUCTOR ES PRIVADO >> EVITA CREAR OBJETOS DE ESTA CLASE
    private MySQLConnector(){}
    
    
    static Connection newInstance( String url, String user, String pass )
    {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection( url, user, pass );    
        }
        catch( SQLException ex)
        {
            System.out.println("Error creando el objeto");
        }
        return conn;
    }
          
    
}
