package com.team3.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column(name = "password")
    private String password;

    @Transient
    private String passwordConfirm;

    @Column(name = "registrationDate")
    private Date registrationDate;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public User(String name, String surname, String email, Date birthday, String password, Date registrationDate, String specialization, Status status) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
        this.registrationDate = registrationDate;
        this.specialization = specialization;
        this.status = status;
    }

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Order> order;

}