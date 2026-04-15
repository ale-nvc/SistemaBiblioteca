import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo1{

    // Dados do livro misturados aqui
    String tituloLivro;
    String autorLivro;

    // Dados do usuário misturados aqui também
    String nomeUsuario;
    boolean usuarioAdimplente;

    // Dados do empréstimo
    LocalDate dataDevolucaoPrevista;
    LocalDate dataDevolucaoReal;

    // Construtor gigante com tudo junto
    public Emprestimo1(String tituloLivro, String autorLivro,
                      String nomeUsuario, boolean usuarioAdimplente,
                      LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal) {
        this.tituloLivro = tituloLivro;
        this.autorLivro = autorLivro;
        this.nomeUsuario = nomeUsuario;
        this.usuarioAdimplente = usuarioAdimplente;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    // Emprestimo calculando multa — mas os dados de prazo são dela, ok...
    // O problema é que a REGRA de negócio está hard-coded aqui dentro
    public double calcularMulta() {
        long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataDevolucaoReal);
        if (diasAtraso <= 0) return 0;

        // Regra de multa misturada com regra de usuário adimplente
        double multa = diasAtraso * 2.50;
        if (!usuarioAdimplente) {
            multa = multa * 1.5; // penalidade extra — regra de usuário jogada aqui
        }
        return multa;
    }

    // Emprestimo também imprime recibo — responsabilidade que não é dela
    public void imprimirRecibo() {
        System.out.println("======= RECIBO DE DEVOLUÇÃO =======");
        System.out.println("Usuário: " + nomeUsuario);
        System.out.println("Livro: " + tituloLivro + " - " + autorLivro);
        System.out.println("Devolução prevista: " + dataDevolucaoPrevista);
        System.out.println("Devolução real:     " + dataDevolucaoReal);

        // Lógica de multa DUPLICADA aqui
        long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataDevolucaoReal);
        if (diasAtraso > 0) {
            double multa = diasAtraso * 2.50;
            if (!usuarioAdimplente) multa *= 1.5;
            System.out.printf("Multa: R$ %.2f%n", multa);
        } else {
            System.out.println("Sem multa.");
        }
        System.out.println("===================================");
    }

    public static void main(String[] args) {
        Emprestimo1 e = new Emprestimo1(
                "O Senhor dos Anéis", "Tolkien",
                "Maria Silva", false,
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 10)
        );

        e.imprimirRecibo();
        System.out.println("Multa calculada: R$ " + e.calcularMulta());
    }
}