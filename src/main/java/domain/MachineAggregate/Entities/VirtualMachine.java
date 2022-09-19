package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;

import javax.persistence.*;

@Entity
@Table(name = "VirtualMachines")
public class VirtualMachine extends Machine{

    public VirtualMachine() {}

    @OneToOne(mappedBy = "virtualMachine")
    private VirtualPhysicalMachineAllocation allocation;

    @Enumerated(EnumType.STRING)
    private VirtualMachineStatusEnum status;

}
