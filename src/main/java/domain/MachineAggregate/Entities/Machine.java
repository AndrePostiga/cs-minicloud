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

    @Column(nullable = false, columnDefinition = "boolean default false")
    protected Boolean hasGpu;

    protected Long ssdInBytes;

    protected Long hdInBytes;

    @Enumerated(EnumType.STRING)
    protected OperationalSystemEnum operationalSystem;

    protected Machine() {}

    protected Machine(Long memoryInBytes, Boolean hasGpu, Long ssdInBytes, Long hdInBytes, OperationalSystemEnum operationalSystem) {
        if (memoryInBytes == 0)
            throw new IllegalArgumentException("Não é possível criar uma máquina física sem memória ram");

        if ((ssdInBytes + hdInBytes) == 0)
            throw new IllegalArgumentException("Não é possível criar uma máquina física sem memória de armazenamento");


        this.memoryInBytes = memoryInBytes;
        this.hasGpu = hasGpu;
        this.ssdInBytes = ssdInBytes;
        this.hdInBytes = hdInBytes;
        this.operationalSystem = operationalSystem;
    }

    public Long getMemoryInBytes() {
        return memoryInBytes;
    }

    public Long getSsdInBytes() {
        return ssdInBytes;
    }

    public Long getHdInBytes() {
        return hdInBytes;
    }

    public Boolean getHasGpu() {
        return hasGpu;
    }

    public OperationalSystemEnum getOperationalSystem() {
        return operationalSystem;
    }
}
