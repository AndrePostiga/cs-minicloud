package infrastructure.database.dao.impl;

import domain.MachineAggregate.Entities.VirtualMachine;
import infrastructure.database.dao.VirtualMachineDao;

public abstract class VirtualMachineDaoImpl extends JPAGenericDao<VirtualMachine, Long> implements VirtualMachineDao {
    public VirtualMachineDaoImpl() {
        super(VirtualMachine.class);
    }
}