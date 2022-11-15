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
}
