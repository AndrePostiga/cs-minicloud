package infrastructure.database.dao;

import annotation.RecuperaLista;
import annotation.RecuperaObjeto;
import domain.MachineAggregate.Entities.PhysicalMachine;

import java.util.List;

public interface PhysicalMachineDao extends GenericDao<PhysicalMachine, Long>{
    @RecuperaLista
    List<PhysicalMachine> GetAll();

    @RecuperaObjeto
    PhysicalMachine GetByIdFetch(Long Id);
}
