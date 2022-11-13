package appServices;

import domain.MachineAggregate.Entities.CPU;

import java.util.List;

public interface CpuAppService {
    CPU Include(CPU cpu);

    CPU Update(Long id, CPU cpu);

    void Delete(Long id);

    CPU GetById(Long id);

    List<CPU> GetCpus();
}
