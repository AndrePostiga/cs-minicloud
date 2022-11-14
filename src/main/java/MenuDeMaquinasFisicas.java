import appServices.PhysicalMachineAppService;
import corejava.Console;
import domain.MachineAggregate.Entities.PhysicalMachine;
import javassist.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.NoResultException;
import java.util.List;

public class MenuDeMaquinasFisicas {
    public static void ExibeMenuDeMaquinasFisicas(PhysicalMachineAppService physicalMachineAppService) {
        int escolha = -1;

        do {

            if (escolha == 1) {
                MenuDeMaquinasFisicas.ExibeTodasAsMaquinasFisicas(physicalMachineAppService);
            }
            else if (escolha == 2) {
                MenuDeMaquinasFisicas.ExibeUmaMaquinaFisica(physicalMachineAppService);
            }

            System.out.println("O que deseja fazer?: ");
            System.out.println("1 - Listar todas as máquinas físicas");
            System.out.println("2 - Listar uma máquina física");
            System.out.println("3 - Criar uma máquina física");
            System.out.println("4 - Editar uma máquina física");
            System.out.println("5 - Deletar uma máquina física");
            System.out.println("6 - Listar todas as máquinas físicas que eu possuem VM");
            System.out.println("0 - Voltar \n ");
            escolha = Console.readInt('\n' + "Digite uma opção:");
        }
        while (escolha != 0);
    }

    private static void ExibeUmaMaquinaFisica(PhysicalMachineAppService physicalMachineAppService) {
        int identificador = Console.readInt('\n' + "Digite o identificador da máquina física a ser procurada:");
        PhysicalMachine maquina;

        try {
            maquina = physicalMachineAppService.GetPhysicalMachinesById((long) identificador);
        }
        catch (EmptyResultDataAccessException ex){
            System.out.println("Objeto não encontrado para o identificador " + identificador);
            return;
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }

        System.out.println("\n--- Máquina Física ---\n");
        System.out.println(maquina);
        System.out.println("\n--- ---\n");
    }

    private static void ExibeTodasAsMaquinasFisicas(PhysicalMachineAppService physicalMachineAppService) {
        List<PhysicalMachine> maquinasFisicas = physicalMachineAppService.GetPhysicalMachines();

        System.out.println("\n--- Máquinas Físicas existentes ---\n");
        for (PhysicalMachine maquinas : maquinasFisicas) {
            System.out.println(maquinas);
        }
        System.out.println("\n--- ---\n");
    }
}
