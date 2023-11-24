/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Pablo
 */
@Entity
@Table(name = "empleado_datos_prof", catalog = "pruebas", schema = "")
@NamedQueries({
    @NamedQuery(name = "EmpleadoDatosProf.findAll", query = "SELECT e FROM EmpleadoDatosProf e"),
    @NamedQuery(name = "EmpleadoDatosProf.findByDni", query = "SELECT e FROM EmpleadoDatosProf e WHERE e.dni = :dni"),
    @NamedQuery(name = "EmpleadoDatosProf.findByCategoria", query = "SELECT e FROM EmpleadoDatosProf e WHERE e.categoria = :categoria"),
    @NamedQuery(name = "EmpleadoDatosProf.findBySueldoBrutoAnual", query = "SELECT e FROM EmpleadoDatosProf e WHERE e.sueldoBrutoAnual = :sueldoBrutoAnual")})
public class EmpleadoDatosProf implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false, length = 9)
    private String dni;
    @Basic(optional = false)
    @Column(nullable = false, length = 2)
    private String categoria;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sueldo_bruto_anual", precision = 8, scale = 2)
    private BigDecimal sueldoBrutoAnual;
    @JoinColumn(name = "dni", referencedColumnName = "dni", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Empleado empleado;

    public EmpleadoDatosProf() {
    }

    public EmpleadoDatosProf(String dni) {
        this.dni = dni;
    }

    public EmpleadoDatosProf(String dni, String categoria) {
        this.dni = dni;
        this.categoria = categoria;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getSueldoBrutoAnual() {
        return sueldoBrutoAnual;
    }

    public void setSueldoBrutoAnual(BigDecimal sueldoBrutoAnual) {
        this.sueldoBrutoAnual = sueldoBrutoAnual;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof EmpleadoDatosProf)) {
            return false;
        }
        EmpleadoDatosProf other = (EmpleadoDatosProf) object;
        if ((this.dni == null && other.dni != null) || (this.dni != null && !this.dni.equals(other.dni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.EmpleadoDatosProf[ dni=" + dni + " ]";
    }
    
}
