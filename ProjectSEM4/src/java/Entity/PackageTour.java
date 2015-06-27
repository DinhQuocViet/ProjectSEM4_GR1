/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "PackageTour", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PackageTour.findAll", query = "SELECT p FROM PackageTour p"),
    @NamedQuery(name = "PackageTour.findByPId", query = "SELECT p FROM PackageTour p WHERE p.pId = :pId"),
    @NamedQuery(name = "PackageTour.findByPTitle", query = "SELECT p FROM PackageTour p WHERE p.pTitle = :pTitle"),
    @NamedQuery(name = "PackageTour.findByPDuration", query = "SELECT p FROM PackageTour p WHERE p.pDuration = :pDuration"),
    @NamedQuery(name = "PackageTour.findByPDescription", query = "SELECT p FROM PackageTour p WHERE p.pDescription = :pDescription"),
    @NamedQuery(name = "PackageTour.findByPPrice", query = "SELECT p FROM PackageTour p WHERE p.pPrice = :pPrice")})
public class PackageTour implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "P_ID")
    private Integer pId;
    @Column(name = "P_Title")
    private String pTitle;
    @Column(name = "P_Duration")
    private String pDuration;
    @Column(name = "P_Description")
    private String pDescription;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "P_Price")
    private Double pPrice;
    @JoinColumn(name = "P_Type", referencedColumnName = "T_ID")
    @ManyToOne
    private TourType pType;

    public PackageTour() {
    }

    public PackageTour(Integer pId) {
        this.pId = pId;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public String getPTitle() {
        return pTitle;
    }

    public void setPTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getPDuration() {
        return pDuration;
    }

    public void setPDuration(String pDuration) {
        this.pDuration = pDuration;
    }

    public String getPDescription() {
        return pDescription;
    }

    public void setPDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public Double getPPrice() {
        return pPrice;
    }

    public void setPPrice(Double pPrice) {
        this.pPrice = pPrice;
    }

    public TourType getPType() {
        return pType;
    }

    public void setPType(TourType pType) {
        this.pType = pType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pId != null ? pId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PackageTour)) {
            return false;
        }
        PackageTour other = (PackageTour) object;
        if ((this.pId == null && other.pId != null) || (this.pId != null && !this.pId.equals(other.pId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.PackageTour[ pId=" + pId + " ]";
    }
    
}
