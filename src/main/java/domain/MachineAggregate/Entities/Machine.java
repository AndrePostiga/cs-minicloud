package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;

import javax.persistence.*;

@Entity
@Table(name = "Machines")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected Long memoryInBytes;

    @ManyToOne(optional = false)
    protected CPU cpu;

    @Column(nullable = false, columnDefinition = "boolean default false")
    protected Boolean hasGpu;

    protected Long ssdInBytes;

    protected Long hdInBytes;

    @Enumerated(EnumType.STRING)
    protected OperationalSystemEnum operationalSystem;
}
