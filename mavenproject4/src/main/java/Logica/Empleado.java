/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Pablo
 */
@Entity
@Table(name = "empleado", catalog = "pruebas", schema = "")
@NamedQueries({
    @NamedQuery(name = "Empleado_1.findAll", query = "SELECT e FROM Empleado_1 e"),
    @NamedQuery(name = "Empleado_1.findByDni", query = "SELECT e FROM Empleado_1 e WHERE e.dni = :dni"),
    @NamedQuery(name = "Empleado_1.findByNomEmp", query = "SELECT e FROM Empleado_1 e WHERE e.nomEmp = :nomEmp")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 9)
    private String dni;
    @Basic(optional = false)
    @Column(name = "nom_emp", nullable = false, length = 40)
    private String nomEmp;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "empleado")
    private EmpleadoDatosProf empleadoDatosProf;
    @JoinColumn(name = "id_depto", referencedColumnName = "id_depto", nullable = false)
    @ManyToOne(optional = false)
    private Departamento idDepto;

    public Empleado() {
    }

    public Empleado(String dni) {
        this.dni = dni;
    }

    public Empleado(String dni, String nomEmp) {
        this.dni = dni;
        this.nomEmp = nomEmp;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNomEmp() {
        return nomEmp;
    }

    public void setNomEmp(String nomEmp) {
        this.nomEmp = nomEmp;
    }

    public EmpleadoDatosProf getEmpleadoDatosProf() {
        return empleadoDatosProf;
    }

    public void setEmpleadoDatosProf(EmpleadoDatosProf empleadoDatosProf) {
        this.empleadoDatosProf = empleadoDatosProf;
    }

    public Departamento getIdDepto() {
        return idDepto;
    }

    public void setIdDepto(Departamento idDepto) {
        this.idDepto = idDepto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dni != null ? dni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.dni == null && other.dni != null) || (this.dni != null && !this.dni.equals(other.dni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Empleado_1[ dni=" + dni + " ]";
    }
    
}
