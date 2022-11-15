package infrastructure.database.dao.impl;

import domain.MachineAggregate.Entities.VirtualMachine;
import infrastructure.database.dao.VirtualMachineDao;
import org.springframework.stereotype.Repository;

@Repository
public abstract class VirtualMachineDaoImpl extends JPAGenericDao<VirtualMachine, Long> implements VirtualMachineDao {
    public VirtualMachineDaoImpl() {
        super(VirtualMachine.class);
    }
}