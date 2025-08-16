package com.reports.CultDataReports.model;


import jakarta.persistence.*;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String firstname;

    @Column(nullable = false, length = 40)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date addmissionDate;


    public Customer() {
        super();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getAddmissionDate() {
        return addmissionDate;
    }

    public void setAddmissionDate(Date addmissionDate) {
        this.addmissionDate = addmissionDate;
    }
}
