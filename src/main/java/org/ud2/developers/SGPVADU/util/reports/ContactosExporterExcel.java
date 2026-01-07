package org.ud2.developers.SGPVADU.util.reports;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ud2.developers.SGPVADU.entity.Contacto;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ContactosExporterExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Contacto> listaContactos;

	public ContactosExporterExcel(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Msj's De Contacto");
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
		celda.setCellValue("Nombre del remitente");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("E-mail");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("Asunto");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("Mensaje");
		celda.setCellStyle(estilo);

		celda = fila.createCell(5);
		celda.setCellValue("Fecha de Emisión");
		celda.setCellStyle(estilo);

		celda = fila.createCell(6);
		celda.setCellValue("Estatus");
		celda.setCellStyle(estilo);
	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Contacto contacto : listaContactos) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(contacto.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(contacto.getNombre());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(contacto.getEmail());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(contacto.getAsunto());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(contacto.getMensaje());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);

			celda = fila.createCell(5);
			celda.setCellValue(contacto.getFecha().toString());
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);

			celda = fila.createCell(6);
			if(contacto.getEstatus() == 1) celda.setCellValue("Leído");
			else celda.setCellValue("No Leído");
			hoja.autoSizeColumn(6);
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
