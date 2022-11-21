package appServices;

import annotation.Perfis;
import domain.MachineAggregate.Entities.CPU;
import domain.ProfileAggregate.Enumerations.ProfilesEnum;
import infrastructure.database.dao.CPUDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CpuAppServiceImpl implements CpuAppService {

    @Autowired
    private CPUDao cpuDAO;

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional
    public CPU Include(CPU cpu) {
        CPU createCpu = cpuDAO.Create(cpu);
        return createCpu;
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional (rollbackFor={Exception.class})
    public CPU Update(Long id, CPU cpu) {
        cpuDAO.Update(cpu);
        return cpuDAO.GetById(id);
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator})
    @Transactional (rollbackFor={Exception.class})
    public void Delete(Long id) {
        CPU cpu = cpuDAO.GetById(id);
        cpuDAO.Delete(cpu);
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator, ProfilesEnum.User})
    public CPU GetById(Long id) {
        return cpuDAO.GetById(id);
    }

    @Override
    @Perfis(nomes = {ProfilesEnum.Administrator, ProfilesEnum.User})
    public List<CPU> GetCpus() {
        return cpuDAO.GetAll();
    }
}
