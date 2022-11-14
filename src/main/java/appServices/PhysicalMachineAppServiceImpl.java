package appServices;

import domain.MachineAggregate.Entities.PhysicalMachine;
import infrastructure.database.dao.PhysicalMachineDao;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.NoResultException;
import java.util.List;

public class PhysicalMachineAppServiceImpl implements PhysicalMachineAppService{

    @Autowired
    private PhysicalMachineDao physicalMachineDao;

    @Override
    public PhysicalMachine CreatePhysicalMachine(PhysicalMachine physicalMachine) {
        return null;
    }

    @Override
    public PhysicalMachine UpdatePhysicalMachine(PhysicalMachine physicalMachine) {
        return null;
    }

    @Override
    public List<PhysicalMachine> GetPhysicalMachines() {
        return physicalMachineDao.GetAll();
    }

    @Override
    public PhysicalMachine GetPhysicalMachinesById(Long id) {
        return physicalMachineDao.GetById(id);
    }
}
