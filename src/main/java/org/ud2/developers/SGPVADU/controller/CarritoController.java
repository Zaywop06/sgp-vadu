package org.ud2.developers.SGPVADU.controller;

import java.text.DecimalFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.DetalleOrden;
import org.ud2.developers.SGPVADU.entity.Orden;
import org.ud2.developers.SGPVADU.entity.Producto;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.service.IntServiceClientes;
import org.ud2.developers.SGPVADU.service.IntServiceDetallesOrdenes;
import org.ud2.developers.SGPVADU.service.IntServiceOrdenes;
import org.ud2.developers.SGPVADU.service.IntServiceProductos;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

	@Autowired
	private IntServiceDetallesOrdenes serviceDetallesOrdenes;

	@Autowired
	private IntServiceOrdenes serviceOrdenes;

	@Autowired
	private IntServiceProductos serviceProductos;

	@Autowired
	private IntServiceUsuarios serviceUsuarios;

	@Autowired
	private IntServiceClientes serviceClientes;

	DecimalFormat df = new DecimalFormat("0.0");

	@PostMapping("/cambiar")
	public String aumentarCantidadProductos(DetalleOrden detalleOrden, Integer cantidad) {
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setTotal(detalleOrden.getPrecio()*cantidad);
		serviceDetallesOrdenes.agregarDetalle(detalleOrden);
		System.out.println(detalleOrden);
		System.out.println(cantidad);
		return "redirect:/carrito/";
	}
	
	@PostMapping("/confirmar")
	public String confirmarOrden(Cliente cliente, RedirectAttributes model) {
		Cliente c = serviceClientes.buscarPorId(cliente.getId());
		c.setTelefono(cliente.getTelefono());
		c.setCalle(cliente.getCalle());
		c.setMunicipio(cliente.getMunicipio());
		c.setColonia(cliente.getColonia());
		c.setCiudad(cliente.getCiudad());
		c.setEstado(cliente.getEstado());
		c.setCp(cliente.getCp());
		serviceClientes.agregarCliente(c);
		model.addFlashAttribute("msg",
				"La infromación de dirección se guardó correctamente, ya está listo para ordenar.");
		return "redirect:/carrito/";
	}

	@GetMapping()
	public Integer contarItems(@AuthenticationPrincipal(expression = "username") String username) {
		Usuario usuario = serviceUsuarios.buscarPorUsername(username);
		Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
		return serviceDetallesOrdenes.contarDetallesPorCliente(cliente.getId());
	}

	@GetMapping("/orden")
	public String mostrarOrden(RedirectAttributes model, org.springframework.security.core.Authentication auth) {
		Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
		Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
		Orden orden = new Orden();
		double sumaTotal = serviceDetallesOrdenes.buscarPorCliente(cliente).stream().mapToDouble(dt -> dt.getTotal())
				.sum();
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(serviceOrdenes.generarNumeroOrden());

		// Usuario
		orden.setUsuario(usuario);
		orden.setTotal(Double.parseDouble(df.format(sumaTotal * .16 + sumaTotal)));
		serviceOrdenes.agregarOrden(orden);
		
		// Guardar detalles
		for (DetalleOrden dt : serviceDetallesOrdenes.buscarPorCliente(cliente)) {
			dt.setOrden(orden);
			Producto producto = dt.getProducto();
			producto.setCantidadIngreso(producto.getCantidadIngreso() - dt.getCantidad());
			if (producto.getCantidadIngreso() == 0) {
				producto.setEstatus(0);
			}
			serviceProductos.agregarProducto(producto);
			//serviceDetallesOrdenes.agregarDetalle(dt);
		}
		serviceDetallesOrdenes.eliminarPorIdCliente(cliente.getId());

		model.addFlashAttribute("msg", "La orden se realizó correctamente.");
		return "redirect:/carrito/";
	}

	@GetMapping("/eliminar")
	public String eliminarCarrito(DetalleOrden detalleOrd, RedirectAttributes model,
			org.springframework.security.core.Authentication auth) {
		Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
		Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
		if (detalleOrd.getId() != null) {
			serviceDetallesOrdenes.eliminarPorId(detalleOrd.getId());
			model.addFlashAttribute("msg", "El item se eliminó correctamente del carrito.");
		} else {
			if (!serviceDetallesOrdenes.obtenerDetalles().isEmpty()) {
				serviceDetallesOrdenes.eliminarPorIdCliente(cliente.getId());
				model.addFlashAttribute("msg", "Se eliminaron correctamente todos los items del carrito.");
			}
		}
		return "redirect:/carrito/";
	}

	@PostMapping("/agregar")
	public String agregarCarrito(@RequestParam("idProducto") Integer idProducto,
			@RequestParam("cantidad") Integer cantidad, RedirectAttributes model,
			org.springframework.security.core.Authentication auth) {
		Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
		Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = serviceProductos.buscarPorId(idProducto);
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecioKg());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(Double.parseDouble(df.format(producto.getPrecioKg() * cantidad)));
		detalleOrden.setProducto(producto);
		detalleOrden.setCliente(cliente);
		boolean ingresado = serviceDetallesOrdenes.buscarPorCliente(cliente).stream()
				.anyMatch(p -> p.getProducto().getId() == idProducto);
		if (!ingresado) {
			serviceDetallesOrdenes.agregarDetalle(detalleOrden);
			model.addFlashAttribute("msg", "El item se agregó correctamente al carrito.");
		} else {
			for (DetalleOrden dorden : serviceDetallesOrdenes.buscarPorCliente(cliente)) {
				if (dorden.getProducto().getId().compareTo(idProducto) == 0) {
					if((cantidad+dorden.getCantidad()) > dorden.getProducto().getCantidadIngreso()) {
						model.addFlashAttribute("msg2", "Ya tienes la cantidad límite de compra para este producto.");
						return "redirect:/carrito/";
					} else {
						detalleOrden.setId(dorden.getId());
						detalleOrden.setCantidad(cantidad + dorden.getCantidad());
						detalleOrden.setTotal(Double.parseDouble(df.format(producto.getPrecioKg() * (cantidad + dorden.getCantidad()))));
						serviceDetallesOrdenes.agregarDetalle(detalleOrden);
						model.addFlashAttribute("msg", "El item se agregó correctamente al carrito.");
					}
				}
			}
		}
		return "redirect:/carrito/";
	}

	@GetMapping("/")
	public String carrito(Model model, org.springframework.security.core.Authentication auth) {
		Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
		Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
		double sumaTotal = serviceDetallesOrdenes.buscarPorCliente(cliente).stream().mapToDouble(dt -> dt.getTotal())
				.sum();
		model.addAttribute("carrito", serviceDetallesOrdenes.buscarPorCliente(cliente));
		model.addAttribute("items", contarItems(cliente.getUsername()));
		model.addAttribute("orden", sumaTotal);
		model.addAttribute("cliente", cliente);
		model.addAttribute("iva", df.format(sumaTotal * .16));
		model.addAttribute("totalIva", df.format(sumaTotal * .16 + sumaTotal));
		return "carrito";
	}
}