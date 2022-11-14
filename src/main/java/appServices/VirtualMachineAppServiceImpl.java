package appServices;

import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import domain.MachineAggregate.Entities.VirtualMachine;
import exceptions.PreconditionFailException;
import infrastructure.database.dao.VirtualMachineDao;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class VirtualMachineAppServiceImpl implements VirtualMachineAppService {

    @Autowired
    private VirtualMachineDao virtualMachineDao;

    @Autowired
    private PhysicalMachineAppService physicalMachineAppService;

    @Override
    public VirtualMachine GetVirtualMachinesById(long id) {
        return virtualMachineDao.GetById(id);
    }

    @Override
    public List<VirtualMachine> GetVirtualMachines() {
        return virtualMachineDao.GetAll();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public VirtualMachine CreateVirtualMachine(int vCores, ArchitectureEnum arquitetura, long memoryInBytes, boolean hasGpu, long ssdInBytes, long hdInBytes, OperationalSystemEnum sistemaOperational, VirtualMachineStatusEnum status, long physicalMachineId) throws NotFoundException, PreconditionFailException {

        PhysicalMachine physicalMachine = physicalMachineAppService.GetPhysicalMachinesById(physicalMachineId);
        if (physicalMachine == null) {
            throw new NotFoundException("Não foi possível encontrar a máquina virtual de identificador: " + physicalMachineId);
        }

        try {
            VirtualMachine virtualMachine = VirtualMachine.CreateVirtualMachine(vCores, arquitetura, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, sistemaOperational, status, physicalMachine);
            return virtualMachineDao.Create(virtualMachine);
        }
        catch (PreconditionFailException ex) {
            throw ex;
        }

    }
}
