package uk.gov.digital.ho.hocs.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lists")
@Access(AccessType.FIELD)
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
public class DataList {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Getter
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name ="list_id", referencedColumnName = "id")
    @Getter
    private Set<DataListEntity> entities = new HashSet<>();


    public DataList(String name, Set<DataListEntity> listEntities) {
        this.name = name;
        this.entities = listEntities;
    }
}