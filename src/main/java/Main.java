import appServices.CpuAppService;
import appServices.PhysicalMachineAppService;
import appServices.VirtualMachineAppService;
import corejava.Console;
import domain.ProfileAggregate.Enumerations.ProfilesEnum;
import helpers.SingletonPerfis;
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

        Main.EscolheUmPerfil();

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

    private static void EscolheUmPerfil() {
        int escolha;
        System.out.println("Escolha um perfil de acesso: ");
        System.out.println("1 - Usuário");
        System.out.println("2 - Administrador");
        System.out.println("3 - Administrador e Usuário");
        System.out.println();
        escolha = Console.readInt("Digite uma opção:");

        if (escolha != 1 && escolha != 2 && escolha != 3)
            Main.EscolheUmPerfil();

        SingletonPerfis singletonPerfis = SingletonPerfis.getSingletonPerfis();
        if (escolha == 1){
            singletonPerfis.setPerfis(new String[] {String.valueOf(ProfilesEnum.User)});
        }
        else if (escolha == 2){
            singletonPerfis.setPerfis(new String[] {String.valueOf(ProfilesEnum.Administrator)});
        }
        else if (escolha == 3){
            singletonPerfis.setPerfis(new String[] {String.valueOf(ProfilesEnum.User), String.valueOf(ProfilesEnum.Administrator)});
        }
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
