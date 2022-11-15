import appServices.PhysicalMachineAppService;
import corejava.Console;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
import exceptions.PreconditionFailException;
import javassist.NotFoundException;

import java.util.List;

public class MenuDeMaquinasFisicas {
    public static void ExibeMenuDeMaquinasFisicas(PhysicalMachineAppService physicalMachineAppService) {
        int escolha = -1;

        do {

            try {
                if (escolha == 1) {
                    MenuDeMaquinasFisicas.ExibeTodasAsMaquinasFisicas(physicalMachineAppService);
                } else if (escolha == 2) {
                    MenuDeMaquinasFisicas.ExibeUmaMaquinaFisica(physicalMachineAppService);
                } else if (escolha == 3) {
                    MenuDeMaquinasFisicas.CriarUmaMaquinaFisica(physicalMachineAppService);
                } else if (escolha == 4) {
                    MenuDeMaquinasFisicas.ExibeTodasAsMaquinasFisicasComAlocacoes(physicalMachineAppService);
                } else if (escolha == 5) {
                    MenuDeMaquinasFisicas.ExibeUmaMaquinaFisicaComAlocacoes(physicalMachineAppService);
                } else if (escolha == 6) {
                    MenuDeMaquinasFisicas.EditaUmaMaquinaFisica(physicalMachineAppService);
                } else if (escolha == 7) {
                    MenuDeMaquinasFisicas.RemoveMaquinaFisica(physicalMachineAppService);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }

            System.out.println("O que deseja fazer?: ");
            System.out.println("1 - Listar todas as m�quinas f�sicas");
            System.out.println("2 - Listar uma m�quina f�sica");
            System.out.println("3 - Criar uma m�quina f�sica");
            System.out.println("4 - Listar M�quinas F�sicas com aloca��es");
            System.out.println("5 - Listar UMA M�quina F�sica e suas aloca��es");
            System.out.println("6 - Editar uma m�quina f�sica"); //A ser implementado quando tiver m�quinas virtuais alocadas
            System.out.println("7 - Deletar uma m�quina f�sica"); //A ser implmentado quando tiver m�quinas virtuais alocadas
            System.out.println("0 - Voltar \n ");
            escolha = Console.readInt('\n' + "Digite uma op��o:");
        } while (escolha != 0);
    }

    private static void RemoveMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) throws PreconditionFailException, NotFoundException {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina f�sica a ser deletada:");
        PhysicalMachine maquina = physicalMachineAppService.DeletePhysicalMachine((long) identificador);

        String physicalMachinePrint = MenuDeMaquinasFisicas.GetTableHeader("M�quina F�sica Deletada");
        physicalMachinePrint += maquina.PrintPhysicalMachine();
        physicalMachinePrint += GetTableLine();
        System.out.println(physicalMachinePrint);
    }

    private static void EditaUmaMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) {
    }

    private static void ExibeUmaMaquinaFisicaComAlocacoes(PhysicalMachineAppService physicalMachineAppService) {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina f�sica a ser procurada:");
        PhysicalMachine maquina = physicalMachineAppService.GetPhysicalMachinesById((long) identificador);

        String physicalMachinePrint = MenuDeMaquinasFisicas.GetTableHeader("M�quina F�sica");
        physicalMachinePrint += maquina.PrintPhysicalMachine();
        physicalMachinePrint += GetTableLine();
        System.out.println(physicalMachinePrint);
        System.out.println(maquina.PrintAllocations());
    }

    private static void ExibeTodasAsMaquinasFisicasComAlocacoes(PhysicalMachineAppService physicalMachineAppService) {
        List<PhysicalMachine> maquinasFisicas = physicalMachineAppService.GetPhysicalMachines();
        System.out.println("\n--- M�quinas F�sicas existentes ---\n");
        for (PhysicalMachine maquina : maquinasFisicas) {
            String physicalMachinePrint = MenuDeMaquinasFisicas.GetTableHeader("M�quina F�sica");
            physicalMachinePrint += maquina.PrintPhysicalMachine();
            physicalMachinePrint += GetTableLine();
            System.out.println(physicalMachinePrint);
            System.out.println(maquina.PrintAllocations());
        }
    }

    private static void CriarUmaMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) throws NotFoundException {
        System.out.println("\n");

        long cpuId = Console.readInt("Digite o id do CPU que a m�quina ir� utilizar:");
        long memoryInBytes = Console.readInt("Digite a quantidade de mem�ria ram em Bytes:");
        boolean hasGpu = Console.readLine("Sua m�quina f�sica ter� GPU? (s/n):") == "s";
        long ssdInBytes = Console.readInt("Digite a quantidade de mem�ria ssd em Bytes:");
        long hdInBytes = Console.readInt("Digite a quantidade de mem�ria hd em Bytes:");
        String sistemaOperationalString = Console.readLine("Digite o SO da m�quina (Windows, WindowsServer, MacOs, Ubuntu, CentOs): ");
        OperationalSystemEnum sistemaOperational = OperationalSystemEnum.valueOf(sistemaOperationalString);

        PhysicalMachine maquinaCriada;
        maquinaCriada = physicalMachineAppService.CreatePhysicalMachine(cpuId, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, sistemaOperational);

        String physicalMachinePrint = MenuDeMaquinasFisicas.GetTableHeader("M�quina F�sica Criada");
        physicalMachinePrint += maquinaCriada.PrintPhysicalMachine();
        physicalMachinePrint += GetTableLine();
        System.out.println(physicalMachinePrint);
    }

    private static void ExibeUmaMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina f�sica a ser procurada:");
        PhysicalMachine maquina = physicalMachineAppService.GetPhysicalMachinesById((long) identificador);

        String physicalMachinePrint = MenuDeMaquinasFisicas.GetTableHeader("M�quina F�sica");
        physicalMachinePrint += maquina.PrintPhysicalMachine();
        physicalMachinePrint += GetTableLine();
        System.out.println(physicalMachinePrint);
    }

    private static void ExibeTodasAsMaquinasFisicas(PhysicalMachineAppService physicalMachineAppService) {
        String physicalMachinePrint = MenuDeMaquinasFisicas.GetTableHeader("M�quinas F�sicas");

        List<PhysicalMachine> maquinasFisicas = physicalMachineAppService.GetPhysicalMachines();
        for (PhysicalMachine maquina : maquinasFisicas) {
            physicalMachinePrint += maquina.PrintPhysicalMachine();
        }

        physicalMachinePrint += GetTableLine();
        System.out.println(physicalMachinePrint);
    }

    private static String GetTableHeader(String title) {
        String leftAlignFormat = "| %-5d | %-6s | %-6d | %-11s | %-11s | %-11s | %-11s | %-11s | %-11s | %-11s | %-11s |%n";
        String header = "";
        header += GetTableLine();
        header += String.format("| %-135s |%n", title);
        header += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+%n");
        header += String.format("| Id    | Status | CPU_Id | Arquitetura | Mem�ria     | SSD         | HD          | OS          | Remain Mem. | Remain SSD  | Remain Hd   |%n");
        header += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+%n");
        return header;
    }

    private static String GetTableLine() {
        return String.format("+-----------------------------------------------------------------------------------------------------------------------------------------+%n");
    }
}
