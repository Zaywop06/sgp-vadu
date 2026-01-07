package org.ud2.developers.SGPVADU.util.reports;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ud2.developers.SGPVADU.entity.Producto;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ProductosExporterExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Producto> listaProductos;

	public ProductosExporterExcel(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Empleados");
	}

	private void escribirCabeceraDeTabla() {
		Row fila = hoja.createRow(0);

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setBold(true);
		fuente.setFontHeight(16);
		estilo.setFont(fuente);

		Cell celda = fila.createCell(0);
		celda.setCellValue("ID");
		celda.setCellStyle(estilo);

		celda = fila.createCell(1);
		celda.setCellValue("Nombre");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("Descripción");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("Categoría");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("Fecha de Ingreso");
		celda.setCellStyle(estilo);

		celda = fila.createCell(5);
		celda.setCellValue("Precio Kg");
		celda.setCellStyle(estilo);

		celda = fila.createCell(6);
		celda.setCellValue("Estatus");
		celda.setCellStyle(estilo);

		celda = fila.createCell(7);
		celda.setCellValue("Stock");
		celda.setCellStyle(estilo);

		celda = fila.createCell(8);
		celda.setCellValue("Temp. de Almacen (C°)");
		celda.setCellStyle(estilo);
		
		celda = fila.createCell(9);
		celda.setCellValue("Vida Util (días)");
		celda.setCellStyle(estilo);
	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Producto producto : listaProductos) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(producto.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(producto.getNombre());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(producto.getDescripcion());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(String.valueOf(producto.getCategoria().getNombre()));
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(String.valueOf(producto.getFechaIngreso()));
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);

			celda = fila.createCell(5);
			celda.setCellValue("$" + producto.getPrecioKg());
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);

			celda = fila.createCell(6);
			if(producto.getEstatus() == 1) celda.setCellValue("En Venta");
			else celda.setCellValue("Descontinuado");
			hoja.autoSizeColumn(6);
			celda.setCellStyle(estilo);

			celda = fila.createCell(7);
			celda.setCellValue(producto.getCantidadIngreso() + " kg");
			hoja.autoSizeColumn(7);
			celda.setCellStyle(estilo);

			celda = fila.createCell(8);
			celda.setCellValue(producto.getTempAlmacen().toString() + " C°");
			hoja.autoSizeColumn(8);
			celda.setCellStyle(estilo);
			
			celda = fila.createCell(9);
			celda.setCellValue(producto.getVidaUtil().toString() + " días");
			hoja.autoSizeColumn(9);
			celda.setCellStyle(estilo);
		}
	}

	public void exportar(HttpServletResponse response) throws IOException {
		escribirCabeceraDeTabla();
		escribirDatosDeLaTabla();

		ServletOutputStream outPutStream = response.getOutputStream();
		libro.write(outPutStream);

		libro.close();
		outPutStream.close();
	}
}
