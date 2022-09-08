package domain.MachineAggregate.Entities;

import java.util.List;

public class PhysicalMachine extends Machine{
    private List<VirtualPhysicalMachineAllocation> allocations;
    private PhysicalMachineStatusEnum status;
    private Long remainMemoryInBytes;
    private Long remainSsdInBytes;
    private Long remainHdInBytes;
    private int remainCpuCores;
}
