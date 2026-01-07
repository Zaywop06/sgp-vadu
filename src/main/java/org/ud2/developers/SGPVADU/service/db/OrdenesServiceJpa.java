package org.ud2.developers.SGPVADU.service.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ud2.developers.SGPVADU.entity.Orden;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.repository.OrdenesRepository;
import org.ud2.developers.SGPVADU.service.IntServiceOrdenes;

@Service
public class OrdenesServiceJpa implements IntServiceOrdenes {

	@Autowired
	private OrdenesRepository repoOrdenes;

	@Override
	public List<Orden> obtenerOrdenes() {
		return repoOrdenes.findAll();
	}

	@Override
	public void agregarOrden(Orden orden) {
		repoOrdenes.save(orden);
	}

	@Override
	public List<Orden> buscarPorUsuario(Usuario usuario) {
		return repoOrdenes.findByUsuario(usuario);
	}

	@Override
	public Orden buscarPorId(Integer idOrden) {
		Optional<Orden> optional = repoOrdenes.findById(idOrden);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Page<Orden> buscarTodas(Pageable page) {
		return repoOrdenes.findAll(page);
	}

	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";

		List<Orden> ordenes = obtenerOrdenes();

		List<Integer> numeros = new ArrayList<Integer>();

		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

		if (ordenes.isEmpty()) {
			numero = 1;
		} else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}

		if (numero < 10) {
			numeroConcatenado = "000000000" + String.valueOf(numero);
		} else if (numero < 100) {
			numeroConcatenado = "00000000" + String.valueOf(numero);
		} else if (numero < 1000) {
			numeroConcatenado = "0000000" + String.valueOf(numero);
		} else if (numero < 10000) {
			numeroConcatenado = "0000000" + String.valueOf(numero);
		}

		return numeroConcatenado;
	}

}
