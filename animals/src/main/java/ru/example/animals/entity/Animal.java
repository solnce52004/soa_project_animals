package ru.example.animals.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;
import ru.example.animals.converter.GenderTypeAttributeConverter;
import ru.example.animals.enums.GenderType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Table(name = "animals")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DynamicUpdate
@DynamicInsert
public class Animal {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_type_id", nullable = false)
    @Fetch(value = FetchMode.JOIN)
    private AnimalType animalType = new AnimalType();

    @Column(name = "animal_name", nullable = false, unique = true)
    private String animalName;

    //    @Enumerated(EnumType.STRING) - не использовать совместно с конвертером
    @Convert(converter = GenderTypeAttributeConverter.class)
    @Column(name = "gender", nullable = false, columnDefinition = "GENDER NOT NULL DEFAULT 'u'::GENDER")
    private GenderType gender = GenderType.UNTITLED;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.ALWAYS)
    @Column(name = "birthdate", columnDefinition = "TIMESTAMP")
    @ColumnDefault("2000-01-01 00:00:00")
    private Date birthdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Generated(GenerationTime.ALWAYS)
    @Column(name = "created_at",
            updatable = false,
            insertable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "updated_at",
            insertable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return getUsername().equals(animal.getUsername()) &&
                getAnimalName().equals(animal.getAnimalName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAnimalName());
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + animalName + '\'' +
                ", gender=" + gender +
                ", birthdate=" + birthdate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
