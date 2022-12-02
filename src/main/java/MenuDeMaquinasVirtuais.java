import appServices.VirtualMachineAppService;
import corejava.Console;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.Enumerations.OperationalSystemEnum;
import domain.MachineAggregate.Entities.Enumerations.VirtualMachineStatusEnum;
import domain.MachineAggregate.Entities.VirtualMachine;
import exceptions.PreconditionFailException;
import javassist.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.NoResultException;
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
                } else if (escolha == 0) {
                    return;
                }
            }
            catch (NoResultException | EmptyResultDataAccessException ex) {
                System.out.println("N�o foi poss�vel encontrar o recurso solicitado, tente novamente.");
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println(ex);
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

    private static void RemoverUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws NotFoundException {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina virtual a ser deletada:");
        VirtualMachine maquina = virtualMachineAppService.DeleteVirtualMachine((long) identificador);

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("M�quina Virtual Removida");
        machinePrint += maquina.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);
    }

    private static void EditarUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws PreconditionFailException {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina virtual a ser editada:");
        VirtualMachine maquina = virtualMachineAppService.GetVirtualMachinesById((long) identificador);
        if (maquina == null) {
            System.out.println('\n' + "M�quina virtual com identificador " + identificador + " n�o foi encontrada!");
            return;
        }

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("M�quina Virtual para Editar");
        machinePrint += maquina.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);

        System.out.println("O que voc� deseja alterar?");
        System.out.println("1. vCores");
        System.out.println("2. Mem�ria");
        System.out.println("3. SSD");
        System.out.println("4. HD");

        VirtualMachine maquinaEditada = null;
        int opcaoAlteracao = Console.readInt("Digite um n�mero de 1 a 4:");
        if (opcaoAlteracao == 1) {
            int vCores = Console.readInt("Digite a nova quantidade de vCores:");
            maquinaEditada = virtualMachineAppService.UpdateVCores(maquina, vCores);
        } else if (opcaoAlteracao == 2) {
            int memoria = Console.readInt("Digite a nova quantidade de mem�ria:");
            maquinaEditada = virtualMachineAppService.UpdateMemory(maquina, memoria);
        } else if (opcaoAlteracao == 3) {
            int ssd = Console.readInt("Digite a nova quantidade de SSD:");
            maquinaEditada = virtualMachineAppService.UpdateSSD(maquina, ssd);
        } else if (opcaoAlteracao == 4) {
            int hd = Console.readInt("Digite a nova quantidade de HD:");
            maquinaEditada = virtualMachineAppService.UpdateHd(maquina, hd);
        }

        if (maquinaEditada != null) {
            machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("M�quina Virtual Editada");
            machinePrint += maquinaEditada.Print();
            machinePrint += GetTableLine();
            System.out.println(machinePrint);
        }
    }

    private static void CriarUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws PreconditionFailException, NotFoundException {
        System.out.println("\n");

        int vCores = Console.readInt("Digite a quantidade de vCpus que a m�quina ir� utilizar:");
        String arquiteturaString = Console.readLine("Informe a arquitetura do vCore (ARM, X86, X86_64): ");
        ArchitectureEnum arquitetura = ArchitectureEnum.valueOf(arquiteturaString);
        long memoryInBytes = Console.readInt("Digite a quantidade de mem�ria ram em Bytes:");
        long ssdInBytes = Console.readInt("Digite a quantidade de mem�ria ssd em Bytes:");
        long hdInBytes = Console.readInt("Digite a quantidade de mem�ria hd em Bytes:");
        String sistemaOperationalString = Console.readLine("Digite o SO da m�quina (Windows, WindowsServer, MacOs, Ubuntu, CentOs): ");
        OperationalSystemEnum sistemaOperational = OperationalSystemEnum.valueOf(sistemaOperationalString);
        String statusString = Console.readLine("Informe o status inicial da m�quina (On, Off, Iddle): ");
        VirtualMachineStatusEnum status = VirtualMachineStatusEnum.valueOf(statusString);
        long maquinaFisicaId = Console.readInt("Digite o id da m�quina f�sica que a m�quina virtual ser� alocada:");

        VirtualMachine maquinaCriada;
        maquinaCriada = virtualMachineAppService.CreateVirtualMachine(
                vCores, arquitetura, memoryInBytes, ssdInBytes, hdInBytes, sistemaOperational, status, maquinaFisicaId
        );

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("M�quina Virtual Criada");
        machinePrint += maquinaCriada.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);
    }

    private static void ExibeUmaMaquinaVirtual(VirtualMachineAppService virtualMachineAppService) throws NotFoundException {
        int identificador = Console.readInt('\n' + "Digite o identificador da m�quina virtual a ser procurada:");
        VirtualMachine maquina = virtualMachineAppService.GetVirtualMachinesById((long) identificador);
        if (maquina == null)
            throw new NotFoundException("N�o foi encontrada uma m�quina virtual com identificador: " + identificador);

        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("M�quina Virtual");
        machinePrint += maquina.Print();
        machinePrint += GetTableLine();
        System.out.println(machinePrint);
    }

    private static void ExibeTodasAsMaquinasVirtuais(VirtualMachineAppService virtualMachineAppService) {
        String machinePrint = MenuDeMaquinasVirtuais.GetTableHeader("M�quinas Virtuals");

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
        header += String.format("| Id    | Status | vCpus  | Arquitetura | Mem�ria     | SSD         | HD          | OS          | Physical Machine Id |%n");
        header += String.format("+-------+--------+--------+-------------+-------------+-------------+-------------+-----------------------------------+%n");
        return header;
    }

    private static String GetTableLine() {
        return String.format("+---------------------------------------------------------------------------------------------------------------------+%n");
    }
}
