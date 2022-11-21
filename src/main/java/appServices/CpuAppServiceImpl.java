package appServices;

import domain.MachineAggregate.Entities.CPU;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import infrastructure.database.dao.CPUDao;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CpuAppServiceImpl implements CpuAppService {

    @Autowired
    private CPUDao cpuDAO;

    @Override
    public CPU GetById(Long id) {
        return cpuDAO.GetById(id);
    }

    @Override
    public List<CPU> GetCpus() {
        return cpuDAO.GetAll();
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU DeleteCpu(Long id) throws NotFoundException {
        CPU cpuToDelete = cpuDAO.GetByIdWithLock(id);
        if (cpuToDelete == null) {
            throw new NotFoundException("Não foi possível encontrar o cpu de identificador: " + id);
        }

        cpuDAO.Delete(cpuToDelete);
        return cpuToDelete;
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU CreateCpu(ArchitectureEnum architecture, Integer cores, Integer cache, Double clockFrequency) {
        CPU cpu = new CPU(architecture, cores, cache, clockFrequency);
        cpuDAO.Create(cpu);
        return cpu;
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU UpdateArquitetura(CPU cpuParaAlterar, ArchitectureEnum architecture) {
        cpuParaAlterar.setArchitecture(architecture);
        cpuDAO.Update(cpuParaAlterar);
        return cpuParaAlterar;
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU UpdateCores(CPU cpuParaAlterar, int cores) {
        cpuParaAlterar.setCores(cores);
        cpuDAO.Update(cpuParaAlterar);
        return cpuParaAlterar;
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU UpdateCache(CPU cpuParaAlterar, int cache) {
        cpuParaAlterar.setCache(cache);
        cpuDAO.Update(cpuParaAlterar);
        return cpuParaAlterar;
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU UpdateClock(CPU cpuParaAlterar, int clock) {
        cpuParaAlterar.setClockFrequency(clock);
        cpuDAO.Update(cpuParaAlterar);
        return cpuParaAlterar;
    }
}
