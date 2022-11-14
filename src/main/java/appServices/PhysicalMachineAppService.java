package appServices;

import domain.MachineAggregate.Entities.PhysicalMachine;
import javassist.NotFoundException;

import java.util.List;

public interface PhysicalMachineAppService {
    PhysicalMachine CreatePhysicalMachine(PhysicalMachine physicalMachine);
    PhysicalMachine UpdatePhysicalMachine(PhysicalMachine physicalMachine);
    List<PhysicalMachine> GetPhysicalMachines();
    PhysicalMachine GetPhysicalMachinesById(Long id);
}
