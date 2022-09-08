package domain.MachineAggregate.Entities;

public abstract class Machine {
    protected Long id;
    protected Long memoryInBytes;
    protected CPU cpu;
    protected Boolean hasGpu;
    protected Long ssdInBytes;
    protected Long hdInBytes;
    protected OperationalSystemEnum operationalSystem;
}
