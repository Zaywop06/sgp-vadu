package org.ud2.developers.SGPVADU.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ud2.developers.SGPVADU.entity.Contacto;
import org.ud2.developers.SGPVADU.repository.ContactosRepository;
import org.ud2.developers.SGPVADU.service.IntServiceContactos;

@Service
public class ContactosServiceJpa implements IntServiceContactos {

	@Autowired
	private ContactosRepository repoContactos;

	@Override
	public List<Contacto> obtenerContactos() {
		return repoContactos.findAll();
	}

	@Override
	public void agregarContacto(Contacto contacto) {
		repoContactos.save(contacto);
	}

	@Override
	public Contacto buscarPorId(Integer idContacto) {
		Optional<Contacto> optional = repoContactos.findById(idContacto);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Integer contarContactos() {
		return repoContactos.cantidadContactos();
	}

	@Override
	public Page<Contacto> buscarTodas(Pageable page) {
		return repoContactos.findAll(page);
	}

}
