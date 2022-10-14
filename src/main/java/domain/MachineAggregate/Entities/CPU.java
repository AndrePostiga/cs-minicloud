package domain.MachineAggregate.Entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "cpus")
@DynamicInsert
@DynamicUpdate
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
    @Column(nullable = false)
    private ArchitectureEnum architecture;

    @Column(nullable = false)
    private Integer cores;

    @Column(nullable = false)
    private Integer cache;

    @Column(nullable = false)
    private double clockFrequency;

    @Version
    private int version;

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

    public int getVersion() {
        return version;
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

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "id=" + id +
                ", architecture=" + architecture +
                ", cores=" + cores +
                ", cache=" + cache +
                ", clockFrequency=" + clockFrequency +
                ", version=" + version +
                '}';
    }
}
