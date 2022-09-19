package domain.MachineAggregate.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "VirtualPhysicalMachineAllocation")
public class VirtualPhysicalMachineAllocation {

    public VirtualPhysicalMachineAllocation() {}

    public VirtualPhysicalMachineAllocation(VirtualMachine virtualMachine, PhysicalMachine physicalMachine, Long userId) {
        this.virtualMachine = virtualMachine;
        this.physicalMachine = physicalMachine;
        this.userId = userId;

        createdDate = LocalDateTime.now(ZoneOffset.UTC);
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @OneToOne
    public VirtualMachine virtualMachine;

    @ManyToOne
    public PhysicalMachine physicalMachine;

    public Long userId;

    public LocalDateTime startedDate;

    public LocalDateTime createdDate;

    public LocalDateTime updatedAt;

    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
    }

    public PhysicalMachine getPhysicalMachine() {
        return physicalMachine;
    }

    public void setPhysicalMachine(PhysicalMachine physicalMachine) {
        this.physicalMachine = physicalMachine;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
