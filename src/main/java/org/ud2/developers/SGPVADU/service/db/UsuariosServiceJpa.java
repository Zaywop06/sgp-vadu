package org.ud2.developers.SGPVADU.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.repository.UsuariosRepository;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;

@Service
public class UsuariosServiceJpa implements IntServiceUsuarios {

	@Autowired
	private UsuariosRepository repoUsuarios;

	@Override
	public List<Usuario> obtenerUsuarios() {
		return repoUsuarios.findAll();
	}

	@Override
	public void agregarUsuario(Usuario usuario) {
		repoUsuarios.save(usuario);
	}

	@Override
	public Usuario buscarPorId(Integer idUsuario) {
		Optional<Usuario> optional = repoUsuarios.findById(idUsuario);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		return repoUsuarios.findByUsername(username);
	}

	@Override
	public void eliminarPorId(Integer idUsuario) {
		repoUsuarios.deleteById(idUsuario);
	}

	@Override
	public Integer contarUsuarios() {
		return repoUsuarios.cantidadUsuarios();
	}

	@Override
	public Page<Usuario> buscarTodas(Pageable page) {
		return repoUsuarios.findAll(page);
	}

}
