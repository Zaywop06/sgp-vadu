package org.ud2.developers.SGPVADU.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ud2.developers.SGPVADU.entity.Orden;
import org.ud2.developers.SGPVADU.entity.Usuario;

public interface OrdenesRepository extends JpaRepository<Orden, Integer> {
	public List<Orden> findByUsuario(Usuario usuario);
}
