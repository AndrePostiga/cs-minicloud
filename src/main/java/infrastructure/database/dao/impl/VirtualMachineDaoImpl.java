package infrastructure.database.dao.impl;

import domain.MachineAggregate.Entities.VirtualMachine;
import infrastructure.database.dao.VirtualMachineDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;


@Repository
public abstract class VirtualMachineDaoImpl extends JPAGenericDao<VirtualMachine, Long> implements VirtualMachineDao {
    public VirtualMachineDaoImpl() {
        super(VirtualMachine.class);
    }

    public final void RemoveAllocation(VirtualMachine virtualMachine) {
        Query q = em.createNativeQuery("DELETE FROM VirtualPhysicalMachineAllocations WHERE virtualMachine_id=?1");
        q.setParameter(1, virtualMachine.getId());
        q.executeUpdate();

        em.remove(virtualMachine);
    }
}