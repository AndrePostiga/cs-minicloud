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
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }

            System.out.println("O que deseja fazer?: ");
            System.out.println("1 - Listar todas as m�quinas virtuais");
            System.out.println("2 - Listar uma m�quina virtual");
            System.out.println("3 - Criar uma m�quina virtual");
            System.out.println("4 - Editar uma m�quina virtual");
            System.out.println("5 - Deletar uma m�quina virtual");
            System.out.println("0 - Voltar \n ");
            escolha = Console.readInt('\n' + "Digite uma op��o:");
        }
        while (escolha != 0);
    }

    private static void CriarUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws PreconditionFailException, NotFoundException {
        System.out.println("\n");

        int vCores = Console.readInt("Digite a quantidade de vCpus que a m�quina ir� utilizar:");
        String arquiteturaString = Console.readLine('\n' + "Informe a arquitetura do vCore (ARM, X86, X86_64): ");
        ArchitectureEnum arquitetura = ArchitectureEnum.valueOf(arquiteturaString);
        long memoryInBytes = Console.readInt("Digite a quantidade de mem�ria ram em Bytes:");
        boolean hasGpu = Console.readLine("Sua m�quina f�sica ter� GPU? (s/n):") == "s";
        long ssdInBytes = Console.readInt("Digite a quantidade de mem�ria ssd em Bytes:");
        long hdInBytes = Console.readInt("Digite a quantidade de mem�ria hd em Bytes:");
        String sistemaOperationalString = Console.readLine("Digite o SO da m�quina (Windows, WindowsServer, MacOs, Ubuntu, CentOs): ");
        OperationalSystemEnum sistemaOperational = OperationalSystemEnum.valueOf(sistemaOperationalString);
        String statusString = Console.readLine('\n' + "Informe o status inicial da m�quina (On, Off, Iddle): ");
        VirtualMachineStatusEnum status = VirtualMachineStatusEnum.valueOf(statusString);
        long maquinaFisicaId = Console.readInt("Digite o id da m�quina f�sica que a m�quina virtual ser� alocada:");

        VirtualMachine maquinaCriada;
        maquinaCriada = virtualMachineAppService.CreateVirtualMachine(
                vCores, arquitetura, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, sistemaOperational, status, maquinaFisicaId
        );

        System.out.println("\n--- M�quina F�sica Criada ---\n");
        System.out.println(maquinaCriada);
        System.out.println("\n--- ---\n");
    }

    private static void ExibeUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina virtual a ser procurada:");
        VirtualMachine maquina = virtualMachineAppService.GetVirtualMachinesById((long) identificador);
        System.out.println("\n--- M�quina Virtual ---\n");
        System.out.println(maquina);
        System.out.println("\n--- ---\n");
    }

    private static void ExibeTodasAsMaquinasVirtuais(VirtualMachineAppService virtualMachineAppService) {
        List<VirtualMachine> maquinasFisicas = virtualMachineAppService.GetVirtualMachines();
        System.out.println("\n--- M�quinas Virtuais existentes ---\n");
        for (VirtualMachine maquinas : maquinasFisicas) {
            System.out.println(maquinas);
        }
        System.out.println("\n--- ---\n");
    }
}
