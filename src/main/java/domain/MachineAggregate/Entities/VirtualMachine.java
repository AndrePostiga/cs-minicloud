package domain.MachineAggregate.Entities;

import javax.persistence.*;

@Entity
@Table(name = "virtual_machine")
public class VirtualMachine extends Machine{

    public VirtualMachine() {}

    @OneToOne(mappedBy = "virtualMachine")
    private VirtualPhysicalMachineAllocation allocation;

    @Enumerated(EnumType.STRING)
    private VirtualMachineStatusEnum status;

}
