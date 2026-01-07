package org.ud2.developers.SGPVADU.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.service.IntServiceClientes;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;
import org.ud2.developers.SGPVADU.util.ExporterExcel;
import org.ud2.developers.SGPVADU.util.ExporterPDF;
import org.ud2.developers.SGPVADU.util.reports.ClientesExporterExcel;
import org.ud2.developers.SGPVADU.util.reports.ClientesExporterPDF;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

	@Autowired
	private IntServiceClientes serviceClientes;

	@Autowired
	public IntServiceUsuarios serviceUsuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/exportarExcel")
	public void exportarListadoDeClientesEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		ExporterExcel exEx = new ExporterExcel();
		List<Cliente> clientes = serviceClientes.obtenerClientes();
		exEx.exportarListadoEnExcel(response, "Listado_Cliente", clientes);
		ClientesExporterExcel exporter = new ClientesExporterExcel(clientes);
		exporter.exportar(response);
	}

	@GetMapping("/exportarPDF")
	public void exportarListadoDeClientesEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		ExporterPDF exPDF = new ExporterPDF();
		List<Cliente> clientes = serviceClientes.obtenerClientes();
		exPDF.exportarListadoEnPDF(response, "Listado_Cliente", clientes);
		ClientesExporterPDF exporter = new ClientesExporterPDF(clientes);
		exporter.exportar(response);
	}

	@GetMapping("/buscar")
	public String modificarEmpleado(@RequestParam("id") int idCliente, Model model) {
		Cliente cliente = serviceClientes.buscarPorId(idCliente);
		model.addAttribute("cliente", cliente);
		return "clientes/formCliente";
	}

	@GetMapping("/eliminar")
	public String eliminarCliente(@RequestParam("id") int idCliente, RedirectAttributes model) {
		Cliente cliente = serviceClientes.buscarPorId(idCliente);
		serviceClientes.eliminarPorId(idCliente);
		serviceUsuarios.eliminarPorId(cliente.getUsuario().getId());
		model.addFlashAttribute("msg", "La información del cliente ha sido agregada correctamente.");
		return "redirect:/clientes/indexPaginado";
	}

	@PostMapping("/agregar")
	public String agregarEmpleado(Cliente cliente, RedirectAttributes model) {
		if (cliente.getId() == null) {
			Usuario usuario = new Usuario();
			usuario.setNombre(cliente.getNombre());
			usuario.setApellidoPaterno(cliente.getApellidoPaterno());
			usuario.setApellidoMaterno(cliente.getApellidoMaterno());
			usuario.setUsername(cliente.getUsername());
			usuario.setEmail(cliente.getEmail());
			usuario.setPassword(passwordEncoder.encode(cliente.getPassword()));
			usuario.setEstatus(1);
			Perfil perfil = new Perfil();
			perfil.setId(3);
			usuario.agregar(perfil);
			serviceUsuarios.agregarUsuario(usuario);
			cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
			cliente.setUsuario(usuario);
			model.addFlashAttribute("msg", "La información del cliente ha sido agregada correctamente.");
		} else {
			Usuario usuario = serviceUsuarios.buscarPorId(cliente.getUsuario().getId());
			usuario.setUsername(cliente.getUsername());
			usuario.setEmail(cliente.getEmail());
			serviceUsuarios.agregarUsuario(usuario);
			model.addFlashAttribute("msg", "La información del cliente ha sido modificada correctamente.");
		}
		serviceClientes.agregarCliente(cliente);
		return "redirect:/clientes/indexPaginado";
	}

	@GetMapping("/nuevo")
	public String mostrarFormCliente(Cliente cliente) {
		return "clientes/formCliente";
	}

	@GetMapping(value = "/indexPaginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Cliente> clientes = serviceClientes.buscarTodas(page);
		model.addAttribute("clientes", clientes);
		model.addAttribute("total", serviceClientes.contarClientes());
		return "clientes/listaClientes";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			}

			@Override
			public String getAsText() throws IllegalArgumentException {
				return DateTimeFormatter.ofPattern("dd-MM-yyyy").format((LocalDate) getValue());
			}
		});
	}
}
