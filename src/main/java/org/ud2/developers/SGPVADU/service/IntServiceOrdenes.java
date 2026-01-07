package org.ud2.developers.SGPVADU.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ud2.developers.SGPVADU.entity.Orden;
import org.ud2.developers.SGPVADU.entity.Usuario;

public interface IntServiceOrdenes {
	public List<Orden> obtenerOrdenes();

	public void agregarOrden(Orden orden);

	public List<Orden> buscarPorUsuario(Usuario usuario);

	public Orden buscarPorId(Integer idOrden);

	public Page<Orden> buscarTodas(Pageable page);

	public String generarNumeroOrden();
}
