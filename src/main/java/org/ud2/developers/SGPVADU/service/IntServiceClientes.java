package org.ud2.developers.SGPVADU.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.Usuario;

public interface IntServiceClientes {
	public List<Cliente> obtenerClientes();

	public void agregarCliente(Cliente cliente);

	public Cliente buscarPorId(Integer idCliente);

	public Cliente buscarPorUsuario(Usuario usuario);

	public void eliminarPorId(Integer idCliente);

	public Integer contarClientes();

	public Page<Cliente> buscarTodas(Pageable page);
}
