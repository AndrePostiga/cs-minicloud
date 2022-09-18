package domain.MachineAggregate.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "physical_machine")
public class PhysicalMachine extends Machine{

    public PhysicalMachine() {}

    @OneToMany(mappedBy = "physicalMachine")
    private List<VirtualPhysicalMachineAllocation> allocations;

    @Enumerated(EnumType.STRING)
    private PhysicalMachineStatusEnum status;

    @Column(name = "remain_memory_in_bytes")
    private Long remainMemoryInBytes;

    @Column(name = "remain_ssd_in_bytes")
    private Long remainSsdInBytes;

    @Column(name = "remain_hd_in_bytes")
    private Long remainHdInBytes;

    @Column(name = "remain_cpu_cores_in_bytes")
    private int remainCpuCores;
}
