package org.ud2.developers.SGPVADU.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.ud2.developers.SGPVADU.entity.Categoria;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.entity.Producto;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.service.IntServiceCategorias;
import org.ud2.developers.SGPVADU.service.IntServiceClientes;
import org.ud2.developers.SGPVADU.service.IntServiceDetallesOrdenes;
import org.ud2.developers.SGPVADU.service.IntServiceOrdenes;
import org.ud2.developers.SGPVADU.service.IntServiceProductos;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

	@Autowired
	private IntServiceProductos serviceProductos;

	@Autowired
	private IntServiceCategorias serviceCategorias;

	@Autowired
	private IntServiceUsuarios serviceUsuarios;

	@Autowired
	private IntServiceClientes serviceClientes;

	@Autowired
	private IntServiceOrdenes serviceOrdenes;

	@Autowired
	private IntServiceDetallesOrdenes serviceDetallesOrdenes;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CarritoController carritoCtrl;

	@PostMapping("/busqueda")
	public String buscarPorCategoria(Model model, Pageable page, String descripcion, Integer idCategoria,
			org.springframework.security.core.Authentication auth, Categoria categoria) {
		Integer id = 0;
		if (descripcion.equals("") && idCategoria != null) {
			/*Page<Producto> productos = serviceProductos.buscarTodasPorEstatusYCategoria(page, idCategoria);
			model.addAttribute("productos", productos);
			model.addAttribute("categoria", cat);*/
			Categoria cat = serviceCategorias.buscarPorId(idCategoria);
			model.addAttribute("categoria", cat);
			model.addAttribute("productos", serviceProductos.buscarPorCategoria(idCategoria));
		} else if (!descripcion.equals("") && idCategoria == null) {
			/*Page<Producto> productos = serviceProductos.buscarTodasPorEstatusYDescripcion(page, descripcion);
			model.addAttribute("productos", productos);*/
			model.addAttribute("productos", serviceProductos.buscarPorDescripcion(descripcion));
		} else if (descripcion.equals("") && idCategoria == null) {
			return "redirect:/";
		} else {
			/*Page<Producto> productos = serviceProductos.buscarTodasPorEstatusYDescripcionYCategoria(page, descripcion, idCategoria);
			model.addAttribute("productos", productos);*/
			Categoria cat = serviceCategorias.buscarPorId(idCategoria);
			model.addAttribute("categoria", cat);
			model.addAttribute("productos",
					serviceProductos.buscarTodasPorDescripcionYCategoria(descripcion, idCategoria));
		}
		if (auth != null && auth.getAuthorities().toString().equals("[Cliente]")) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
			Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
			model.addAttribute("descripcion", descripcion);
			model.addAttribute("items", carritoCtrl.contarItems(cliente.getUsername()));
			model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
			model.addAttribute("id", id);
			return "home";
		}
		model.addAttribute("descripcion", descripcion);
		model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
		model.addAttribute("id", id);
		return "home";
	}

	@GetMapping("/contactanos")
	public String contactanos(Model model, org.springframework.security.core.Authentication auth) {
		if (auth != null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
			Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
			model.addAttribute("items", carritoCtrl.contarItems(cliente.getUsername()));
		}
		return "contacto";
	}

	@GetMapping("/acerca")
	public String acerca(Model model, org.springframework.security.core.Authentication auth) {
		if (auth != null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
			Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
			model.addAttribute("items", carritoCtrl.contarItems(cliente.getUsername()));
		}
		return "acerca";
	}

	@GetMapping("/historialOrdenes")
	public String mostrarHistorialOrdenes(Model model, org.springframework.security.core.Authentication auth) {
		Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
		Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
		model.addAttribute("detalles", serviceDetallesOrdenes.obtenerDetalles());
		model.addAttribute("ordenes", serviceOrdenes.buscarPorUsuario(usuario));
		model.addAttribute("cliente", cliente);
		model.addAttribute("items", carritoCtrl.contarItems(cliente.getUsername()));
		return "historialOrdenes";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}

	@PostMapping("/registrar")
	public String guardarUsuario(Usuario usuario, BindingResult bindingResult, Model model) {
		Cliente cliente = new Cliente();
		cliente.setNombre(usuario.getNombre());
		cliente.setApellidoPaterno(usuario.getApellidoPaterno());
		cliente.setApellidoMaterno(usuario.getApellidoMaterno());
		cliente.setUsername(usuario.getUsername());
		cliente.setEmail(usuario.getEmail());
		if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Las contraseñas no coinciden");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("usuario", usuario);
			return "formRegistro";
		}
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuario.setConfirmPassword(passwordEncoder.encode(usuario.getConfirmPassword()));
		cliente.setPassword(usuario.getPassword());
		cliente.setFechaRegistro(usuario.getFechaRegistro());
		usuario.setEstatus(1);
		Perfil perfil = new Perfil();
		perfil.setId(3);
		usuario.agregar(perfil);
		cliente.setUsuario(usuario);
		serviceUsuarios.agregarUsuario(usuario);
		serviceClientes.agregarCliente(cliente);
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String mostrarFormRegistro(Usuario usuario) {
		return "formRegistro";
	}

	@GetMapping("/login")
	public String mostrarFormLogin() {
		return "formLogin";
	}

	@GetMapping(value = "/")
	public String mostrarIndex(Model model, Pageable page, org.springframework.security.core.Authentication auth,
			Categoria categoria) {
		if (auth != null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
			Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
			for (Perfil perfil : usuario.getPerfiles()) {
				if (perfil.getPerfil().compareTo("Cliente") == 0) {
					Page<Producto> productos = serviceProductos.buscarTodasEnVenta(page);
					model.addAttribute("items", carritoCtrl.contarItems(cliente.getUsername()));
					model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
					model.addAttribute("productos", productos);
					return "home";
				}
			}
		}
		Page<Producto> productos = serviceProductos.buscarTodasEnVenta(page);
		model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
		model.addAttribute("productos", productos);
		return "home";
	}
}
