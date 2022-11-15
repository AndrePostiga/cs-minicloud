package appServices;

import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import exceptions.PreconditionFailException;
import javassist.NotFoundException;

import java.util.List;

public interface PhysicalMachineAppService {
    List<PhysicalMachine> GetPhysicalMachines();
    PhysicalMachine GetPhysicalMachinesById(Long id);
    PhysicalMachine CreatePhysicalMachine(long cpuId, long memoryInBytes, boolean hasGpu, long ssdInBytes, long hdInBytes, OperationalSystemEnum sistemaOperational) throws NotFoundException;

    PhysicalMachine DeletePhysicalMachine(long identificador) throws NotFoundException, PreconditionFailException;

    PhysicalMachine UpdateCpu(PhysicalMachine maquina, int cpuId) throws NotFoundException, PreconditionFailException;

    PhysicalMachine UpdateMemory(PhysicalMachine maquina, long memoria) throws PreconditionFailException;

    PhysicalMachine UpdateSSD(PhysicalMachine maquina, long ssd) throws PreconditionFailException;

    PhysicalMachine UpdateHd(PhysicalMachine maquina, long hd) throws PreconditionFailException;

    PhysicalMachine UpdateOperationalSystem(PhysicalMachine maquina, OperationalSystemEnum sistemaOperational) throws PreconditionFailException;
}
