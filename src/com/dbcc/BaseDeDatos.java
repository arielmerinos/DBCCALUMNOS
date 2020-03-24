package com.dbcc;

import com.dbcc.Alumno;
import com.dbcc.Materia;
import com.util.Lista;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class BaseDeDatos {
    private static Lista<Alumno> listaMadre = new Lista<>();
    private static Lista<Materia> listaMaterias = new Lista<>();
    private static int contador;

    public void leyendo(){
        try {
            Scanner input = new Scanner(new File("src/alumnos.txt"));
            while (input.hasNextLine()) {
                /**
                 * Este while es para cada alumno (bueno cada linea del txt)
                 */
                String line = input.nextLine();
                String[] casit = line.split(",");
                String name = casit[0].substring(1);
                String materia = casit[1];
                materia = materia.substring(1,materia.length()-1);
                Alumno nuevo = new Alumno(name, materia);
                listaMadre.agregar(nuevo);
            }
            input.close();
        } catch (FileNotFoundException e){
            System.out.println("NO se encontro el archivo");
        } catch (IOException e){
            System.out.println(e);
        } catch (Exception ex) {
            System.out.println("El documento no se pudo abrir, intente de nuevo");
        }
    }
    public Lista<Alumno> getAlumnos(){
        return listaMadre;
    }

    public Materia asignaAlumnos(String nombreMateria){
        Materia materia = new Materia(nombreMateria);
        for (Alumno al: listaMadre) {
            if (al.getMateria().equals(nombreMateria)){
                materia.agregarAlumno(al);
            }
        }
        return materia;
    }

    public Lista<String> alumnosSinRepetir(){
        Lista<String> alumnosSinRepetir = new Lista<>();
        for (Alumno estudiante: listaMadre) {
            if (!alumnosSinRepetir.contiene(estudiante.getName())){
                estudiante.setMatricula(++contador);
                alumnosSinRepetir.agregar(estudiante.getName());
            }
        }
        return alumnosSinRepetir;
    }


    public Lista<Alumno> unionRelacionar(){
        Lista<Alumno> listaFinalF = new Lista<>();
        for (String nombre: alumnosSinRepetir()){
            Alumno nuevo = new Alumno(nombre);
            for (Alumno estudiante : listaMadre) {
                if (nombre.equals(estudiante.getName())){
                    if (estudiante.getMatricula() != 0){
                        nuevo.setMatricula(estudiante.getMatricula());
                    }
                    nuevo.agregarMaterias(estudiante.getMateria(),"profe x definir",0);
                }
            }
            listaFinalF.agregar(nuevo);
        }
        return listaFinalF;
    }
    public Lista<String> materiasSinRepetir(){
        Lista<String> materiasSinR = new Lista<>();
        for (Alumno estudiante: listaMadre) {
            if (!materiasSinR.contiene(estudiante.getMateria())){
                materiasSinR.agregar(estudiante.getMateria());
            }
        }
        return materiasSinR;
    }

    public Lista<Materia> materiasAlumnos() {
        Lista<Materia> materiasL = new Lista<>();
        for (String mat: materiasSinRepetir()) {
            materiasL.agregar(asignaAlumnos(mat));
        }
        listaMaterias = materiasL;
        return materiasL;
    }

    public Lista<Materia> asignarClaveProfe(Materia materia, String profe, int clave){
        Lista<Materia> modiicada = listaMaterias;
        for (Materia materi : modiicada) {
            if (materi.getNombre().toLowerCase().equals(materia.getNombre().toLowerCase())){
                Materia aux = materi;
                aux.setProfesora(profe);
                aux.setClave(clave);
                materiasAlumnos().eliminar(materi);
                materiasAlumnos().agregar(aux);
            }
        }
        listaMaterias = modiicada;
        return modiicada;
    }
    public Materia buscaMateria(String name){
        for (Materia materia : materiasAlumnos()){
            if (materia.getNombre().toLowerCase().equals((name.toLowerCase()))){
                return materia;
            }
        }
        return null;
    }
    public Alumno buscaAlumnos(String name){
        for (Alumno estudiante: unionRelacionar()) {
            if (estudiante.getName().toLowerCase().equals(name.toLowerCase())){
                return estudiante;
            }
        }
        return null;
    }

    public static Lista<Materia> getListaMaterias() {
        return listaMaterias;
    }
}