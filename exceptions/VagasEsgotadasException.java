package exceptions;

public class VagasEsgotadasException extends RuntimeException {
    public VagasEsgotadasException(String mensagem) {
        super(mensagem);
    }
}