package org.ud2.developers.SGPVADU.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.DetalleOrden;

public interface DetallesOrdenesRepository extends JpaRepository<DetalleOrden, Integer> {
	public List<DetalleOrden> findByCliente(Cliente cliente);

	@Transactional
	@Modifying
	@Query(value = "delete from Detalles where cliente_id = ?", nativeQuery = true)
	public void deleteByIdCliente(Integer idCliente);

	@Query(value = "select count(*) from Detalles where cliente_id = ?", nativeQuery = true)
	public Integer cantidadDetallesPorCliente(Integer idCliente);

	@Query(value = "select count(*) from Detalles", nativeQuery = true)
	public Integer cantidadDetalles();
}
