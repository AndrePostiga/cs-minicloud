import appServices.PhysicalMachineAppService;
import corejava.Console;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.PhysicalMachine;
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
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return;
            }

            System.out.println("O que deseja fazer?: ");
            System.out.println("1 - Listar todas as m�quinas f�sicas");
            System.out.println("2 - Listar uma m�quina f�sica");
            System.out.println("3 - Criar uma m�quina f�sica");
            System.out.println("4 - Editar uma m�quina f�sica"); //A ser implementado quando tiver m�quinas virtuais alocadas
            System.out.println("5 - Deletar uma m�quina f�sica"); //A ser implmentado quando tiver m�quinas virtuais alocadas
            System.out.println("6 - Listar todas as m�quinas f�sicas que eu possuem VM"); // a ser implementado quando tiver m�quinas virtuais alocadas
            System.out.println("0 - Voltar \n ");
            escolha = Console.readInt('\n' + "Digite uma op��o:");
        } while (escolha != 0);
    }

    private static void CriarUmaMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) {
        System.out.println("\n");

        long cpuId = Console.readInt("Digite o id do CPU que a m�quina ir� utilizar:");
        long memoryInBytes = Console.readInt("Digite a quantidade de mem�ria ram em Bytes:");
        boolean hasGpu = Console.readLine("Sua m�quina f�sica ter� GPU? (s/n):") == "s";
        long ssdInBytes = Console.readInt("Digite a quantidade de mem�ria ssd em Bytes:");
        long hdInBytes = Console.readInt("Digite a quantidade de mem�ria hd em Bytes:");
        String sistemaOperationalString = Console.readLine("Digite o SO da m�quina (Windows, WindowsServer, MacOs, Ubuntu, CentOs): ");
        OperationalSystemEnum sistemaOperational = OperationalSystemEnum.valueOf(sistemaOperationalString);

        PhysicalMachine maquinaCriada;
        try {
            maquinaCriada = physicalMachineAppService.CreatePhysicalMachine(cpuId, memoryInBytes, hasGpu, ssdInBytes, hdInBytes, sistemaOperational);
        } catch (NotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("\n--- M�quina F�sica Criada ---\n");
        System.out.println(maquinaCriada);
        System.out.println("\n--- ---\n");
    }

    private static void ExibeUmaMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina f�sica a ser procurada:");
        PhysicalMachine maquina = physicalMachineAppService.GetPhysicalMachinesById((long) identificador);
        System.out.println("\n--- M�quina F�sica ---\n");
        System.out.println(maquina);
        System.out.println("\n--- ---\n");
    }

    private static void ExibeTodasAsMaquinasFisicas(PhysicalMachineAppService physicalMachineAppService) {
        List<PhysicalMachine> maquinasFisicas = physicalMachineAppService.GetPhysicalMachines();
        System.out.println("\n--- M�quinas F�sicas existentes ---\n");
        for (PhysicalMachine maquinas : maquinasFisicas) {
            System.out.println(maquinas);
        }
        System.out.println("\n--- ---\n");
    }
}
