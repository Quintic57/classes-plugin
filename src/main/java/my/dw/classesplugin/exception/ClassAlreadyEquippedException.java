package my.dw.classesplugin.exception;

public class ClassAlreadyEquippedException extends Exception {

    public ClassAlreadyEquippedException() {
        super("The specified class is already equipped");
    }

}
