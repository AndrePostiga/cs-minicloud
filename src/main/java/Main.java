import appServices.CpuAppService;
import appServices.PhysicalMachineAppService;
import appServices.VirtualMachineAppService;
import corejava.Console;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args){
        System.out.println("Bem vindo ao Mini Cloud");

        //Instancia classes de serviço
        ApplicationContext factory = new ClassPathXmlApplicationContext("beans-jpa.xml");
        CpuAppService cpuAppService = (CpuAppService)factory.getBean("cpuAppService");
        PhysicalMachineAppService physicalMachineAppService = (PhysicalMachineAppService)factory.getBean("physicalMachineAppService");
        VirtualMachineAppService virtualMachineAppService = (VirtualMachineAppService)factory.getBean("virtualMachineAppService");

        int escolha = -1;
        do {

            if (escolha == 1){
                MenuDeCpus.ExibeMenuDeCPUs(cpuAppService);
            }
            else if (escolha == 2){
                MenuDeMaquinasFisicas.ExibeMenuDeMaquinasFisicas(physicalMachineAppService);
            }
            else if (escolha == 3){
                MenuDeMaquinasVirtuais.ExibeMenuDeMaquinasVirtuais(virtualMachineAppService);
            }

            escolha = exibeMenuERetornaEscolha();
        }
        while (escolha != 0);
    }

    private static int exibeMenuERetornaEscolha() {
        int escolha;
        System.out.println("Menu: ");
        System.out.println("1 - Menu de CPUS");
        System.out.println("2 - Menu de Máquinas Físicas");
        System.out.println("3 - Menu de Máquinas Virtuais");
        System.out.println("0 - Sair \n ");
        escolha = Console.readInt('\n' + "Digite uma opção:");
        return escolha;
    }
}
