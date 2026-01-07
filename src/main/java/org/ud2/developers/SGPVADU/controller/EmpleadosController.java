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
import org.ud2.developers.SGPVADU.entity.Empleado;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.service.IntServiceEmpleados;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;
import org.ud2.developers.SGPVADU.util.ExporterExcel;
import org.ud2.developers.SGPVADU.util.ExporterPDF;
import org.ud2.developers.SGPVADU.util.reports.EmpleadosExporterExcel;
import org.ud2.developers.SGPVADU.util.reports.EmpleadosExporterPDF;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/empleados")
public class EmpleadosController {

	@Autowired
	private IntServiceEmpleados serviceEmpleados;

	@Autowired
	public IntServiceUsuarios serviceUsuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/exportarExcel")
	public void exportarListadoDeEmpleadosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		ExporterExcel exEx = new ExporterExcel();
		List<Empleado> empleados = serviceEmpleados.obtenerEmpleados();
		exEx.exportarListadoEnExcel(response, "Listado_Empleado", empleados);
		EmpleadosExporterExcel exporter = new EmpleadosExporterExcel(empleados);
		exporter.exportar(response);
	}

	@GetMapping("/exportarPDF")
	public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		ExporterPDF exPDF = new ExporterPDF();
		List<Empleado> empleados = serviceEmpleados.obtenerEmpleados();
		exPDF.exportarListadoEnPDF(response, "Listado_Empleado", empleados);
		EmpleadosExporterPDF exporter = new EmpleadosExporterPDF(empleados);
		exporter.exportar(response);
	}

	@GetMapping("/buscar")
	public String modificarEmpleado(@RequestParam("id") int idEmpleado, Model model) {
		Empleado empleado = serviceEmpleados.buscarPorId(idEmpleado);
		model.addAttribute("empleado", empleado);
		return "empleados/formEmpleado";
	}

	@GetMapping("/eliminar")
	public String eliminarEmpleado(@RequestParam("id") int idEmpleado, RedirectAttributes model) {
		Empleado empleado = serviceEmpleados.buscarPorId(idEmpleado);
		serviceEmpleados.eliminarPorId(idEmpleado);
		serviceUsuarios.eliminarPorId(empleado.getUsuario().getId());
		model.addFlashAttribute("msg", "La información del empleado ha sido eliminada correctamente.");
		return "redirect:/empleados/indexPaginado";
	}

	@PostMapping("/agregar")
	public String agregarEmpleado(Empleado empleado, RedirectAttributes model) {
		if (empleado.getId() == null) {
			Usuario usuario = new Usuario();
			usuario.setNombre(empleado.getNombre());
			usuario.setApellidoPaterno(empleado.getApellidoPaterno());
			usuario.setApellidoMaterno(empleado.getApellidoMaterno());
			usuario.setUsername(empleado.getUsername());
			usuario.setEmail(empleado.getEmail());
			usuario.setPassword(passwordEncoder.encode(empleado.getPassword()));
			usuario.setEstatus(1);
			Perfil perfil = new Perfil();
			perfil.setId(2);
			usuario.agregar(perfil);
			serviceUsuarios.agregarUsuario(usuario);
			empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
			empleado.setUsuario(usuario);
			model.addFlashAttribute("msg", "La información del empleado ha sido agregada correctamente.");
		} else {
			Usuario usuario = serviceUsuarios.buscarPorId(empleado.getUsuario().getId());
			usuario.setUsername(empleado.getUsername());
			usuario.setEmail(empleado.getEmail());
			serviceUsuarios.agregarUsuario(usuario);
			model.addFlashAttribute("msg", "La información del empleado ha sido modificada correctamente.");
		}
		serviceEmpleados.agregarEmpleado(empleado);
		return "redirect:/empleados/indexPaginado";
	}

	@GetMapping("/nuevo")
	public String mostrarFormEmpleado(Empleado empleado) {
		return "empleados/formEmpleado";
	}

	@GetMapping(value = "/indexPaginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Empleado> empleados = serviceEmpleados.buscarTodas(page);
		model.addAttribute("empleados", empleados);
		model.addAttribute("total", serviceEmpleados.contarEmpleados());
		return "empleados/listaEmpleados";
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
