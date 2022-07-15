package com.moorhuhnservice.moorhuhnservice.data;

import java.util.Set;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  @Column(name = "name", nullable = false, unique = true)
  String name;

  @OneToMany(cascade = CascadeType.ALL)
  Set<Question> questions;

  public Configuration(String name, Set<Question> questions) {
    this.name = name;
    this.questions = questions;
  }
}
