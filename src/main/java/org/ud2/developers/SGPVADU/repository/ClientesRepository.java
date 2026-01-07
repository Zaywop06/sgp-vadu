package org.ud2.developers.SGPVADU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.Usuario;

public interface ClientesRepository extends JpaRepository<Cliente, Integer> {
	public Cliente findByUsuario(Usuario usuario);

	@Query(value = "select count(*) from Clientes", nativeQuery = true)
	public Integer cantidadClientes();
}
