package org.ud2.developers.SGPVADU.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ud2.developers.SGPVADU.entity.Categoria;
import org.ud2.developers.SGPVADU.service.IntServiceCategorias;
import org.ud2.developers.SGPVADU.util.ExporterExcel;
import org.ud2.developers.SGPVADU.util.ExporterPDF;
import org.ud2.developers.SGPVADU.util.reports.CategoriasExporterExcel;
import org.ud2.developers.SGPVADU.util.reports.CategoriasExporterPDF;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/categorias")
public class CategoriasController {

	@Autowired
	private IntServiceCategorias serviceCategorias;

	@GetMapping("/exportarExcel")
	public void exportarListadoDeCategoriasEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		ExporterExcel exEx = new ExporterExcel();
		List<Categoria> categorias = serviceCategorias.obtenerCategorias();
		exEx.exportarListadoEnExcel(response, "Listado_Categoría", categorias);
		CategoriasExporterExcel exporter = new CategoriasExporterExcel(categorias);
		exporter.exportar(response);
	}

	@GetMapping("/exportarPDF")
	public void exportarListadoDeClientesEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		ExporterPDF exPDF = new ExporterPDF();
		List<Categoria> categorias = serviceCategorias.obtenerCategorias();
		exPDF.exportarListadoEnPDF(response, "Listado_Categoría", categorias);
		CategoriasExporterPDF exporter = new CategoriasExporterPDF(categorias);
		exporter.exportar(response);
	}
	
	@GetMapping("/buscar")
	public String modificarCategoria(@RequestParam("id") int idCategoria, Model model) {
		Categoria categoria = serviceCategorias.buscarPorId(idCategoria);
		model.addAttribute("categoria", categoria);
		return "categorias/formCategoria";
	}

	@GetMapping("/eliminar")
	public String eliminarCategoria(Categoria categoria, RedirectAttributes model) {
		serviceCategorias.eliminarPorId(categoria.getId());
		model.addFlashAttribute("msg", "La información de la categoría ha sido eliminada correctamente.");
		return "redirect:/categorias/indexPaginado";
	}

	@PostMapping("/agregar")
	public String agregarCategoria(Categoria categoria, RedirectAttributes model) {
		if (categoria.getId() == null)
			model.addFlashAttribute("msg", "La información de la categoría ha sido agregada correctamente.");
		else
			model.addFlashAttribute("msg", "La información de la categoría ha sido modificada correctamente.");
		serviceCategorias.agregarCategoria(categoria);
		return "redirect:/categorias/indexPaginado";
	}

	@GetMapping("/nueva")
	public String mostrarFormCategoria(Categoria categoria) {
		return "categorias/formCategoria";
	}

	@GetMapping(value = "/indexPaginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Categoria> categorias = serviceCategorias.buscarTodas(page);
		model.addAttribute("categorias", categorias);
		model.addAttribute("total", serviceCategorias.contarCategorias());
		return "categorias/listaCategorias";
	}
}
