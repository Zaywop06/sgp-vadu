package org.ud2.developers.SGPVADU.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ud2.developers.SGPVADU.entity.Categoria;

public interface IntServiceCategorias {
	public List<Categoria> obtenerCategorias();

	public void agregarCategoria(Categoria categoria);

	public Categoria buscarPorId(Integer idCategoria);

	public void eliminarPorId(Integer idCategoria);

	public Integer contarCategorias();

	public Page<Categoria> buscarTodas(Pageable page);
}
