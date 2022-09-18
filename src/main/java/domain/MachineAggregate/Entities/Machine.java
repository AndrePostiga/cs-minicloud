package domain.MachineAggregate.Entities;

import javax.persistence.*;

@Entity
@Table(name = "machines")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "memory_in_bytes", nullable = false)
    protected Long memoryInBytes;

    @ManyToOne(optional = false)
    protected CPU cpu;

    @Column(name = "has_gpu", nullable = false, columnDefinition = "boolean default false")
    protected Boolean hasGpu;

    @Column(name = "ssd_in_bytes")
    protected Long ssdInBytes;

    @Column(name = "hd_in_bytes")
    protected Long hdInBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "operational_system")
    protected OperationalSystemEnum operationalSystem;
}
