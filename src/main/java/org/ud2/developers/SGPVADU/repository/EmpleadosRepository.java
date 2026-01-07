package org.ud2.developers.SGPVADU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ud2.developers.SGPVADU.entity.Empleado;

public interface EmpleadosRepository extends JpaRepository<Empleado, Integer> {
	@Query(value = "select count(*) from Empleados", nativeQuery = true)
	public Integer cantidadEmpleados();
}
