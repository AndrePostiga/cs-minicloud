import appServices.CpuAppService;
import appServices.PhysicalMachineAppService;
import corejava.Console;
import domain.MachineAggregate.Entities.CPU;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import javassist.NotFoundException;

import java.util.List;

public class MenuDeCpus {

    public static void ExibeMenuDeCPUs(CpuAppService cpuAppService) {
        int escolha = -1;

        do {

            try {
                if (escolha == 1) {
                    MenuDeCpus.CriarUmCpu(cpuAppService);
                } else if (escolha == 2) {
                    MenuDeCpus.AtualizarUmCpu(cpuAppService);
                } else if (escolha == 3) {
                    MenuDeCpus.DeletarUmCpu(cpuAppService);
                } else if (escolha == 4) {
                    MenuDeCpus.ExibeTodosOsCpus(cpuAppService);
                } else if (escolha == 5) {
                    MenuDeCpus.ExibeUmCpu(cpuAppService);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }

            System.out.println("O que deseja fazer?: ");
            System.out.println("1. Cadastrar um CPU");
            System.out.println("2. Alterar um CPU");
            System.out.println("3. Remover um CPU");
            System.out.println("4. Listar todos os CPUs");
            System.out.println("5. Buscar um CPU");
            System.out.println("0 - Voltar \n ");
            escolha = Console.readInt('\n' + "Digite uma opção:");
        } while (escolha != 0);
    }

    private static void AtualizarUmCpu(CpuAppService cpuAppService) {
        int resposta = Console.readInt("Digite o identificador do CPU a ser editado:");
        CPU cpuParaAlterar = cpuAppService.GetById(new Long(resposta));
        if (cpuParaAlterar == null) {
            System.out.println('\n' + "CPU com identificador " + resposta + " não foi encontrado!");
            return;
        }

        System.out.println("CPU que será alterado:");
        System.out.println(cpuParaAlterar);

        System.out.println('\n' + "O que você deseja alterar?");
        System.out.println('\n' + "1. Arquitetura");
        System.out.println('\n' + "2. Core");
        System.out.println('\n' + "3. Cache");
        System.out.println('\n' + "4. ClockFrequency");

        int opcaoAlteracao = Console.readInt('\n' + "Digite um número de 1 a 4:");
        CPU cpuAlterado = null;
        if (opcaoAlteracao == 1) {
            String arquiteturaString = Console.readLine("Informe a nova arquitetura do CPU (ARM, X86, X86_64): ");
            ArchitectureEnum arquitetura = ArchitectureEnum.valueOf(arquiteturaString);
            cpuAlterado = cpuAppService.UpdateArquitetura(cpuParaAlterar, arquitetura);
        } else if (opcaoAlteracao == 2) {
            int cores = Console.readInt("Digite a nova quantidade de cores:");
            cpuAlterado = cpuAppService.UpdateCores(cpuParaAlterar, cores);
        } else if (opcaoAlteracao == 3) {
            int cache = Console.readInt("Digite a nova quantidade de memória cache em bytes:");
            cpuAlterado = cpuAppService.UpdateCache(cpuParaAlterar, cache);
        } else if (opcaoAlteracao == 4) {
            int clock = Console.readInt("Digite a nova quantidade de clock em bytes:");
            cpuAlterado = cpuAppService.UpdateClock(cpuParaAlterar, clock);
        }

        if (cpuAlterado != null) {
            System.out.println("Alteração efetuada com sucesso!");
            System.out.println(cpuAlterado);
        }
    }

    private static void ExibeUmCpu(CpuAppService cpuAppService) {
        System.out.println();
        int id = Console.readInt("Digite o identificador do CPU a ser procurado:");
        CPU cpu = cpuAppService.GetById((long) id);
        System.out.println(cpu);
        System.out.println();
    }

    private static void ExibeTodosOsCpus(CpuAppService cpuAppService) {
        System.out.println();
        List<CPU> cpus = cpuAppService.GetCpus();
        cpus.forEach(System.out::println);
        System.out.println();
    }

    private static void CriarUmCpu(CpuAppService cpuAppService) {
        System.out.println("\n");

        String arquiteturaString = Console.readLine("Informe a arquitetura do CPU (ARM, X86, X86_64): ");
        ArchitectureEnum arquitetura = ArchitectureEnum.valueOf(arquiteturaString);
        Integer cores = Console.readInt("Informe o valor do número de cores: ");
        Integer cache = Console.readInt("Informe o valor do cache em bytes: ");
        Double clockFrequency = Console.readDouble("Informe o valor do clock: ");

        CPU cpuToCreate = cpuAppService.CreateCpu(arquitetura, cores, cache, clockFrequency);
        System.out.println('\n' + "CPU número " + cpuToCreate.getId() + " incluído com sucesso!");
        System.out.println('\n' + "Informações do CPU : \n" + cpuToCreate);
        System.out.println();
    }

    private static void DeletarUmCpu(CpuAppService cpuAppService) throws NotFoundException {
        int id = Console.readInt("Digite o número do cpu que você deseja remover: ");
        CPU cpuParaRemover = cpuAppService.DeleteCpu(new Long(id));
        System.out.println(cpuParaRemover);
        System.out.println();
    }
}
