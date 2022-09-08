package domain.MachineAggregate.Daos;

import domain.MachineAggregate.Entities.CPU;
import infrastructure.database.dao.AbstractJPADao;
import infrastructure.database.dao.GenericDao;

public class CpuDAO extends AbstractJPADao<CPU> implements GenericDao<CPU> {
    public CpuDAO() {
        super(CPU.class);
    }
}
