package org.ud2.developers.SGPVADU.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ud2.developers.SGPVADU.entity.Categoria;
import org.ud2.developers.SGPVADU.repository.CategoriasRepository;
import org.ud2.developers.SGPVADU.service.IntServiceCategorias;

@Service
public class CategoriasServiceJpa implements IntServiceCategorias {

	@Autowired
	private CategoriasRepository repoCategorias;

	@Override
	public List<Categoria> obtenerCategorias() {
		return repoCategorias.findAll();
	}

	@Override
	public void agregarCategoria(Categoria categoria) {
		repoCategorias.save(categoria);
	}

	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		Optional<Categoria> optional = repoCategorias.findById(idCategoria);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void eliminarPorId(Integer idCategoria) {
		repoCategorias.deleteById(idCategoria);
	}

	@Override
	public Integer contarCategorias() {
		return repoCategorias.cantidadCategorias();
	}

	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		return repoCategorias.findAll(page);
	}

}
