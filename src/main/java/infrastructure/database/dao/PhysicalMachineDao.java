package infrastructure.database.dao;

import annotation.RecuperaLista;
import domain.MachineAggregate.Entities.PhysicalMachine;

import java.util.List;

public interface PhysicalMachineDao extends GenericDao<PhysicalMachine, Long>{
    @RecuperaLista
    List<PhysicalMachine> GetAll();
}
