package domain.MachineAggregate.Entities;

import javax.persistence.*;

@Entity
@Table(name = "cpus")
public class CPU {

    public CPU() {}

    public CPU(ArchitectureEnum architecture, Integer cores, Integer cache, double clockFrequency) {
        this.architecture = architecture;
        this.cores = cores;
        this.cache = cache;
        this.clockFrequency = clockFrequency;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ArchitectureEnum architecture;

    private Integer cores;

    private Integer cache;

    private double clockFrequency;

    public Long getId() {
        return id;
    }
    public ArchitectureEnum getArchitecture() {
        return architecture;
    }
    public Integer getCores() {
        return cores;
    }
    public Integer getCache() {
        return cache;
    }
    public double getClockFrequency() {
        return clockFrequency;
    }

    public void setArchitecture(ArchitectureEnum architecture) {
        this.architecture = architecture;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

    public void setClockFrequency(double clockFrequency) {
        this.clockFrequency = clockFrequency;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "id=" + id +
                ", architecture=" + architecture +
                ", cores=" + cores +
                ", cache=" + cache +
                ", clockFrequency=" + clockFrequency +
                '}';
    }
}
