import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// ─────────────────────────────────────────────────
// GRASP 1 — INFORMATION EXPERT
// Livro conhece seus próprios dados.
// Responsabilidade de se apresentar é dele.
// ─────────────────────────────────────────────────
class Livro {
    private String titulo;
    private String autor;

    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    public String getDescricao() {
        return titulo + " — " + autor;
    }
}

// ─────────────────────────────────────────────────
// GRASP 1 — INFORMATION EXPERT + GRASP 3 — HIGH COHESION
// Usuario conhece seus dados e a regra
// de penalidade que lhe diz respeito.
// ─────────────────────────────────────────────────
class Usuario {
    private String nome;
    private boolean adimplente;

    public Usuario(String nome, boolean adimplente) {
        this.nome = nome;
        this.adimplente = adimplente;
    }

    public String getNome() { return nome; }

    public boolean temPenalidadeExtra() {
        return !adimplente;
    }
}

// ─────────────────────────────────────────────────
// GRASP 2 — PURE FABRICATION
// Não existe no mundo real da biblioteca,
// mas isola e centraliza a regra de multa.
// Se a regra mudar, mexe só aqui.
// ─────────────────────────────────────────────────
class CalculadoraMulta {
    private static final double MULTA_POR_DIA = 2.50;
    private static final double FATOR_PENALIDADE = 1.5;

    public double calcular(long diasAtraso, Usuario usuario) {
        if (diasAtraso <= 0) return 0;

        double multa = diasAtraso * MULTA_POR_DIA;
        if (usuario.temPenalidadeExtra()) {
            multa *= FATOR_PENALIDADE;
        }
        return multa;
    }
}

// ─────────────────────────────────────────────────
// GRASP 3 — HIGH COHESION + LOW COUPLING
// Emprestimo gerencia apenas o que é seu:
// livro, usuário e datas.
// Não sabe calcular multa, não imprime nada.
// ─────────────────────────────────────────────────
class Emprestimo {
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataPrevista;
    private LocalDate dataReal;

    public Emprestimo(Livro livro, Usuario usuario,
                      LocalDate dataPrevista, LocalDate dataReal) {
        this.livro = livro;
        this.usuario = usuario;
        this.dataPrevista = dataPrevista;
        this.dataReal = dataReal;
    }

    public long calcularDiasAtraso() {
        return Math.max(0, ChronoUnit.DAYS.between(dataPrevista, dataReal));
    }

    public Livro getLivro()       { return livro; }
    public Usuario getUsuario()   { return usuario; }
    public LocalDate getDataPrevista() { return dataPrevista; }
    public LocalDate getDataReal()     { return dataReal; }
}

// ─────────────────────────────────────────────────
// GRASP 4 — CONTROLLER
// Orquestra o fluxo de devolução sem ter
// lógica de negócio — apenas coordena.
// ─────────────────────────────────────────────────
class DevolucaoController {
    private CalculadoraMulta calculadora = new CalculadoraMulta();

    public void processarDevolucao(Emprestimo emprestimo) {
        long diasAtraso = emprestimo.calcularDiasAtraso();
        double multa = calculadora.calcular(diasAtraso, emprestimo.getUsuario());
        imprimirRecibo(emprestimo, diasAtraso, multa);
    }

    private void imprimirRecibo(Emprestimo emp, long diasAtraso, double multa) {
        System.out.println("======= RECIBO DE DEVOLUÇÃO =======");
        System.out.println("Usuário: " + emp.getUsuario().getNome());
        System.out.println("Livro:   " + emp.getLivro().getDescricao());
        System.out.println("Devolução prevista: " + emp.getDataPrevista());
        System.out.println("Devolução real:     " + emp.getDataReal());
        if (multa > 0) {
            System.out.println("Dias de atraso: " + diasAtraso);
            System.out.printf("Multa: R$ %.2f%n", multa);
        } else {
            System.out.println("Devolução no prazo. Sem multa.");
        }
        System.out.println("===================================");
    }
}

// ─────────────────────────────────────────────────
// PONTO DE ENTRADA
// ─────────────────────────────────────────────────
public class Biblioteca {
    public static void main(String[] args) {
        Livro livro = new Livro("O Senhor dos Anéis", "Tolkien");
        Usuario usuario = new Usuario("Maria Silva", false);

        Emprestimo emprestimo = new Emprestimo(
                livro, usuario,
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 10)
        );

        DevolucaoController controller = new DevolucaoController();
        controller.processarDevolucao(emprestimo);
    }
}