package org.ud2.developers.SGPVADU.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ud2.developers.SGPVADU.entity.Producto;

public interface IntServiceProductos {
	public List<Producto> obtenerEnVenta();

	public List<Producto> obtenerProductos();

	public void agregarProducto(Producto producto);

	public Producto buscarPorId(Integer idProducto);

	public List<Producto> buscarPorCategoria(Integer idCategoria);

	public List<Producto> buscarPorDescripcion(String descripcion);

	public List<Producto> buscarTodasPorDescripcionYCategoria(String descripcion, Integer idCategoria);

	public void eliminarPorId(Integer idProducto);

	public Page<Producto> buscarTodas(Pageable page);

	public Page<Producto> buscarTodasEnVenta(Pageable page);

	/*public Page<Producto> buscarTodasPorEstatusYDescripcion(Pageable page, String descripcion);

	public Page<Producto> buscarTodasPorEstatusYCategoria(Pageable page, Integer idCategoria);

	public Page<Producto> buscarTodasPorEstatusYDescripcionYCategoria(Pageable page, String descripcion, Integer idCategoria);*/

	public Integer contarProductos();
}
