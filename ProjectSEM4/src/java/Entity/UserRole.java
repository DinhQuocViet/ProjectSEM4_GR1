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
@Table(name = "UserRole", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u"),
    @NamedQuery(name = "UserRole.findByUrId", query = "SELECT u FROM UserRole u WHERE u.urId = :urId"),
    @NamedQuery(name = "UserRole.findByURName", query = "SELECT u FROM UserRole u WHERE u.uRName = :uRName")})
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "UR_ID")
    private Integer urId;
    @Column(name = "UR_Name")
    private String uRName;
    @OneToMany(mappedBy = "uRole")
    private List<Users> usersList;

    public UserRole() {
    }

    public UserRole(Integer urId) {
        this.urId = urId;
    }

    public Integer getUrId() {
        return urId;
    }

    public void setUrId(Integer urId) {
        this.urId = urId;
    }

    public String getURName() {
        return uRName;
    }

    public void setURName(String uRName) {
        this.uRName = uRName;
    }

    @XmlTransient
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (urId != null ? urId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRole)) {
            return false;
        }
        UserRole other = (UserRole) object;
        if ((this.urId == null && other.urId != null) || (this.urId != null && !this.urId.equals(other.urId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.UserRole[ urId=" + urId + " ]";
    }
    
}
