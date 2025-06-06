package com.andrewbycode.cookdocs.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private Integer clientRegistration;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Recipe> recipes;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Image image;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Like> likes;

    @Transient
    private List<Review> reviews;

    public User(String userName) {
        this.userName = userName;
    }
}
