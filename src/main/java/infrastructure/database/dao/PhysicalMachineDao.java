package infrastructure.database.dao;

import annotation.RecuperaLista;
import annotation.RecuperaObjeto;
import domain.MachineAggregate.Entities.PhysicalMachine;

import java.util.List;

public interface PhysicalMachineDao {
    @RecuperaLista
    List<PhysicalMachine> GetAll();

    @RecuperaObjeto
    PhysicalMachine GetById(long id);
}
