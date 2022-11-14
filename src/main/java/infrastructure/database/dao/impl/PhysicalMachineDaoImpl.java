package infrastructure.database.dao.impl;

import domain.MachineAggregate.Entities.PhysicalMachine;
import infrastructure.database.dao.PhysicalMachineDao;
import org.springframework.stereotype.Repository;

@Repository
public abstract class PhysicalMachineDaoImpl extends JPAGenericDao<PhysicalMachine, Long> implements PhysicalMachineDao {
    public PhysicalMachineDaoImpl() {
        super(PhysicalMachine.class);
    }
}
