/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Rentcar", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rentcar.findAll", query = "SELECT r FROM Rentcar r"),
    @NamedQuery(name = "Rentcar.findByRId", query = "SELECT r FROM Rentcar r WHERE r.rId = :rId"),
    @NamedQuery(name = "Rentcar.findByRPickUp", query = "SELECT r FROM Rentcar r WHERE r.rPickUp = :rPickUp"),
    @NamedQuery(name = "Rentcar.findByRDropOff", query = "SELECT r FROM Rentcar r WHERE r.rDropOff = :rDropOff"),
    @NamedQuery(name = "Rentcar.findByRDriver", query = "SELECT r FROM Rentcar r WHERE r.rDriver = :rDriver"),
    @NamedQuery(name = "Rentcar.findByRStatus", query = "SELECT r FROM Rentcar r WHERE r.rStatus = :rStatus")})
public class Rentcar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "R_ID", nullable = false)
    private Integer rId;
    @Column(name = "R_PickUp")
    @Temporal(TemporalType.DATE)
    private Date rPickUp;
    @Column(name = "R_DropOff")
    @Temporal(TemporalType.DATE)
    private Date rDropOff;
    @Column(name = "R_Driver")
    private Boolean rDriver;
    @Column(name = "R_Status", length = 20)
    private String rStatus;
    @JoinColumn(name = "U_ID", referencedColumnName = "U_ID")
    @ManyToOne
    private Users uId;
    @JoinColumn(name = "C_ID", referencedColumnName = "C_ID")
    @ManyToOne
    private CarforRent cId;

    public Rentcar() {
    }

    public Rentcar(Integer rId) {
        this.rId = rId;
    }

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public Date getRPickUp() {
        return rPickUp;
    }

    public void setRPickUp(Date rPickUp) {
        this.rPickUp = rPickUp;
    }

    public Date getRDropOff() {
        return rDropOff;
    }

    public void setRDropOff(Date rDropOff) {
        this.rDropOff = rDropOff;
    }

    public Boolean getRDriver() {
        return rDriver;
    }

    public void setRDriver(Boolean rDriver) {
        this.rDriver = rDriver;
    }

    public String getRStatus() {
        return rStatus;
    }

    public void setRStatus(String rStatus) {
        this.rStatus = rStatus;
    }

    public Users getUId() {
        return uId;
    }

    public void setUId(Users uId) {
        this.uId = uId;
    }

    public CarforRent getCId() {
        return cId;
    }

    public void setCId(CarforRent cId) {
        this.cId = cId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rId != null ? rId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rentcar)) {
            return false;
        }
        Rentcar other = (Rentcar) object;
        if ((this.rId == null && other.rId != null) || (this.rId != null && !this.rId.equals(other.rId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Rentcar[ rId=" + rId + " ]";
    }
    
}
