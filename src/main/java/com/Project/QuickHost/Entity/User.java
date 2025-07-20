package com.Project.QuickHost.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "app_user") // Table name in postgres database cant be created
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @Column(nullable = false,unique = true)//Will automatically create index fr it
    private String email;

    private String password;//use Bcrypt password enco

    @ElementCollection(fetch=FetchType.EAGER)//create another table Appuser_roles
    @Enumerated(EnumType.ORDINAL)//if using ordinal it mean roles are stored in number
    private Set<Roles>roles;//storing roles a user can have[one to one mapping]



}
