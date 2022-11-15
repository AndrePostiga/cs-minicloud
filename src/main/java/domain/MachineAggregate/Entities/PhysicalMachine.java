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
            throw new IllegalArgumentException("Não é possível criar uma máquina física sem cpu");

        this.cpu = cpu;
        this.status = status;
    }

    public String PrintAllocations() {
        String leftAlignFormat = "| %-5d | %-6s | %-6d | %-11s | %-11s | %-11s | %-11s | %-11s |%n";
        String allocations = "Alocações: \n";

        allocations += String.format("+-----------------------------------------------------------------------------------------------+%n");
        allocations += String.format("| %-93s |%n", "Máquinas Virtuais Alocadas");
        allocations += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-------------+%n");
        allocations += String.format("| Vm ID | Status | vCores | Arquitetura |   Memória   |     SSD     |      HD     |     OS      |%n");
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
                throw new PreconditionFailException("Não é possível atualizar a máquina física para um CPU com arquitetura diferente devido suas alocações");

            if (cpu.getCores() < allocation.virtualMachine.getvCores())
                throw new PreconditionFailException("Não é possível atualizar a máquina física para um CPU com menos cores devido suas alocações");
        }

        this.cpu = cpu;
    }

    public void UpdateMemory(long newMemoryInBytes) throws PreconditionFailException {
        long usedMemory = this.memoryInBytes - this.GetRemainMemoryInBytes();
        if (usedMemory > newMemoryInBytes)
            throw new PreconditionFailException("Não é possível atualizar a memória da máquina física pois a nova quantidade de memória é menor do que a quantidade de memória utilizada por sua alocações");

        this.memoryInBytes = newMemoryInBytes;
    }

    public void UpdateSSD(long newSsdInBytes) throws PreconditionFailException {
        long usedMemory = this.ssdInBytes - this.GetRemainSsdInBytes();
        if (usedMemory > newSsdInBytes)
            throw new PreconditionFailException("Não é possível atualizar o ssd da máquina física pois a nova quantidade de ssd é menor do que a quantidade de ssd utilizada por sua alocações");

        this.ssdInBytes = newSsdInBytes;
    }

    public void UpdateHD(long newHdInBytes) throws PreconditionFailException {
        long usedMemory = this.hdInBytes - this.GetRemainHdInBytes();
        if (usedMemory > newHdInBytes)
            throw new PreconditionFailException("Não é possível atualizar o hd da máquina física pois a nova quantidade de hd é menor do que a quantidade de hd utilizada por sua alocações");

        this.hdInBytes = newHdInBytes;
    }

    public void UpdateOperationalSystem(OperationalSystemEnum newOperationalSystem) throws PreconditionFailException {
        if (this.allocations == null || this.allocations.stream().count() == 0) {
            this.operationalSystem = newOperationalSystem;
            return;
        }

        for (VirtualPhysicalMachineAllocation allocation : this.allocations) {
            if (newOperationalSystem != allocation.virtualMachine.getOperationalSystem())
                throw new PreconditionFailException("Não é possível atualizar a máquina física para um sistema operacional diferente devido suas alocações");
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
