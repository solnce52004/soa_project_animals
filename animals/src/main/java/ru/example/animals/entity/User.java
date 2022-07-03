package ru.example.animals.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


//TODO: заглушка!!!

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
@NamedEntityGraph(
        name = "user",
        attributeNodes = {
                @NamedAttributeNode("id"),
                @NamedAttributeNode(value = "animals", subgraph = "user-animals-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "user-animals-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("user")
                        }
                )
        }
)
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    private Set<Animal> animals = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
