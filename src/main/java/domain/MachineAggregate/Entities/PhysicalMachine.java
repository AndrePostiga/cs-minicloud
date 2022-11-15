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
public class PhysicalMachine extends Machine {

    public PhysicalMachine() {
    }

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

    public String PrintAllocations() {
        String leftAlignFormat = "| %-5d | %-6s | %-6d | %-11s | %-11s | %-11s | %-11s | %-11s |%n";
        String allocations = "Aloca��es: \n";

        allocations += String.format("+-----------------------------------------------------------------------------------------------+%n");
        allocations += String.format("| %-93s |%n", "M�quinas Virtuais Alocadas");
        allocations += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-------------+%n");
        allocations += String.format("| Vm ID | Status | vCores | Arquitetura |   Mem�ria   |     SSD     |      HD     |     OS      |%n");
        allocations += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-------------+%n");

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            allocations += String.format(leftAlignFormat,
                    allocation.virtualMachine.getId(),
                    allocation.virtualMachine.getStatus(),
                    allocation.virtualMachine.getvCores(),
                    allocation.virtualMachine.getArchitecture(),
                    allocation.virtualMachine.getMemoryInBytes(),
                    allocation.virtualMachine.getSsdInBytes(),
                    allocation.virtualMachine.getHdInBytes(),
                    allocation.virtualMachine.getOperationalSystem());
        }

        allocations += String.format("+-----------------------------------------------------------------------------------------------+%n");

        return allocations;
    }

    public String PrintPhysicalMachine() {
        String leftAlignFormat = "| %-5d | %-6s | %-6d | %-11s | %-11s | %-11s | %-11s | %-11s | %-11s | %-11s | %-11s |%n";
        String physicalMachine = String.format(leftAlignFormat,
                this.getId(),
                this.getStatus(),
                this.getCpu().getId(),
                this.getCpu().getArchitecture(),
                this.getMemoryInBytes(),
                this.getSsdInBytes(),
                this.getHdInBytes(),
                this.getOperationalSystem(),
                this.GetRemainMemoryInBytes(),
                this.GetRemainSsdInBytes(),
                this.GetRemainHdInBytes());

        return physicalMachine;
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

        if (this.allocations == null)
            return remainMemory;

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            remainMemory -= allocation.virtualMachine.getMemoryInBytes();
        }

        return remainMemory;
    }

    public long GetRemainSsdInBytes() {
        long remainMemory = this.ssdInBytes;

        if (this.allocations == null)
            return remainMemory;

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            remainMemory -= allocation.virtualMachine.getSsdInBytes();
        }

        return remainMemory;
    }

    public long GetRemainHdInBytes() {
        long remainMemory = this.hdInBytes;

        if (this.allocations == null)
            return remainMemory;

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            remainMemory -= allocation.virtualMachine.getHdInBytes();
        }

        return remainMemory;
    }

    public CPU getCpu() {
        return cpu;
    }

    public PhysicalMachineStatusEnum getStatus() {
        return status;
    }

    public List<VirtualPhysicalMachineAllocation> getAllocations() {
        return allocations;
    }

    public void UpdateCpu(CPU cpu) throws PreconditionFailException {
        if (this.allocations == null || this.allocations.stream().count() == 0) {
            this.cpu = cpu;
            return;
        }

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            if (cpu.getArchitecture() != allocation.virtualMachine.getArchitecture())
                throw new PreconditionFailException("N�o � poss�vel atualizar a m�quina f�sica para um CPU com arquitetura diferente devido suas aloca��es");

            if (cpu.getCores() < allocation.virtualMachine.getvCores())
                throw new PreconditionFailException("N�o � poss�vel atualizar a m�quina f�sica para um CPU com menos cores devido suas aloca��es");
        }

        this.cpu = cpu;
    }

    public void UpdateMemory(long newMemoryInBytes) throws PreconditionFailException {
        long usedMemory = this.memoryInBytes - this.GetRemainMemoryInBytes();
        if (usedMemory > newMemoryInBytes)
            throw new PreconditionFailException("N�o � poss�vel atualizar a mem�ria da m�quina f�sica pois a nova quantidade de mem�ria � menor do que a quantidade de mem�ria utilizada por sua aloca��es");

        this.memoryInBytes = newMemoryInBytes;
    }

    public void UpdateSSD(long newSsdInBytes) throws PreconditionFailException {
        long usedMemory = this.ssdInBytes - this.GetRemainSsdInBytes();
        if (usedMemory > newSsdInBytes)
            throw new PreconditionFailException("N�o � poss�vel atualizar o ssd da m�quina f�sica pois a nova quantidade de ssd � menor do que a quantidade de ssd utilizada por sua aloca��es");

        this.ssdInBytes = newSsdInBytes;
    }

    public void UpdateHD(long newHdInBytes) throws PreconditionFailException {
        long usedMemory = this.hdInBytes - this.GetRemainHdInBytes();
        if (usedMemory > newHdInBytes)
            throw new PreconditionFailException("N�o � poss�vel atualizar o hd da m�quina f�sica pois a nova quantidade de hd � menor do que a quantidade de hd utilizada por sua aloca��es");

        this.hdInBytes = newHdInBytes;
    }

    public void UpdateOperationalSystem(OperationalSystemEnum newOperationalSystem) throws PreconditionFailException {
        if (this.allocations == null || this.allocations.stream().count() == 0) {
            this.operationalSystem = newOperationalSystem;
            return;
        }

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            if (newOperationalSystem != allocation.virtualMachine.getOperationalSystem())
                throw new PreconditionFailException("N�o � poss�vel atualizar a m�quina f�sica para um sistema operacional diferente devido suas aloca��es");
        }

        this.operationalSystem = newOperationalSystem;
    }

    @Override
    public String toString() {
        return "PhysicalMachine{" +
                "cpu=" + cpu +
                ", allocations=" + allocations +
                ", status=" + status +
                '}';
    }
}
