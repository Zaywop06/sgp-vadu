package org.ud2.developers.SGPVADU.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ud2.developers.SGPVADU.entity.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	@Query(value = "select count(*) from Categorias", nativeQuery = true)
	public Integer cantidadCategorias();
}