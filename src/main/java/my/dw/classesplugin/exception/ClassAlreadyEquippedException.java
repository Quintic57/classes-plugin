package my.dw.classesplugin.exception;

public class ClassAlreadyEquippedException extends RuntimeException {

    public ClassAlreadyEquippedException() {
        super("The specified class is already equipped");
    }

}
