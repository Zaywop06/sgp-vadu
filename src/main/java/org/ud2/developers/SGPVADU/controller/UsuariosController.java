package org.ud2.developers.SGPVADU.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;
import org.ud2.developers.SGPVADU.util.ExporterExcel;
import org.ud2.developers.SGPVADU.util.ExporterPDF;
import org.ud2.developers.SGPVADU.util.reports.UsuariosExporterExcel;
import org.ud2.developers.SGPVADU.util.reports.UsuariosExporterPDF;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	public IntServiceUsuarios serviceUsuarios;

	@GetMapping("/exportarExcel")
	public void exportarListadoDeUsuariosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		ExporterExcel exEx = new ExporterExcel();
		List<Usuario> usuarios = serviceUsuarios.obtenerUsuarios();
		exEx.exportarListadoEnExcel(response, "Listado_Usuario", usuarios);
		UsuariosExporterExcel exporter = new UsuariosExporterExcel(usuarios);
		exporter.exportar(response);
	}

	@GetMapping("/exportarPDF")
	public void exportarListadoDeUsuariosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		ExporterPDF exPDF = new ExporterPDF();
		List<Usuario> usuarios = serviceUsuarios.obtenerUsuarios();
		exPDF.exportarListadoEnPDF(response, "Listado_Usuario", usuarios);
		UsuariosExporterPDF exporter = new UsuariosExporterPDF(usuarios);
		exporter.exportar(response);
	}

	@GetMapping("/desbloquear")
	public String desbloquearUsuario(@RequestParam("id") int idUsuario, RedirectAttributes model) {
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
		usuario.setEstatus(1);
		serviceUsuarios.agregarUsuario(usuario);
		model.addFlashAttribute("msg", "Se le ha desbloqueado correctamente al usuario.");
		return "redirect:/usuarios/indexPaginado";
	}

	@GetMapping("/bloquear")
	public String bloquearUsuario(@RequestParam("id") int idUsuario, RedirectAttributes model) {
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
		usuario.setEstatus(0);
		serviceUsuarios.agregarUsuario(usuario);
		model.addFlashAttribute("msg", "Se le ha bloqueado correctamente al usuario.");
		return "redirect:/usuarios/indexPaginado";
	}

	@GetMapping("/eliminar")
	public String eliminarUsuario(Usuario usuario, RedirectAttributes model) {
		serviceUsuarios.eliminarPorId(usuario.getId());
		model.addFlashAttribute("msg", "La información del usuario ha sido eliminada correctamente.");
		return "redirect:/usuarios/indexPaginado";
	}

	@GetMapping(value = "/indexPaginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Perfil p = new Perfil();
		for (Usuario usuario : serviceUsuarios.obtenerUsuarios()) {
			p = usuario.getPerfiles().get(0);
		}
		model.addAttribute("perfil", p);
		Page<Usuario> usuarios = serviceUsuarios.buscarTodas(page);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("total", serviceUsuarios.contarUsuarios());
		return "usuarios/listaUsuarios";
	}
}
