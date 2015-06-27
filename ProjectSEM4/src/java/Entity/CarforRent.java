/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
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
@Table(name = "CarforRent", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarforRent.findAll", query = "SELECT c FROM CarforRent c"),
    @NamedQuery(name = "CarforRent.findByCId", query = "SELECT c FROM CarforRent c WHERE c.cId = :cId"),
    @NamedQuery(name = "CarforRent.findByCName", query = "SELECT c FROM CarforRent c WHERE c.cName = :cName"),
    @NamedQuery(name = "CarforRent.findByCModel", query = "SELECT c FROM CarforRent c WHERE c.cModel = :cModel"),
    @NamedQuery(name = "CarforRent.findByCType", query = "SELECT c FROM CarforRent c WHERE c.cType = :cType"),
    @NamedQuery(name = "CarforRent.findByCSeating", query = "SELECT c FROM CarforRent c WHERE c.cSeating = :cSeating"),
    @NamedQuery(name = "CarforRent.findByCAirconditioner", query = "SELECT c FROM CarforRent c WHERE c.cAirconditioner = :cAirconditioner"),
    @NamedQuery(name = "CarforRent.findByCPrice", query = "SELECT c FROM CarforRent c WHERE c.cPrice = :cPrice")})
public class CarforRent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "C_ID")
    private Integer cId;
    @Column(name = "C_Name")
    private String cName;
    @Column(name = "C_Model")
    private String cModel;
    @Column(name = "C_Type")
    private String cType;
    @Column(name = "C_Seating")
    private Integer cSeating;
    @Column(name = "C_Airconditioner")
    private Boolean cAirconditioner;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "C_Price")
    private Double cPrice;
    @OneToMany(mappedBy = "cId")
    private List<Rentcar> rentcarList;

    public CarforRent() {
    }

    public CarforRent(Integer cId) {
        this.cId = cId;
    }

    public Integer getCId() {
        return cId;
    }

    public void setCId(Integer cId) {
        this.cId = cId;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCModel() {
        return cModel;
    }

    public void setCModel(String cModel) {
        this.cModel = cModel;
    }

    public String getCType() {
        return cType;
    }

    public void setCType(String cType) {
        this.cType = cType;
    }

    public Integer getCSeating() {
        return cSeating;
    }

    public void setCSeating(Integer cSeating) {
        this.cSeating = cSeating;
    }

    public Boolean getCAirconditioner() {
        return cAirconditioner;
    }

    public void setCAirconditioner(Boolean cAirconditioner) {
        this.cAirconditioner = cAirconditioner;
    }

    public Double getCPrice() {
        return cPrice;
    }

    public void setCPrice(Double cPrice) {
        this.cPrice = cPrice;
    }

    @XmlTransient
    public List<Rentcar> getRentcarList() {
        return rentcarList;
    }

    public void setRentcarList(List<Rentcar> rentcarList) {
        this.rentcarList = rentcarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cId != null ? cId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarforRent)) {
            return false;
        }
        CarforRent other = (CarforRent) object;
        if ((this.cId == null && other.cId != null) || (this.cId != null && !this.cId.equals(other.cId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CarforRent[ cId=" + cId + " ]";
    }
    
}
