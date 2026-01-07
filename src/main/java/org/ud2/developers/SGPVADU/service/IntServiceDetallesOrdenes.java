package org.ud2.developers.SGPVADU.service;

import java.util.List;

import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.DetalleOrden;

public interface IntServiceDetallesOrdenes {
	public List<DetalleOrden> obtenerDetalles();

	public void agregarDetalle(DetalleOrden detalleOrden);

	public void eliminarPorId(Integer idDetalleOrden);
	
	public void eliminarPorIdCliente(Integer idCliente);	
	
	public DetalleOrden buscarPorId(Integer idDetalleOrden);
	
	public List<DetalleOrden> buscarPorCliente(Cliente cliente);
	
	public Integer contarDetalles();
	
	public Integer contarDetallesPorCliente(Integer idCliente);
}
