package org.ud2.developers.SGPVADU.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ud2.developers.SGPVADU.entity.Producto;

public interface ProductosRepository extends JpaRepository<Producto, Integer> {
	public List<Producto> findByEstatus(Integer estatus);

	@Query(value = "select * from Productos where idCategoria = ?", nativeQuery = true)
	public List<Producto> buscarTodosPorCategoria(Integer idCategoria);

	@Query(value = "select * from Productos where descripcion like %?%", nativeQuery = true)
	public List<Producto> buscarTodosPorDescripcion(String descripcion);

	@Query(value = "select * from Productos where descripcion like %?% and idCategoria = ?", nativeQuery = true)
	public List<Producto> findAllProductosByDescripcionAndCategoria(String descripcion, Integer idCategoria);

	public Page<Producto> findAllProductosByEstatus(Integer estatus, Pageable pageable);

	/*@Query(value = "select * from Productos where estatus = 1 and descripcion like %?%", nativeQuery = true)
	public Page<Producto> findByCriteria(String descripcion, Pageable pageable);
	
	@Query(value = "select * from Productos where estatus = 1 and idCategoria = ?", nativeQuery = true)
	public Page<Producto> findByCriteria(Integer idCategoria, Pageable pageable);
	
	@Query(value = "select * from Productos where estatus = 1 and descripcion like %?% and idCategoria = ?", nativeQuery = true)
	public Page<Producto> findByCriteria(String descripcion, Integer idCategoria, Pageable pageable);*/

	@Query(value = "select count(*) from Productos", nativeQuery = true)
	public Integer cantidadProductos();
}
