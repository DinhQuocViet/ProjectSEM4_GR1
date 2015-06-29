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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "Users", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUId", query = "SELECT u FROM Users u WHERE u.uId = :uId"),
    @NamedQuery(name = "Users.findByUusername", query = "SELECT u FROM Users u WHERE u.uusername = :uusername"),
    @NamedQuery(name = "Users.findByUpassword", query = "SELECT u FROM Users u WHERE u.upassword = :upassword"),
    @NamedQuery(name = "Users.findByUname", query = "SELECT u FROM Users u WHERE u.uname = :uname"),
    @NamedQuery(name = "Users.findByUSex", query = "SELECT u FROM Users u WHERE u.uSex = :uSex"),
    @NamedQuery(name = "Users.findByUAddress", query = "SELECT u FROM Users u WHERE u.uAddress = :uAddress"),
    @NamedQuery(name = "Users.findByUEmail", query = "SELECT u FROM Users u WHERE u.uEmail = :uEmail"),
    @NamedQuery(name = "Users.findByUPhonenumber", query = "SELECT u FROM Users u WHERE u.uPhonenumber = :uPhonenumber")})
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "U_ID", nullable = false)
    private Integer uId;
    @Column(name = "U_username", length = 50)
    private String uusername;
    @Column(name = "U_password", length = 50)
    private String upassword;
    @Column(name = "U_name", length = 50)
    private String uname;
    @Column(name = "U_Sex")
    private Boolean uSex;
    @Column(name = "U_Address", length = 2147483647)
    private String uAddress;
    @Column(name = "U_Email", length = 100)
    private String uEmail;
    @Column(name = "U_Phonenumber")
    private Integer uPhonenumber;
    @JoinColumn(name = "U_Role", referencedColumnName = "UR_ID")
    @ManyToOne
    private UserRole uRole;
    @OneToMany(mappedBy = "uId")
    private Collection<BookingTour> bookingTourCollection;
    @OneToMany(mappedBy = "uId")
    private Collection<Rentcar> rentcarCollection;

    public Users() {
    }

    public Users(Integer uId) {
        this.uId = uId;
    }

    public Integer getUId() {
        return uId;
    }

    public void setUId(Integer uId) {
        this.uId = uId;
    }

    public String getUusername() {
        return uusername;
    }

    public void setUusername(String uusername) {
        this.uusername = uusername;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Boolean getUSex() {
        return uSex;
    }

    public void setUSex(Boolean uSex) {
        this.uSex = uSex;
    }

    public String getUAddress() {
        return uAddress;
    }

    public void setUAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public String getUEmail() {
        return uEmail;
    }

    public void setUEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public Integer getUPhonenumber() {
        return uPhonenumber;
    }

    public void setUPhonenumber(Integer uPhonenumber) {
        this.uPhonenumber = uPhonenumber;
    }

    public UserRole getURole() {
        return uRole;
    }

    public void setURole(UserRole uRole) {
        this.uRole = uRole;
    }

    @XmlTransient
    public Collection<BookingTour> getBookingTourCollection() {
        return bookingTourCollection;
    }

    public void setBookingTourCollection(Collection<BookingTour> bookingTourCollection) {
        this.bookingTourCollection = bookingTourCollection;
    }

    @XmlTransient
    public Collection<Rentcar> getRentcarCollection() {
        return rentcarCollection;
    }

    public void setRentcarCollection(Collection<Rentcar> rentcarCollection) {
        this.rentcarCollection = rentcarCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uId != null ? uId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.uId == null && other.uId != null) || (this.uId != null && !this.uId.equals(other.uId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Users[ uId=" + uId + " ]";
    }
    
}
