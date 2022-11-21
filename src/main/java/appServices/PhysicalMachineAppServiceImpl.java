package appServices;

import domain.MachineAggregate.Entities.CPU;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import exceptions.PreconditionFailException;
import infrastructure.database.dao.PhysicalMachineDao;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PhysicalMachineAppServiceImpl implements PhysicalMachineAppService {

    @Autowired
    private PhysicalMachineDao physicalMachineDao;

    @Autowired
    private CpuAppService cpuAppService;

    @Override
    public List<PhysicalMachine> GetPhysicalMachines() {
        return physicalMachineDao.GetAll();
    }

    @Override
    public PhysicalMachine GetPhysicalMachinesById(Long id) {
        return physicalMachineDao.GetById(id);
    }

    @Override
    @Transactional
    public PhysicalMachine CreatePhysicalMachine(long cpuId, long memoryInBytes, long ssdInBytes, long hdInBytes, OperationalSystemEnum sistemaOperational) throws NotFoundException {
        CPU cpu = cpuAppService.GetById(cpuId);
        if (cpu == null) {
            throw new NotFoundException("Não foi possível encontrar o cpu de identificador: " + cpuId);
        }

        PhysicalMachine maquinaFisica = PhysicalMachine.CreatePhysicalMachine(cpu, memoryInBytes, ssdInBytes, hdInBytes, sistemaOperational);
        PhysicalMachine maquinaFisicaCriada = physicalMachineDao.Create(maquinaFisica);
        return maquinaFisicaCriada;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine DeletePhysicalMachine(long identificador) throws NotFoundException, PreconditionFailException {
        PhysicalMachine physicalMachineToDelete = physicalMachineDao.GetByIdWithLock(identificador);
        if (physicalMachineToDelete == null) {
            throw new NotFoundException("Não foi possível encontrar a máquina de identificador: " + identificador);
        }

        if (physicalMachineToDelete.getAllocations().stream().count() > 0) {
            throw new PreconditionFailException("Não é possível remover uma máquina física que possui VM's allocadas, por favor, exclua as Vm's antes de remover");
        }

        physicalMachineDao.Delete(physicalMachineToDelete);
        return physicalMachineToDelete;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine UpdateCpu(PhysicalMachine maquina, int cpuId) throws NotFoundException, PreconditionFailException {
        CPU cpu = cpuAppService.GetById(new Long(cpuId));
        if (cpu == null) {
            throw new NotFoundException("Não foi possível encontrar o cpu de identificador: " + cpuId);
        }

        maquina.UpdateCpu(cpu);
        physicalMachineDao.Update(maquina);
        return this.GetPhysicalMachinesById(maquina.getId());
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine UpdateMemory(PhysicalMachine maquina, long memoria) throws PreconditionFailException {
        maquina.UpdateMemory(memoria);
        physicalMachineDao.Update(maquina);
        return this.GetPhysicalMachinesById(maquina.getId());
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine UpdateSSD(PhysicalMachine maquina, long ssd) throws PreconditionFailException {
        maquina.UpdateSSD(ssd);
        physicalMachineDao.Update(maquina);
        return this.GetPhysicalMachinesById(maquina.getId());
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine UpdateHd(PhysicalMachine maquina, long hd) throws PreconditionFailException {
        maquina.UpdateHD(hd);
        physicalMachineDao.Update(maquina);
        return this.GetPhysicalMachinesById(maquina.getId());
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine UpdateOperationalSystem(PhysicalMachine maquina, OperationalSystemEnum sistemaOperational) throws PreconditionFailException {
        maquina.UpdateOperationalSystem(sistemaOperational);
        physicalMachineDao.Update(maquina);
        return this.GetPhysicalMachinesById(maquina.getId());
    }
}
