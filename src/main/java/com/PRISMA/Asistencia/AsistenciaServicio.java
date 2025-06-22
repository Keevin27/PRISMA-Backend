package com.PRISMA.Asistencia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PRISMA.Entity.AsistenciaAlumno;

@Service
public class AsistenciaServicio implements IAsistenciaServicio{

    @Autowired
    private AsistenciaRepositorio asistenciaRepositorio;

    @Override
    public List<AsistenciaAlumno> listarAsistencia() {

        return this.asistenciaRepositorio.findAll();
    
    }

    @Override
    public AsistenciaAlumno buscarAsistenciaPorId(Integer id_asistencia) {
        AsistenciaAlumno asistenciaAlumno = this.asistenciaRepositorio.findById(id_asistencia).orElse(null);
        return asistenciaAlumno;
        }

    @Override
    public void guardarAsistencia(AsistenciaAlumno asistenciaAlumno) {

        this.asistenciaRepositorio.save(asistenciaAlumno);
    
    }

    

}
