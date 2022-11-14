package infrastructure.database.dao;

import annotation.RecuperaLista;
import domain.MachineAggregate.Entities.CPU;

import java.util.List;

public interface CPUDao extends GenericDao<CPU, Long>{
    @RecuperaLista
    List<CPU> GetAll();
}
