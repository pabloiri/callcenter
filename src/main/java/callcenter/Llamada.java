package callcenter;

import java.util.Random;

public class Llamada {

    private int duracion = 5000 + (int) (Math.random() * 5000);

    public int duracion() {
        return duracion;
    }
}
