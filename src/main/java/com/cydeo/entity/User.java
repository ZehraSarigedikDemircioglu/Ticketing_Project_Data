package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
//@Where(clause = "is_deleted=false") // any repository which is using User entity, (this project for UserRepository)
// whatever queries inside, include that where clause. We do not create to each method ourselves thanks to Spring
// select * from users where ... and is_deleted = false;
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;
    @ManyToOne
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
}
