package com.esgi.loginsubscription.payload;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


public class UserDto implements Serializable {
    private Long id;

    private Date connectedAt;

    @NotNull
    @Size(min = 2, max = 50)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 50)
    private String lastname;

    @NotNull
    @Size(min = 10, max = 150)
    private String email;

    @NotNull
    @Size(min = 5, max = 10)
    private String agency;

    @NotNull
    @Size(min = 1, max = 6)
    private String roleName;

    @NotNull
    private Date dateStart;

    private Date dateEnd;

    public UserDto(Long id, Date connectedAt, String firstname, String lastname, String email, String agency, String roleName, Date dateStart, Date dateEnd) {
        this.id = id;
        this.connectedAt = connectedAt;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.agency = agency;
        this.roleName = roleName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(Date connectedAt) {
        this.connectedAt = connectedAt;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agencyCost) {
        this.agency = agencyCost;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    @InitBinder
    public void dataBinding(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("connectedAt");
        dataBinder.setAllowedFields("firstname", "lastname", "email", "agencyCost", "roleName", "dateStart", "dateEnd");
    }
}
