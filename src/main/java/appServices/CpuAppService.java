package appServices;

import domain.MachineAggregate.Entities.CPU;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import javassist.NotFoundException;

import java.util.List;

public interface CpuAppService {
    CPU Include(CPU cpu);

    CPU Update(Long id, CPU cpu);

    void Delete(Long id);

    CPU GetById(Long id);

    List<CPU> GetCpus();

    CPU DeleteCpu(Long id) throws NotFoundException;

    CPU CreateCpu(ArchitectureEnum architecture, Integer cores, Integer cache, Double clockFrequency);

    CPU UpdateArquitetura(CPU cpuParaAlterar, ArchitectureEnum arquitetura);

    CPU UpdateCores(CPU cpuParaAlterar, int cores);

    CPU UpdateCache(CPU cpuParaAlterar, int cache);

    CPU UpdateClock(CPU cpuParaAlterar, int clock);
}
