package org.ud2.developers.SGPVADU.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ud2.developers.SGPVADU.entity.Cliente;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.entity.Producto;
import org.ud2.developers.SGPVADU.entity.Usuario;
import org.ud2.developers.SGPVADU.service.IntServiceCategorias;
import org.ud2.developers.SGPVADU.service.IntServiceClientes;
import org.ud2.developers.SGPVADU.service.IntServiceProductos;
import org.ud2.developers.SGPVADU.service.IntServiceUsuarios;
import org.ud2.developers.SGPVADU.util.ExporterExcel;
import org.ud2.developers.SGPVADU.util.ExporterPDF;
import org.ud2.developers.SGPVADU.util.Utileria;
import org.ud2.developers.SGPVADU.util.reports.ProductosExporterExcel;
import org.ud2.developers.SGPVADU.util.reports.ProductosExporterPDF;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/productos")
public class ProductosController {

	@Autowired
	private IntServiceProductos serviceProductos;

	@Autowired
	private IntServiceCategorias serviceCategorias;

	@Autowired
	private IntServiceUsuarios serviceUsuarios;

	@Autowired
	private IntServiceClientes serviceClientes;

	@Autowired
	private CarritoController carritoCtrl;

	@Autowired
	private Utileria util;

	@GetMapping("/exportarExcel")
	public void exportarListadoDeProductosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		ExporterExcel exEx = new ExporterExcel();
		List<Producto> productos = serviceProductos.obtenerProductos();
		exEx.exportarListadoEnExcel(response, "Listado_Producto", productos);
		ProductosExporterExcel exporter = new ProductosExporterExcel(productos);
		exporter.exportar(response);
	}

	@GetMapping("/exportarPDF")
	public void exportarListadoDeProductosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		ExporterPDF exPDF = new ExporterPDF();
		List<Producto> productos = serviceProductos.obtenerProductos();
		exPDF.exportarListadoEnPDF(response, "Listado_Producto", productos);
		ProductosExporterPDF exporter = new ProductosExporterPDF(productos);
		exporter.exportar(response);
	}

	@GetMapping("/detalle")
	public String consultarDetalleProducto(@RequestParam("id") int idProducto, Model model,
			org.springframework.security.core.Authentication auth) {
		Producto producto = serviceProductos.buscarPorId(idProducto);
		model.addAttribute("producto", producto);
		if (auth != null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(auth.getName());
			Cliente cliente = serviceClientes.buscarPorUsuario(usuario);
			for (Perfil perfil : usuario.getPerfiles()) {
				if (perfil.getPerfil().compareTo("Cliente") == 0) {
					model.addAttribute("items", carritoCtrl.contarItems(cliente.getUsername()));
					return "productos/detalle";
				}
			}
		}
		return "productos/detalle";
	}

	@GetMapping("/buscar")
	public String modificarProducto(@RequestParam("id") int idProducto, Model model) {
		Producto producto = serviceProductos.buscarPorId(idProducto);
		model.addAttribute("producto", producto);
		model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
		return "productos/formProducto";
	}

	@GetMapping("/eliminar")
	public String eliminarProducto(Producto producto, RedirectAttributes model) {
		serviceProductos.eliminarPorId(producto.getId());
		model.addFlashAttribute("msg", "La información del producto ha sido eliminada correctamente.");
		return "redirect:/productos/indexPaginado";
	}

	@PostMapping("/agregar")
	public String agregarProducto(Producto producto, BindingResult result, Model model, RedirectAttributes model2,
			@RequestParam("archivoImagen") MultipartFile file) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			model.addAttribute("categorias", serviceProductos.obtenerProductos());
			return "productos/formProducto";
		}
		if (producto.getId() == null) {
			if (!file.isEmpty()) {
				String fileName = util.uploadImage(file);
				if (fileName != null) {
					producto.setImagen(fileName);
				}
			}
			model2.addFlashAttribute("msg", "La información del producto ha sido agregada correctamente.");
		} else {
			if (!file.isEmpty()) {
				String fileName = util.uploadImage(file);
				if (fileName != null) {
					producto.setImagen(fileName);
					model2.addFlashAttribute("msg", "La información del producto ha sido modificada correctamente.");
				}
			} else {
				Producto p = serviceProductos.buscarPorId(producto.getId());
				producto.setImagen(p.getImagen());
			}
		}
		serviceProductos.agregarProducto(producto);
		return "redirect:/productos/indexPaginado";
	}

	@GetMapping("/nuevo")
	public String mostrarFormProducto(Producto producto, Model model) {
		model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
		return "productos/formProducto";
	}

	@GetMapping(value = "/indexPaginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Producto> productos = serviceProductos.buscarTodas(page);
		model.addAttribute("productos", productos);
		model.addAttribute("total", serviceProductos.contarProductos());
		return "productos/listaProductos";
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
