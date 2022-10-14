import corejava.Console;
import domain.MachineAggregate.Entities.Enumerations.ArchitectureEnum;
import domain.MachineAggregate.Entities.CPU;
import domain.MachineAggregate.Daos.CpuDAO;
import infrastructure.database.DaoFactory;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        CpuDAO dao = DaoFactory.getDAO(CpuDAO.class);

        boolean continua = true;
        while (continua) {
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Cadastrar um CPU");
            System.out.println("2. Alterar um CPU");
            System.out.println("3. Remover um CPU");
            System.out.println("4. Listar todos os CPUs");
            System.out.println("5. Sair");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            switch (opcao)
            {
                case 1:
                {
                    String arquiteturaString = Console.readLine('\n' +"Informe a arquitetura do CPU (ARM, X86, X86_64): ");
                    ArchitectureEnum arquitetura = ArchitectureEnum.valueOf(arquiteturaString);

                    Integer cores = Console.readInt("Informe o valor do número de cores: ");
                    Integer cache = Console.readInt("Informe o valor do cache em bytes: ");
                    Double clockFrequency = Console.readDouble("Informe o valor do clock: ");

                    CPU cpu = new CPU(arquitetura, cores, cache, clockFrequency);
                    dao.create(cpu);
                    System.out.println('\n' + "CPU número " + cpu.getId() + " incluído com sucesso!");
                    System.out.println('\n' + "Informações do CPU : \n" + cpu);
                    break;
                }

                case 2:
                {
                    int resposta = Console.readInt('\n' + "Digite o número do CPU que você deseja alterar: ");;

                    CPU cpuParaAlterar = dao.getById(new Long(resposta));
                    if (cpuParaAlterar == null) {
                        System.out.println('\n' + "CPU com identificador " + resposta + " não foi encontrado!");
                        break;
                    }

                    System.out.println(cpuParaAlterar);

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. Arquitetura");
                    System.out.println('\n' + "2. Core");
                    System.out.println('\n' + "3. Cache");
                    System.out.println('\n' + "4. ClockFrequency");

                    int opcaoAlteracao = Console.readInt('\n' + "Digite um número de 1 a 4:");
                    CPU cpuAlterado = null;

                    try {
                        switch (opcaoAlteracao) {
                            case 1: {
                                String novaArquiteturaString = Console.readLine("Digite a nova arquitetura do CPU (ARM, X86, X86_64): ");
                                ArchitectureEnum novaArquitetura = ArchitectureEnum.valueOf(novaArquiteturaString);
                                cpuParaAlterar.setArchitecture(novaArquitetura);
                                cpuAlterado = dao.update(new Long(resposta), cpuParaAlterar);
                                break;
                            }

                            case 2: {
                                Integer novosCores = Console.readInt("Informe o valor do número de cores: ");
                                cpuParaAlterar.setCores(novosCores);
                                cpuAlterado = dao.update(new Long(resposta), cpuParaAlterar);
                                break;
                            }

                            case 3: {
                                Integer novoCache = Console.readInt("Informe o valor do cache em bytes: ");
                                cpuParaAlterar.setCache(novoCache);
                                cpuAlterado = dao.update(new Long(resposta), cpuParaAlterar);
                                break;
                            }

                            case 4: {
                                Double novoClockFrequency = Console.readDouble("Informe o valor do clock: ");
                                cpuParaAlterar.setClockFrequency(novoClockFrequency);
                                cpuAlterado = dao.update(new Long(resposta), cpuParaAlterar);
                                break;
                            }

                            default:
                                System.out.println('\n' + "Opção inválida!");
                        }
                    }
                    catch (Exception ex) {
                        System.out.println("Erro: " + ex.getMessage());
                        break;
                    }

                    System.out.println('\n' + "Alteração efetuada com sucesso!");
                    System.out.println(cpuAlterado);
                    break;
                }

                case 3:
                {
                    int resposta = Console.readInt('\n' + "Digite o número do cpu que você deseja remover: ");
                    CPU cpuParaRemover = dao.getById(new Long(resposta));
                    if (cpuParaRemover == null) {
                        System.out.println('\n' + "CPU com identificador " + resposta + " não foi encontrado!");
                        break;
                    }

                    System.out.println(cpuParaRemover);

                    String resp = Console.readLine('\n' +"Confirma a remoção do cpu? (s/n)");

                    if(resp.equals("s")) {
                        dao.delete(new Long(resposta));
                    }
                    else {
                        System.out.println('\n' + "CPU não removido.");
                    }

                    break;
                }

                case 4:
                {
                    List<CPU> cpus = dao.getAll();
                    cpus.stream().forEach(System.out::println);
                    break;
                }

                case 5:
                {
                    continua = false;
                    break;
                }

                default:
                    System.out.println('\n' + "Opção inválida!");
            }
        }

    }
}
