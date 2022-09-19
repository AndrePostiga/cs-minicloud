package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.PhysicalMachineStatusEnum;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PhysicalMachines")
public class PhysicalMachine extends Machine{

    public PhysicalMachine() {}

    @OneToMany(mappedBy = "physicalMachine")
    private List<VirtualPhysicalMachineAllocation> allocations;

    @Enumerated(EnumType.STRING)
    private PhysicalMachineStatusEnum status;

    private Long remainMemoryInBytes;

    private Long remainSsdInBytes;

    private Long remainHdInBytes;

    private int remainCpuCores;
}
