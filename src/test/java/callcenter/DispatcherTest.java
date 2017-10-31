package callcenter;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Estos son tests de integracion.
 * El enunciado pedia un test de 10 llamadas concurrentes.
 */
public class DispatcherTest {

    private Dispatcher dispatcher;
    List<Empleado> operadores;
    List<Empleado> supervisores;
    List<Empleado> directores;

    @Before
    public void setUp() throws Exception {
        operadores = new ArrayList();
        supervisores = new ArrayList();
        directores = new ArrayList();
    }

    @Test
    public void dispatchCall_3Llamadas5empleados() throws Exception {
        crearEmpleados(3, 1, 1);
        crearDispacherDespacharLlamadasYFinalizar(3);
        // solo atienden llamadas los operadores
        assertEquals(0, directores.get(0).getLlamadasAtendidas());
        assertEquals(0, supervisores.get(0).getLlamadasAtendidas());
        for (Empleado operador : operadores) {
            assertEquals(1, operador.getLlamadasAtendidas());
        }
    }

    @Test
    public void dispatchCall_5Llamadas3empleados() throws Exception {
        crearEmpleados(1, 1, 1);
        crearDispacherDespacharLlamadasYFinalizar(5);
        // todos atienden al menos 1 llamada
        assertEquals(5, totalLlamadasAtendidas());
        assertTrue(directores.get(0).getLlamadasAtendidas() >= 1);
        assertTrue(supervisores.get(0).getLlamadasAtendidas() >= 1);
        for (Empleado operador : operadores) {
            assertTrue(operador.getLlamadasAtendidas() >= 1);
        }
    }

    @Test
    public void dispatchCall_5Llamadas5empleados() throws Exception {
        crearEmpleados(3, 1, 1);
        crearDispacherDespacharLlamadasYFinalizar(5);
        // todos atienden 1 llamada
        assertEquals(1, directores.get(0).getLlamadasAtendidas());
        assertEquals(1, supervisores.get(0).getLlamadasAtendidas());
        for (Empleado operador : operadores) {
            assertEquals(1, operador.getLlamadasAtendidas());
        }
    }

    @Test
    public void dispatchCall_10Llamadas5empleados() throws Exception {
        crearEmpleados(3, 1, 1);
        crearDispacherDespacharLlamadasYFinalizar(10);
        // se atendieron las 10 llamadas.
        assertEquals(10, totalLlamadasAtendidas());
    }

    @Test
    public void dispatchCall_15Llamadas5empleados() throws Exception {
        crearEmpleados(3, 1, 1);
        crearDispacherDespacharLlamadasYFinalizar(15);
        // se atendieron las 10 llamadas.
        assertEquals(15, totalLlamadasAtendidas());
    }


    private void crearDispacherDespacharLlamadasYFinalizar(int llamadas) throws InterruptedException {
        dispatcher = new Dispatcher(operadores, supervisores, directores);
        for (int i = 0; i < llamadas; i++) {
            dispatcher.dispatchCall(new Llamada());
        }
        dispatcher.finalizar();
    }

    private void crearEmpleados(int cantOp, int cantSup, int cantDir) {
        for (int i = 0; i < cantOp; i++) {
            operadores.add(new Operador());
        }
        for (int i = 0; i < cantSup; i++) {
            supervisores.add(new Supervisor());
        }
        for (int i = 0; i < cantDir; i++) {
            directores.add(new Director());
        }
    }

    private int totalLlamadasAtendidas() {
        int llamadas = 0;
        for (Empleado em : operadores) {
            llamadas += em.getLlamadasAtendidas();
        }
        for (Empleado em : supervisores) {
            llamadas += em.getLlamadasAtendidas();
        }
        for (Empleado em : directores) {
            llamadas += em.getLlamadasAtendidas();
        }
        return llamadas;
    }


}