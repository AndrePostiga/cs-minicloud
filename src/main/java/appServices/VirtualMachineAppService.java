package appServices;

import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import domain.MachineAggregate.Entities.VirtualMachine;
import exceptions.PreconditionFailException;
import javassist.NotFoundException;

import java.util.List;

public interface VirtualMachineAppService {
    VirtualMachine GetVirtualMachinesById(long id);

    List<VirtualMachine> GetVirtualMachines();

    VirtualMachine CreateVirtualMachine(int vCores, ArchitectureEnum arquitetura, long memoryInBytes, boolean hasGpu, long ssdInBytes, long hdInBytes, OperationalSystemEnum sistemaOperational, VirtualMachineStatusEnum status, long physicalMachineId) throws NotFoundException, PreconditionFailException;

    VirtualMachine DeleteVirtualMachine(long identificador) throws NotFoundException;

    VirtualMachine UpdateVCores(VirtualMachine maquina, int vCores) throws PreconditionFailException;

    VirtualMachine UpdateMemory(VirtualMachine maquina, int memoria) throws PreconditionFailException;

    VirtualMachine UpdateSSD(VirtualMachine maquina, int ssd) throws PreconditionFailException;

    VirtualMachine UpdateHd(VirtualMachine maquina, int hd) throws PreconditionFailException;
}
