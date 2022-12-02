package appServices;

import annotation.Perfis;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import domain.MachineAggregate.Entities.VirtualMachine;
import domain.ProfileAggregate.Enumerations.ProfilesEnum;
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
    @Perfis(nomes = {ProfilesEnum.Administrator, ProfilesEnum.User})
    public VirtualMachine GetVirtualMachinesById(long id) {
        return virtualMachineDao.GetByIdFetch(id);
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator, ProfilesEnum.User})
    public List<VirtualMachine> GetVirtualMachines() {
        return virtualMachineDao.GetAll();
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional
    public VirtualMachine CreateVirtualMachine(int vCores, ArchitectureEnum arquitetura, long memoryInBytes, long ssdInBytes, long hdInBytes, OperationalSystemEnum sistemaOperational, VirtualMachineStatusEnum status, long physicalMachineId) throws NotFoundException, PreconditionFailException {

        PhysicalMachine physicalMachine = physicalMachineAppService.GetPhysicalMachinesById(physicalMachineId);
        if (physicalMachine == null) {
            throw new NotFoundException("Não foi possível encontrar a máquina virtual de identificador: " + physicalMachineId);
        }

        try {
            VirtualMachine virtualMachine = VirtualMachine.CreateVirtualMachine(vCores, arquitetura, memoryInBytes, ssdInBytes, hdInBytes, sistemaOperational, status, physicalMachine);
            return virtualMachineDao.Create(virtualMachine);
        }
        catch (PreconditionFailException ex) {
            throw ex;
        }

    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine DeleteVirtualMachine(long identificador) throws NotFoundException {
        VirtualMachine machineToDelete = virtualMachineDao.GetById(identificador);
        if (machineToDelete == null) {
            throw new NotFoundException("Não foi possível encontrar a máquina de identificador: " + identificador);
        }

        virtualMachineDao.RemoveAllocation(machineToDelete);
        return machineToDelete;
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateVCores(VirtualMachine maquina, int vCores) throws PreconditionFailException {
        maquina.UpdateVCores(vCores);
        virtualMachineDao.Update(maquina);
        return this.GetVirtualMachinesById(maquina.getId());
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateMemory(VirtualMachine maquina, int memory) throws PreconditionFailException {
        maquina.UpdateMemory(memory);
        virtualMachineDao.Update(maquina);
        return this.GetVirtualMachinesById(maquina.getId());
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateSSD(VirtualMachine maquina, int ssd) throws PreconditionFailException {
        maquina.UpdateSsd(ssd);
        virtualMachineDao.Update(maquina);
        return this.GetVirtualMachinesById(maquina.getId());
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional(rollbackFor={Exception.class})
    public VirtualMachine UpdateHd(VirtualMachine maquina, int hd) throws PreconditionFailException {
        maquina.UpdateHd(hd);
        virtualMachineDao.Update(maquina);
        return this.GetVirtualMachinesById(maquina.getId());
    }
}
