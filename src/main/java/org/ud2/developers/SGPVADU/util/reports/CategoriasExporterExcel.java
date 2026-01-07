package org.ud2.developers.SGPVADU.util.reports;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ud2.developers.SGPVADU.entity.Categoria;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class CategoriasExporterExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Categoria> listaCategorias;

	public CategoriasExporterExcel(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Categorías");
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
	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Categoria categoria : listaCategorias) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(categoria.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(categoria.getNombre());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(categoria.getDescripcion());
			hoja.autoSizeColumn(2);
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
