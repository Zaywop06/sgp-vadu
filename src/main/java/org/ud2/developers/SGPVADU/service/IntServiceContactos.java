package org.ud2.developers.SGPVADU.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ud2.developers.SGPVADU.entity.Contacto;

public interface IntServiceContactos {
	public List<Contacto> obtenerContactos();

	public void agregarContacto(Contacto contacto);

	public Contacto buscarPorId(Integer idContacto);

	public Integer contarContactos();

	public Page<Contacto> buscarTodas(Pageable page);
}
