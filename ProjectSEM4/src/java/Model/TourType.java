/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "TourType", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TourType.findAll", query = "SELECT t FROM TourType t"),
    @NamedQuery(name = "TourType.findByTId", query = "SELECT t FROM TourType t WHERE t.tId = :tId"),
    @NamedQuery(name = "TourType.findByTtype", query = "SELECT t FROM TourType t WHERE t.ttype = :ttype")})
public class TourType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "T_ID", nullable = false)
    private Integer tId;
    @Column(name = "T_type", length = 50)
    private String ttype;
    @OneToMany(mappedBy = "pType")
    private Collection<PackageTour> packageTourCollection;

    public TourType() {
    }

    public TourType(Integer tId) {
        this.tId = tId;
    }

    public Integer getTId() {
        return tId;
    }

    public void setTId(Integer tId) {
        this.tId = tId;
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    @XmlTransient
    public Collection<PackageTour> getPackageTourCollection() {
        return packageTourCollection;
    }

    public void setPackageTourCollection(Collection<PackageTour> packageTourCollection) {
        this.packageTourCollection = packageTourCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tId != null ? tId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TourType)) {
            return false;
        }
        TourType other = (TourType) object;
        if ((this.tId == null && other.tId != null) || (this.tId != null && !this.tId.equals(other.tId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.TourType[ tId=" + tId + " ]";
    }
    
}
