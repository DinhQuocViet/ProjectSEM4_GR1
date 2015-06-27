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
@Table(name = "BookingTour", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookingTour.findAll", query = "SELECT b FROM BookingTour b"),
    @NamedQuery(name = "BookingTour.findByBId", query = "SELECT b FROM BookingTour b WHERE b.bId = :bId"),
    @NamedQuery(name = "BookingTour.findByBPerson", query = "SELECT b FROM BookingTour b WHERE b.bPerson = :bPerson"),
    @NamedQuery(name = "BookingTour.findByBstatus", query = "SELECT b FROM BookingTour b WHERE b.bstatus = :bstatus")})
public class BookingTour implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "B_ID")
    private Integer bId;
    @Column(name = "B_Person")
    private Integer bPerson;
    @Column(name = "B_status")
    private String bstatus;
    @JoinColumn(name = "U_ID", referencedColumnName = "U_ID")
    @ManyToOne
    private Users uId;

    public BookingTour() {
    }

    public BookingTour(Integer bId) {
        this.bId = bId;
    }

    public Integer getBId() {
        return bId;
    }

    public void setBId(Integer bId) {
        this.bId = bId;
    }

    public Integer getBPerson() {
        return bPerson;
    }

    public void setBPerson(Integer bPerson) {
        this.bPerson = bPerson;
    }

    public String getBstatus() {
        return bstatus;
    }

    public void setBstatus(String bstatus) {
        this.bstatus = bstatus;
    }

    public Users getUId() {
        return uId;
    }

    public void setUId(Users uId) {
        this.uId = uId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bId != null ? bId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookingTour)) {
            return false;
        }
        BookingTour other = (BookingTour) object;
        if ((this.bId == null && other.bId != null) || (this.bId != null && !this.bId.equals(other.bId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.BookingTour[ bId=" + bId + " ]";
    }
    
}
