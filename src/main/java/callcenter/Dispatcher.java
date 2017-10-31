package callcenter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tiene la capacidad de procesar 10 llamadas concurrentemente.
 * Si entran mas de 10 llamadas concurrentes, las llamadas adicionales esperan en una cola
 * y van a ser procesadas a medida que se terminen las llamadas en curso.
 * Si todos los empleados estan ocupados, las llamadas entrantes esperan a que se liberen los empleados.
 */
public class Dispatcher {

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    private List<Empleado> operadores;
    private List<Empleado> supervisores;
    private List<Empleado> directores;

    public Dispatcher(List<Empleado> operadores, List<Empleado> supervisores, List<Empleado> directores) {
        this.operadores = operadores;
        this.supervisores = supervisores;
        this.directores = directores;
    }

    public void dispatchCall(Llamada call) throws InterruptedException {

        executorService.submit(() -> {
            Empleado empleado = asignarEmpleado();
            String threadName = Thread.currentThread().getName();
            System.out.println("Thread " + threadName);
            System.out.println("Procesando llamada de " + call.duracion() + " ms");
            try {
                empleado.atender(call);
            } catch (InterruptedException e) {
                System.out.println("callcenter.Llamada interrumpida");
            }
            this.liberarEmpleado(empleado);
        });
    }

    /**
     * Metodo sinchronized
     */
    private synchronized void liberarEmpleado(Empleado empleado) {
        empleado.setOcupado(false);
        this.notifyAll();
    }

    /**
     * Metodo sinchronized
     */
    private synchronized Empleado asignarEmpleado() {
        System.out.println("Buscando empleado");
        Empleado empleado = obtenerEmpleado();
        while(empleado == null) {
            System.out.println("Esperando empleado");
            try {
                this.wait();
            } catch (InterruptedException e) {}
            empleado = obtenerEmpleado();
        }
        empleado.setOcupado(true);
        System.out.println("Empleado asignado");
        return empleado;
    }

    private Empleado obtenerEmpleado() {
        Optional<Empleado> optional = operadores.stream().filter(em -> !em.isOcupado()).findAny();
        if(!optional.isPresent()) {
            optional = supervisores.stream().filter(em -> !em.isOcupado()).findAny();
        }
        if(!optional.isPresent()) {
            optional = directores.stream().filter(em -> !em.isOcupado()).findAny();
        }
        return optional.orElse(null);
    }

    public void finalizar() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
