package domain.MachineAggregate.Entities;

import domain.MachineAggregate.Entities.Enumerations.PhysicalMachineStatusEnum;

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

    @OneToMany(mappedBy = "physicalMachine")
    private List<VirtualPhysicalMachineAllocation> allocations;

    @Enumerated(EnumType.STRING)
    private PhysicalMachineStatusEnum status;

    private Long remainMemoryInBytes;

    private Long remainSsdInBytes;

    private Long remainHdInBytes;

    private int remainCpuCores;
}
