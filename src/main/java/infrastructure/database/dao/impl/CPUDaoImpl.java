package infrastructure.database.dao.impl;

import domain.MachineAggregate.Entities.CPU;
import infrastructure.database.dao.CPUDao;
import org.springframework.stereotype.Repository;

@Repository
public abstract class CPUDaoImpl extends JPAGenericDao<CPU, Long> implements CPUDao {

    public CPUDaoImpl() {
        super(CPU.class);
    }

}
