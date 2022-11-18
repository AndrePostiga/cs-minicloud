package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import exceptions.PreconditionFailException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "VirtualMachine.GetAll", query = "select p from VirtualMachine p order by p.id"),
        @NamedQuery(name = "VirtualMachine.GetById", query = "select p from VirtualMachine p where p.id = ?1")
})
@Entity
@Table(name = "VirtualMachines")
@OnDelete(action = OnDeleteAction.CASCADE)
public class VirtualMachine extends Machine {

    public VirtualMachine() {
    }

    @Column(nullable = false)
    protected int vCores;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArchitectureEnum architecture;

    @OneToOne(mappedBy = "virtualMachine", fetch = FetchType.EAGER)
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


        this.allocation = physicalMachineToAllocate.Allocate(this);
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

    public VirtualPhysicalMachineAllocation getAllocation() {
        return allocation;
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

    public void UpdateVCores(int vCores) throws PreconditionFailException {
        if (vCores < 1)
            throw new PreconditionFailException("vCores não pode ser menor do que 1");

        if (vCores > this.allocation.physicalMachine.cpu.getCores())
            throw new PreconditionFailException("Não é possível aumentar a quantidade de vCores de forma que tenha mais cores que a máquina física pai");

        this.vCores = vCores;
    }

    public void UpdateMemory(long memory) throws PreconditionFailException {
        if (memory < 1)
            throw new PreconditionFailException("Memória não pode ser menor do que 1");

        long usedMemory = this.allocation.physicalMachine.getMemoryInBytes() - this.allocation.physicalMachine.GetRemainMemoryInBytes();
        usedMemory -= this.memoryInBytes;
        if ((memory + usedMemory) > this.allocation.physicalMachine.getMemoryInBytes())
            throw new PreconditionFailException("Não é possível alocar mais memória do que a máquina fisica pai possui");

        this.memoryInBytes = memory;
    }

    public void UpdateSsd(long ssd) throws PreconditionFailException {
        if (ssd < 1)
            throw new PreconditionFailException("Ssd não pode ser menor do que 1");

        long usedMemory = this.allocation.physicalMachine.getSsdInBytes() - this.allocation.physicalMachine.GetRemainSsdInBytes();
        usedMemory -= this.ssdInBytes;
        if ((ssd + usedMemory) > this.allocation.physicalMachine.getSsdInBytes())
            throw new PreconditionFailException("Não é possível alocar mais ssd do que a máquina fisica pai possui");

        this.ssdInBytes = ssd;
    }

    public void UpdateHd(long hd) throws PreconditionFailException {
        if (hd < 1)
            throw new PreconditionFailException("Hd não pode ser menor do que 1");

        long usedMemory = this.allocation.physicalMachine.getHdInBytes() - this.allocation.physicalMachine.GetRemainHdInBytes();
        usedMemory -= this.hdInBytes;
        if ((hd + usedMemory) > this.allocation.physicalMachine.getHdInBytes())
            throw new PreconditionFailException("Não é possível alocar mais ssd do que a máquina fisica pai possui");

        this.hdInBytes = hd;
    }

    public String Print() {
        String leftAlignFormat = "| %-5d | %-6s | %-6d | %-11s | %-11s | %-11s | %-11s | %-11s | %-19d |%n";
        String printString = String.format(leftAlignFormat,
                this.getId(),
                this.getStatus(),
                this.getvCores(),
                this.getArchitecture(),
                this.getMemoryInBytes(),
                this.getSsdInBytes(),
                this.getHdInBytes(),
                this.getOperationalSystem(),
                this.allocation.physicalMachine.getId());

        return printString;
    }
}
