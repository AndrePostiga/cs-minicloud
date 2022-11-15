package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import exceptions.PreconditionFailException;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "VirtualMachine.GetAll", query = "select p from VirtualMachine p order by p.id"),
        @NamedQuery(name = "VirtualMachine.GetById", query = "select p from VirtualMachine p where p.id = ?1")
})
@Entity
@Table(name = "VirtualMachines")
public class VirtualMachine extends Machine{

    public VirtualMachine() {}

    @Column(nullable = false)
    protected int vCores;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArchitectureEnum architecture;

    @OneToOne(mappedBy = "virtualMachine", cascade = CascadeType.PERSIST)
    private VirtualPhysicalMachineAllocation allocation;

    @Enumerated(EnumType.STRING)
    private VirtualMachineStatusEnum status;

    public static VirtualMachine CreateVirtualMachine(
            int vCores, ArchitectureEnum architecture, Long memoryInBytes, Boolean hasGpu, Long ssdInBytes,
            Long hdInBytes, OperationalSystemEnum operationalSystem, VirtualMachineStatusEnum status,
            PhysicalMachine physicalMachineToAllocate
    ) throws PreconditionFailException {
        VirtualMachine machine = new VirtualMachine(vCores, architecture, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, operationalSystem, status, physicalMachineToAllocate);
        return machine;
    }

    public VirtualMachine(int vCores, ArchitectureEnum architecture, Long memoryInBytes, Boolean hasGpu, Long ssdInBytes, Long hdInBytes, OperationalSystemEnum operationalSystem, VirtualMachineStatusEnum status, PhysicalMachine physicalMachineToAllocate) throws PreconditionFailException {
        super(memoryInBytes, hasGpu, ssdInBytes, hdInBytes, operationalSystem);

        if (vCores < 0) {
            throw new IllegalArgumentException("Não é possível criar uma máquina virtual sem vCores");
        }

        this.vCores = vCores;
        this.status = status;
        this.architecture = architecture;

        try {
            this.allocation = physicalMachineToAllocate.Allocate(this);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public ArchitectureEnum getArchitecture() {
        return architecture;
    }

    public int getvCores() {
        return vCores;
    }

    public VirtualMachineStatusEnum getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "VirtualMachine{" +
                "vCores=" + vCores +
                ", architecture=" + architecture +
                ", allocation=" + allocation +
                ", status=" + status +
                '}';
    }
}
