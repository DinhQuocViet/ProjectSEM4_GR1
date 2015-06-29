/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "placesvisit", catalog = "E2W", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Placesvisit.findAll", query = "SELECT p FROM Placesvisit p"),
    @NamedQuery(name = "Placesvisit.findByVId", query = "SELECT p FROM Placesvisit p WHERE p.vId = :vId"),
    @NamedQuery(name = "Placesvisit.findByVTitle", query = "SELECT p FROM Placesvisit p WHERE p.vTitle = :vTitle"),
    @NamedQuery(name = "Placesvisit.findByVDescription", query = "SELECT p FROM Placesvisit p WHERE p.vDescription = :vDescription")})
public class Placesvisit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "V_ID", nullable = false)
    private Integer vId;
    @Column(name = "V_Title", length = 100)
    private String vTitle;
    @Column(name = "V_Description", length = 1073741823)
    private String vDescription;

    public Placesvisit() {
    }

    public Placesvisit(Integer vId) {
        this.vId = vId;
    }

    public Integer getVId() {
        return vId;
    }

    public void setVId(Integer vId) {
        this.vId = vId;
    }

    public String getVTitle() {
        return vTitle;
    }

    public void setVTitle(String vTitle) {
        this.vTitle = vTitle;
    }

    public String getVDescription() {
        return vDescription;
    }

    public void setVDescription(String vDescription) {
        this.vDescription = vDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vId != null ? vId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Placesvisit)) {
            return false;
        }
        Placesvisit other = (Placesvisit) object;
        if ((this.vId == null && other.vId != null) || (this.vId != null && !this.vId.equals(other.vId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Placesvisit[ vId=" + vId + " ]";
    }
    
}
