package com.uacm.cm.puntodeventa.modelo;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private int edad;
    private String correo;
    private String contrasenia;

    public Usuario(String nombre, String apellidos, int edad, String correo, String contrasenia) throws Exception {
        try{
            asignarNombre(nombre);
            asignarApellidos(apellidos);
            asignarEdad(edad);
            asignarCorreo(correo);
            asiganrContrasenia(contrasenia);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Usuario(int id, String nombre, String apellidos, int edad, String correo, String contrasenia) throws Exception {
        try{
            this.id = id;
            asignarNombre(nombre);
            asignarApellidos(apellidos);
            asignarEdad(edad);
            asignarCorreo(correo);
            asiganrContrasenia(contrasenia);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public String dameNombre() {
        return nombre;
    }

    private void asignarNombre(String nombre) throws Exception {
        Pattern pattern = Pattern.compile("^([A-Za-zÁÉÍÓÚáéíóúÑñ]{2,})(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,}){0,3}$");
        Matcher matcher = pattern.matcher(nombre);
        if(matcher.matches()){
            this.nombre = nombre;
        }else{
            throw new Exception("nombre incorrecto");
        }
    }

    public String dameApellidos() {
        return apellidos;
    }

    private void asignarApellidos(String apellidos) throws Exception {
        Pattern pattern = Pattern.compile("^([A-Za-zÁÉÍÓÚáéíóúÑñ]{2,})(\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,}){0,2}$");
        Matcher matcher = pattern.matcher(apellidos);
        if(matcher.matches()){
            this.apellidos = apellidos;
        }else{
            throw new Exception("apellido incorrecto");
        }
    }

    public int dameEdad() {
        return edad;
    }

    private void asignarEdad(int edad) throws Exception {
        if(edad>2 && edad<90){
            this.edad = edad;
        }else{
            throw new Exception("rango de edad valido: 2<edad<90");
        }
    }

    public String dameCorreo() {
        return correo;
    }

    private void asignarCorreo(String correo) throws Exception {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        Matcher matcher = pattern.matcher(correo);
        if(matcher.matches()){
            this.correo = correo;
        }else{
            throw new Exception("correo no valido");
        }
    }
    public String dameContrasenia() {
        return contrasenia;
    }

    private void asiganrContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int dameId(){
        return id;
    }
}
