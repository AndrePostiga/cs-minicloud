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
            throw new IllegalArgumentException("N�o � poss�vel criar uma m�quina f�sica sem cpu");

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
            throw new PreconditionFailException("A m�quina f�sica solicidada j� est� cheia, status: " + this.status);

        if (this.cpu.getArchitecture() != machine.getArchitecture())
            throw new PreconditionFailException("Arquitetura de m�quina virtual diferente de arquitetura de m�quina fisica soliticada, m�quina f�sica: " + this.cpu.getArchitecture() + " m�quina virtual: " + machine.getArchitecture());

        if (this.cpu.getCores() < machine.getvCores())
            throw new PreconditionFailException("Quantidade de vCores da m�quina virtual excede a quantidade de cores dispon�veis na m�quina f�sica solicitada, m�quina f�sica: " + this.cpu.getCores() + " m�quina virtual: " + machine.getvCores());

        if (this.getHasGpu() != machine.getHasGpu())
            throw new PreconditionFailException("Gpu solicitado para m�quina virtual n�o est� presente na m�quina f�sica solicitada, m�quina f�sica: " + this.getHasGpu() + " m�quina virtual: " + machine.getHasGpu());

        if (this.getOperationalSystem() != machine.getOperationalSystem())
            throw new PreconditionFailException("Sistema operacional da m�quina virtual diverge do sistema da m�quina f�sica solicitada, m�quina f�sica: " + this.getOperationalSystem() + " m�quina virtual: " + machine.getOperationalSystem());

        long remainMemory = this.GetRemainMemoryInBytes();
        if (remainMemory < machine.getMemoryInBytes()) {
            throw new PreconditionFailException("N�o � poss�vel alocar esta m�quina virtual na m�quina f�sica solicitada, mem�ria remanescente: " + remainMemory + " Bytes mem�ria solicitada: " + machine.getMemoryInBytes() + " Bytes.");
        }

        remainMemory = this.GetRemainSsdInBytes();
        if (remainMemory < machine.getSsdInBytes()) {
            throw new PreconditionFailException("N�o � poss�vel alocar esta m�quina virtual na m�quina f�sica solicitada, ssd remanescente: " + remainMemory + " Bytes ssd solicitado: " + machine.getSsdInBytes() + " Bytes.");
        }

        remainMemory = this.GetRemainHdInBytes();
        if (remainMemory < machine.getHdInBytes()) {
            throw new PreconditionFailException("N�o � poss�vel alocar esta m�quina virtual na m�quina f�sica solicitada, hd remanescente: " + remainMemory + " Bytes hd solicitado: " + machine.getHdInBytes() + " Bytes.");
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
