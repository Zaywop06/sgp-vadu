package org.ud2.developers.SGPVADU.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.repository.PerfilesRepository;
import org.ud2.developers.SGPVADU.service.IntServicePerfiles;

@Service
public class PerfilesServiceJpa implements IntServicePerfiles {

	@Autowired
	private PerfilesRepository repoPerfiles;

	@Override
	public List<Perfil> obtenerPerfiles() {
		return repoPerfiles.findAll();
	}
}
