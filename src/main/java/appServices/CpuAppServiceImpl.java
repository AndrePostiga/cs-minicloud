package appServices;

import domain.MachineAggregate.Entities.CPU;
import infrastructure.database.dao.CPUDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CpuAppServiceImpl implements CpuAppService {

    @Autowired
    private CPUDao cpuDAO;

    @Override
    @Transactional
    public CPU Include(CPU cpu) {
        return cpuDAO.Create(cpu);
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU Update(Long id, CPU cpu) {
        cpuDAO.Update(cpu);
        return cpuDAO.GetById(id);
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public void Delete(Long id) {
        CPU cpu = cpuDAO.GetById(id);
        cpuDAO.Delete(cpu);
    }

    @Override
    public CPU GetById(Long id) {
        return cpuDAO.GetById(id);
    }

    @Override
    public List<CPU> GetCpus() {
        return cpuDAO.GetAll();
    }
}
