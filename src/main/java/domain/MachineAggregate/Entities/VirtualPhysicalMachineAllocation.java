package domain.MachineAggregate.Entities;

import java.time.LocalDateTime;

public class VirtualPhysicalMachineAllocation {
    public VirtualMachine virtualMachine;
    public PhysicalMachine physicalMachine;
    public Long userId;
    public LocalDateTime startedDate;
    public LocalDateTime createdDate;
    public LocalDateTime updatedAt;
}
