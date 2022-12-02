package infrastructure.database.dao;

import annotation.RecuperaLista;
import annotation.RecuperaObjeto;
import domain.MachineAggregate.Entities.VirtualMachine;

import java.util.List;

public interface VirtualMachineDao extends GenericDao<VirtualMachine, Long>{
    @RecuperaLista
    List<VirtualMachine> GetAll();

    void RemoveAllocation(VirtualMachine virtualMachine);

    @RecuperaObjeto
    VirtualMachine GetByIdFetch(Long Id);
}