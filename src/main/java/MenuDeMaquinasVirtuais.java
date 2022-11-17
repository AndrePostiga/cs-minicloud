import appServices.VirtualMachineAppService;
import corejava.Console;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import domain.MachineAggregate.Entities.VirtualMachine;
import exceptions.PreconditionFailException;
import javassist.NotFoundException;

import java.util.List;

public class MenuDeMaquinasVirtuais {

    public static void ExibeMenuDeMaquinasVirtuais(VirtualMachineAppService virtualMachineAppService) {
        int escolha = -1;

        do {

            try {
                if (escolha == 1) {
                    MenuDeMaquinasVirtuais.ExibeTodasAsMaquinasVirtuais(virtualMachineAppService);
                } else if (escolha == 2) {
                    MenuDeMaquinasVirtuais.ExibeUmaMaquinaVirtual(virtualMachineAppService);
                } else if (escolha == 3) {
                    MenuDeMaquinasVirtuais.CriarUmaMaquinaVirtual(virtualMachineAppService);
                } else if (escolha == 4) {
                    MenuDeMaquinasVirtuais.EditarUmaMaquinaVirtual(virtualMachineAppService);
                } else if (escolha == 5) {
                    MenuDeMaquinasVirtuais.RemoverUmaMaquinaVirtual(virtualMachineAppService);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println(ex);
                return;
            }

            System.out.println("O que deseja fazer?: ");
            System.out.println("1 - Listar todas as máquinas virtuais");
            System.out.println("2 - Listar uma máquina virtual");
            System.out.println("3 - Criar uma máquina virtual");
            System.out.println("4 - Editar uma máquina virtual");
            System.out.println("5 - Deletar uma máquina virtual");
            System.out.println("0 - Voltar \n ");
            escolha = Console.readInt('\n' + "Digite uma opção:");
        }
        while (escolha != 0);
    }

    private static void RemoverUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws NotFoundException {
        int identificador = Console.readInt('\n' + "Digite o identificador da máquina virtual a ser deletada:");
        VirtualMachine maquina = virtualMachineAppService.DeleteVirtualMachine((long) identificador);
        System.out.println(maquina);
    }

    private static void EditarUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws PreconditionFailException {
        int identificador = Console.readInt('\n' + "Digite o identificador da máquina virtual a ser editada:");
        VirtualMachine maquina = virtualMachineAppService.GetVirtualMachinesById((long) identificador);
        if (maquina == null) {
            System.out.println('\n' + "Máquina virtual com identificador " + identificador + " não foi encontrada!");
            return;
        }

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("Máquina Virtual para Editar");
        machinePrint += maquina.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);

        System.out.println("O que você deseja alterar?");
        System.out.println("1. vCores");
        System.out.println("2. Memória");
        System.out.println("3. SSD");
        System.out.println("4. HD");

        VirtualMachine maquinaEditada = null;
        int opcaoAlteracao = Console.readInt("Digite um número de 1 a 4:");
        if (opcaoAlteracao == 1) {
            int vCores = Console.readInt("Digite a nova quantidade de vCores:");
            maquinaEditada = virtualMachineAppService.UpdateVCores(maquina, vCores);
        } else if (opcaoAlteracao == 2) {
            int memoria = Console.readInt("Digite a nova quantidade de memória:");
            maquinaEditada = virtualMachineAppService.UpdateMemory(maquina, memoria);
        } else if (opcaoAlteracao == 3) {
            int ssd = Console.readInt("Digite a nova quantidade de SSD:");
            maquinaEditada = virtualMachineAppService.UpdateSSD(maquina, ssd);
        } else if (opcaoAlteracao == 4) {
            int hd = Console.readInt("Digite a nova quantidade de HD:");
            maquinaEditada = virtualMachineAppService.UpdateHd(maquina, hd);
        }

        if (maquinaEditada != null) {
            machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("Máquina Virtual Editada");
            machinePrint += maquinaEditada.Print();
            machinePrint += GetTableLine();
            System.out.println(machinePrint);
        }
    }

    private static void CriarUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws PreconditionFailException, NotFoundException {
        System.out.println("\n");

        int vCores = Console.readInt("Digite a quantidade de vCpus que a máquina irá utilizar:");
        String arquiteturaString = Console.readLine('\n' + "Informe a arquitetura do vCore (ARM, X86, X86_64): ");
        ArchitectureEnum arquitetura = ArchitectureEnum.valueOf(arquiteturaString);
        long memoryInBytes = Console.readInt("Digite a quantidade de memória ram em Bytes:");
        boolean hasGpu = Console.readLine("Sua máquina física terá GPU? (s/n):") == "s";
        long ssdInBytes = Console.readInt("Digite a quantidade de memória ssd em Bytes:");
        long hdInBytes = Console.readInt("Digite a quantidade de memória hd em Bytes:");
        String sistemaOperationalString = Console.readLine("Digite o SO da máquina (Windows, WindowsServer, MacOs, Ubuntu, CentOs): ");
        OperationalSystemEnum sistemaOperational = OperationalSystemEnum.valueOf(sistemaOperationalString);
        String statusString = Console.readLine('\n' + "Informe o status inicial da máquina (On, Off, Iddle): ");
        VirtualMachineStatusEnum status = VirtualMachineStatusEnum.valueOf(statusString);
        long maquinaFisicaId = Console.readInt("Digite o id da máquina física que a máquina virtual será alocada:");

        VirtualMachine maquinaCriada;
        maquinaCriada = virtualMachineAppService.CreateVirtualMachine(
                vCores, arquitetura, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, sistemaOperational, status, maquinaFisicaId
        );

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("Máquina Virtual Criada");
        machinePrint += maquinaCriada.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);
    }

    private static void ExibeUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) {
        int identificador = Console.readInt('\n' + "Digite o identificador da máquina virtual a ser procurada:");
        VirtualMachine maquina = virtualMachineAppService.GetVirtualMachinesById((long) identificador);

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("Máquina Virtual");
        machinePrint += maquina.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);
    }

    private static void ExibeTodasAsMaquinasVirtuais(VirtualMachineAppService virtualMachineAppService) {
        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("Máquinas Virtuals");

        List<VirtualMachine> maquinasVirtuais = virtualMachineAppService.GetVirtualMachines();
        for (VirtualMachine maquina : maquinasVirtuais) {
            machinePrint += maquina.Print();
        }
        machinePrint += GetTableLine();
        System.out.println(machinePrint);
    }

    private static String GetTableHeader(String title) {
        String leftAlignFormat = "| %-5d | %-6s | %-6d | %-11s | %-11s | %-11s | %-11s | %-11s | %-19d |%n";
        String header = "";
        header += GetTableLine();
        header += String.format("| %-115s |%n", title);
        header += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-------------+---------------------+%n");
        header += String.format("| Id    | Status | vCpus  | Arquitetura | Memória     | SSD         | HD          | OS          | Physical Machine Id |%n");
        header += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-----------------------------------+%n");
        return header;
    }

    private static String GetTableLine() {
        return String.format("+---------------------------------------------------------------------------------------------------------------------+%n");
    }
}
