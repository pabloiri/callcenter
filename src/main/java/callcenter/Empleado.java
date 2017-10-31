package callcenter;

/**
 * Esta clase representa un empleado de una central telefonica.
 */

public abstract class Empleado {

    private boolean ocupado;

    private int llamadasAtendidas;

    void atender(Llamada llamada) throws InterruptedException {
        Thread.sleep(llamada.duracion());
        llamadasAtendidas++;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    /**
     * Este metodo debe ser invocado solo desde el codigo synchronized del dispatcher por cuestiones de concurrencia.
     * @param ocupado
     */
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public int getLlamadasAtendidas() {
        return llamadasAtendidas;
    }
}
