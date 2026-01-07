package org.ud2.developers.SGPVADU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ud2.developers.SGPVADU.entity.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	public Usuario findByUsername(String username);

	@Query(value = "select count(*) from Usuarios", nativeQuery = true)
	public Integer cantidadUsuarios();
}
