package org.ud2.developers.SGPVADU.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ud2.developers.SGPVADU.entity.Producto;
import org.ud2.developers.SGPVADU.repository.ProductosRepository;
import org.ud2.developers.SGPVADU.service.IntServiceProductos;

@Service
public class ProductosServiceJpa implements IntServiceProductos {

	@Autowired
	private ProductosRepository repoProductos;

	@Override
	public List<Producto> obtenerEnVenta() {
		return repoProductos.findByEstatus(1);
	}

	@Override
	public List<Producto> obtenerProductos() {
		return repoProductos.findAll();
	}

	@Override
	public void agregarProducto(Producto producto) {
		repoProductos.save(producto);
	}

	@Override
	public Producto buscarPorId(Integer idProducto) {
		Optional<Producto> optional = repoProductos.findById(idProducto);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Producto> buscarPorCategoria(Integer idCategoria) {
		return repoProductos.buscarTodosPorCategoria(idCategoria);
	}

	@Override
	public List<Producto> buscarPorDescripcion(String descripcion) {
		return repoProductos.buscarTodosPorDescripcion(descripcion);
	}

	@Override
	public List<Producto> buscarTodasPorDescripcionYCategoria(String descripcion, Integer idCategoria) {
		return repoProductos.findAllProductosByDescripcionAndCategoria(descripcion, idCategoria);
	}

	@Override
	public void eliminarPorId(Integer idProducto) {
		repoProductos.deleteById(idProducto);
	}

	@Override
	public Page<Producto> buscarTodas(Pageable page) {
		return repoProductos.findAll(page);
	}

	@Override
	public Page<Producto> buscarTodasEnVenta(Pageable page) {
		return repoProductos.findAllProductosByEstatus(1, page);
	}

	/*@Override
	public Page<Producto> buscarTodasPorEstatusYDescripcion(Pageable page, String descripcion) {
		return repoProductos.findByCriteria(descripcion, page);
	}

	@Override
	public Page<Producto> buscarTodasPorEstatusYCategoria(Pageable page, Integer idCategoria) {
		return repoProductos.findByCriteria(idCategoria, page);
	}
	
	@Override
	public Page<Producto> buscarTodasPorEstatusYDescripcionYCategoria(Pageable page, String descripcion, Integer idCategoria) {
		return repoProductos.findByCriteria(descripcion, idCategoria, page);
	}*/

	@Override
	public Integer contarProductos() {
		return repoProductos.cantidadProductos();
	}

}
