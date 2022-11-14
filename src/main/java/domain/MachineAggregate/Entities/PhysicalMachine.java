package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.PhysicalMachineStatusEnum;
import exceptions.PreconditionFailException;

import javax.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "PhysicalMachine.GetAll", query = "select p from PhysicalMachine p order by p.id"),
        @NamedQuery(name = "PhysicalMachine.GetById", query = "select p from PhysicalMachine p where p.id = ?1")
})
@Entity
@Table(name = "PhysicalMachines")
public class PhysicalMachine extends Machine{

    public PhysicalMachine() {}

    @ManyToOne(optional = false)
    protected CPU cpu;

    @OneToMany(mappedBy = "physicalMachine", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<VirtualPhysicalMachineAllocation> allocations;

    @Enumerated(EnumType.STRING)
    private PhysicalMachineStatusEnum status;

    public static PhysicalMachine CreatePhysicalMachine(CPU cpu, Long memoryInBytes, Boolean hasGpu, Long ssdInBytes, Long hdInBytes, OperationalSystemEnum operationalSystem) {
        PhysicalMachineStatusEnum status = PhysicalMachineStatusEnum.Free;
        return new PhysicalMachine(cpu, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, operationalSystem, status);
    }

    private PhysicalMachine(CPU cpu, Long memoryInBytes, Boolean hasGpu, Long ssdInBytes, Long hdInBytes, OperationalSystemEnum operationalSystem, PhysicalMachineStatusEnum status) {
        super(memoryInBytes, hasGpu, ssdInBytes, hdInBytes, operationalSystem);

        if (cpu == null)
            throw new IllegalArgumentException("Não é possível criar uma máquina física sem cpu");

        this.cpu = cpu;
        this.status = status;
    }

    @Override
    public String toString() {
        String allocations = "\n\nAllocations: \n";
        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            allocations += allocation.virtualMachine.toString();
            allocations += "\n";
        }

        return "PhysicalMachine id=" + id + "\n{" +
                "\n\t memoryInBytes=" + memoryInBytes +
                "\n\t cpu=" + cpu +
                "\n\t hasGpu=" + hasGpu +
                "\n\t ssdInBytes=" + ssdInBytes +
                "\n\t hdInBytes=" + hdInBytes +
                "\n\t operationalSystem=" + operationalSystem +
                "\n\t status=" + status +
                "\n}" +
                "\nRemainMemory=" + this.GetRemainMemoryInBytes() +
                "\t RemainSSD="+ this.GetRemainSsdInBytes() +
                "\t RemainHD="+ this.GetRemainHdInBytes() +
                allocations;
    }

    public VirtualPhysicalMachineAllocation Allocate(VirtualMachine machine) throws PreconditionFailException {

        if (this.status == PhysicalMachineStatusEnum.Full)
            throw new PreconditionFailException("A máquina física solicidada já está cheia, status: " + this.status);

        if (this.cpu.getArchitecture() != machine.getArchitecture())
            throw new PreconditionFailException("Arquitetura de máquina virtual diferente de arquitetura de máquina fisica soliticada, máquina física: " + this.cpu.getArchitecture() + " máquina virtual: " + machine.getArchitecture());

        if (this.cpu.getCores() < machine.getvCores())
            throw new PreconditionFailException("Quantidade de vCores da máquina virtual excede a quantidade de cores disponíveis na máquina física solicitada, máquina física: " + this.cpu.getCores() + " máquina virtual: " + machine.getvCores());

        if (this.getHasGpu() != machine.getHasGpu())
            throw new PreconditionFailException("Gpu solicitado para máquina virtual não está presente na máquina física solicitada, máquina física: " + this.getHasGpu() + " máquina virtual: " + machine.getHasGpu());

        if (this.getOperationalSystem() != machine.getOperationalSystem())
            throw new PreconditionFailException("Sistema operacional da máquina virtual diverge do sistema da máquina física solicitada, máquina física: " + this.getOperationalSystem() + " máquina virtual: " + machine.getOperationalSystem());

        long remainMemory = this.GetRemainMemoryInBytes();
        if (remainMemory < machine.getMemoryInBytes()) {
            throw new PreconditionFailException("Não é possível alocar esta máquina virtual na máquina física solicitada, memória remanescente: " + remainMemory + " Bytes memória solicitada: " + machine.getMemoryInBytes() + " Bytes.");
        }

        remainMemory = this.GetRemainSsdInBytes();
        if (remainMemory < machine.getSsdInBytes()) {
            throw new PreconditionFailException("Não é possível alocar esta máquina virtual na máquina física solicitada, ssd remanescente: " + remainMemory + " Bytes ssd solicitado: " + machine.getSsdInBytes() + " Bytes.");
        }

        remainMemory = this.GetRemainHdInBytes();
        if (remainMemory < machine.getHdInBytes()) {
            throw new PreconditionFailException("Não é possível alocar esta máquina virtual na máquina física solicitada, hd remanescente: " + remainMemory + " Bytes hd solicitado: " + machine.getHdInBytes() + " Bytes.");
        }

        VirtualPhysicalMachineAllocation newAllocation = new VirtualPhysicalMachineAllocation(machine, this, 1l);
        this.allocations.add(newAllocation);
        return newAllocation;
    }

    public long GetRemainMemoryInBytes() {
        long remainMemory = this.memoryInBytes;
        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            remainMemory -= allocation.virtualMachine.getMemoryInBytes();
        }

        return remainMemory;
    }

    public long GetRemainSsdInBytes() {
        long remainMemory = this.ssdInBytes;
        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            remainMemory -= allocation.virtualMachine.getSsdInBytes();
        }

        return remainMemory;
    }

    public long GetRemainHdInBytes() {
        long remainMemory = this.hdInBytes;
        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            remainMemory -= allocation.virtualMachine.getHdInBytes();
        }

        return remainMemory;
    }
}
