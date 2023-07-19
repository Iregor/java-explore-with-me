package ru.practicum.user.data;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Email
    @NotNull
    @Column(name = "email")
    @Size(min = 6, max = 254)
    private String email;
    @NotBlank
    @Column(name = "name")
    @Size(min = 2, max = 250)
    private String name;
}
