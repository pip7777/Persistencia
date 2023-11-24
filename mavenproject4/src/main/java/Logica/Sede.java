/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Pablo
 */
@Entity
@Table(catalog = "pruebas", schema = "")
@NamedQueries({
    @NamedQuery(name = "Sede.findAll", query = "SELECT s FROM Sede s"),
    @NamedQuery(name = "Sede.findByIdSede", query = "SELECT s FROM Sede s WHERE s.idSede = :idSede"),
    @NamedQuery(name = "Sede.findByNomSede", query = "SELECT s FROM Sede s WHERE s.nomSede = :nomSede")})
public class Sede implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sede", nullable = false)
    private Integer idSede;
    @Basic(optional = false)
    @Column(name = "nom_sede", nullable = false, length = 20)
    private String nomSede;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sede")
    private Collection<ProyectoSede> proyectoSedeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSede")
    private Collection<Departamento> departamentoCollection;

    public Sede() {
    }

    public Sede(Integer idSede) {
        this.idSede = idSede;
    }

    public Sede(Integer idSede, String nomSede) {
        this.idSede = idSede;
        this.nomSede = nomSede;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getNomSede() {
        return nomSede;
    }

    public void setNomSede(String nomSede) {
        this.nomSede = nomSede;
    }

    public Collection<ProyectoSede> getProyectoSedeCollection() {
        return proyectoSedeCollection;
    }

    public void setProyectoSedeCollection(Collection<ProyectoSede> proyectoSedeCollection) {
        this.proyectoSedeCollection = proyectoSedeCollection;
    }

    public Collection<Departamento> getDepartamentoCollection() {
        return departamentoCollection;
    }

    public void setDepartamentoCollection(Collection<Departamento> departamentoCollection) {
        this.departamentoCollection = departamentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSede != null ? idSede.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sede)) {
            return false;
        }
        Sede other = (Sede) object;
        if ((this.idSede == null && other.idSede != null) || (this.idSede != null && !this.idSede.equals(other.idSede))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Sede[ idSede=" + idSede + " ]";
    }
    
}
