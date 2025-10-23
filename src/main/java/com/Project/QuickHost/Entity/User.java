package com.Project.QuickHost.Entity;

import com.Project.QuickHost.Entity.enums.Gender;
import com.Project.QuickHost.Entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.util.Collection;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "app_user") // Table name in postgres database cant be created
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String DOB;


    @Column(nullable = false,unique = true)//Will automatically create index fr it
    @Email(message = "Invalid email format")
    private String email;

    private String password;//use Bcrypt password enco

    @ElementCollection(fetch=FetchType.EAGER)//create another table Appuser_roles
    @Enumerated(EnumType.STRING)//if using ordinal it mean roles are stored in number
    private Set<Roles>roles;//storing roles a user can have[one to one mapping]


    /**
     * @return ,will used to authorize people,
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * @return the email
     */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
