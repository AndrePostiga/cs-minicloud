package infrastructure.database.dao;

import annotation.RecuperaLista;
import domain.MachineAggregate.Entities.VirtualMachine;

import java.util.List;

public interface VirtualMachineDao extends GenericDao<VirtualMachine, Long>{
    @RecuperaLista
    List<VirtualMachine> GetAll();

    void RemoveAllocation(VirtualMachine virtualMachine);
}