package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Empleado implements Serializable {
    private int idEmpleado;
    private String nombre;
    private String apellido;
    private String correo;

    public Empleado(String nombre, String apellido, String correo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }

    public void mostrarDatos(){
        System.out.println("idEmpleado = " + idEmpleado);
        System.out.println("nombre = " + nombre);
        System.out.println("correo = " + apellido);
        System.out.println("correo = " + correo);
    }
}



