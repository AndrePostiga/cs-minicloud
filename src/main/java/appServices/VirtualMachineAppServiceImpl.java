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
    @Transactional
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

    @Override
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine DeleteVirtualMachine(long identificador) throws NotFoundException {
        VirtualMachine machineToDelete = virtualMachineDao.GetByIdWithLock(identificador);
        if (machineToDelete == null) {
            throw new NotFoundException("Não foi possível encontrar a máquina de identificador: " + identificador);
        }

        virtualMachineDao.Delete(machineToDelete);
        return machineToDelete;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateVCores(VirtualMachine maquina, int vCores) {
        return null;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateMemory(VirtualMachine maquina, int memoria) {
        return null;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateSSD(VirtualMachine maquina, int ssd) {
        return null;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateHd(VirtualMachine maquina, int hd) {
        return null;
    }
}
