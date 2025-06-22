package com.PRISMA.Asistencia;

import java.util.List;

import com.PRISMA.Entity.AsistenciaAlumno;

public interface IAsistenciaServicio {

    public List<AsistenciaAlumno> listarAsistencia(); 

    public AsistenciaAlumno buscarAsistenciaPorId(Integer id_asistencia);

    public void guardarAsistencia(AsistenciaAlumno asistenciaAlumno);

}
