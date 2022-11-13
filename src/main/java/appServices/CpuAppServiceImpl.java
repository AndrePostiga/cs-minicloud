package appServices;

import infrastructure.database.dao.CpuDAO;
import domain.MachineAggregate.Entities.CPU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CpuAppServiceImpl implements CpuAppService {

    @Autowired
    private CpuDAO cpuDAO;

    @Override
    @Transactional
    public CPU Include(CPU cpu) {
        return cpuDAO.create(cpu);
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public CPU Update(Long id, CPU cpu) {
        return cpuDAO.update(id, cpu);
    }

    @Override
    @Transactional (rollbackFor={Exception.class})
    public void Delete(Long id) {
        cpuDAO.delete(id);
    }

    @Override
    public CPU GetById(Long id) {
        return cpuDAO.getById(id);
    }

    @Override
    public List<CPU> GetCpus() {
        return cpuDAO.getAll();
    }
}
