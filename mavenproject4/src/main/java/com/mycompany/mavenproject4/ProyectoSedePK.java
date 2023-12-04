/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject4;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author administrador
 */
@Embeddable
public class ProyectoSedePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_proy")
    private int idProy;
    @Basic(optional = false)
    @Column(name = "id_sede")
    private int idSede;

    public ProyectoSedePK() {
    }

    public ProyectoSedePK(int idProy, int idSede) {
        this.idProy = idProy;
        this.idSede = idSede;
    }

    public int getIdProy() {
        return idProy;
    }

    public void setIdProy(int idProy) {
        this.idProy = idProy;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idProy;
        hash += (int) idSede;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProyectoSedePK)) {
            return false;
        }
        ProyectoSedePK other = (ProyectoSedePK) object;
        if (this.idProy != other.idProy) {
            return false;
        }
        if (this.idSede != other.idSede) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject4.ProyectoSedePK[ idProy=" + idProy + ", idSede=" + idSede + " ]";
    }
    
}
