package appServices;

import corejava.Console;
import domain.MachineAggregate.Entities.CPU;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import infrastructure.database.dao.PhysicalMachineDao;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

public class PhysicalMachineAppServiceImpl implements PhysicalMachineAppService{

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
    @Transactional(rollbackFor={Exception.class})
    public PhysicalMachine CreatePhysicalMachine(long cpuId, long memoryInBytes, boolean hasGpu, long ssdInBytes, long hdInBytes, OperationalSystemEnum sistemaOperational) throws NotFoundException {
        CPU cpu = cpuAppService.GetById(cpuId);
        if (cpu == null) {
            throw new NotFoundException("Não foi possível encontrar o cpu de identificador: " + cpuId);
        }

        PhysicalMachine maquinaFisica = PhysicalMachine.CreatePhysicalMachine(cpu, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, sistemaOperational);
        return physicalMachineDao.Create(maquinaFisica);
    }
}
