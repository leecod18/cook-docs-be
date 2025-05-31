package com.andrewbycode.cookdocs.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;
    private Integer imageType;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
