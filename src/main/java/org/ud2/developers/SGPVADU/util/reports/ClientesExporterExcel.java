package org.ud2.developers.SGPVADU.util.reports;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ud2.developers.SGPVADU.entity.Cliente;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ClientesExporterExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Cliente> listaClientes;

	public ClientesExporterExcel(List<Cliente> listaEmpleados) {
		this.listaClientes = listaEmpleados;
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
		celda.setCellValue("Nombre(s)");
		celda.setCellStyle(estilo);

		celda = fila.createCell(2);
		celda.setCellValue("Ap. Paterno");
		celda.setCellStyle(estilo);

		celda = fila.createCell(3);
		celda.setCellValue("Ap. Materno");
		celda.setCellStyle(estilo);

		celda = fila.createCell(4);
		celda.setCellValue("Teléfono");
		celda.setCellStyle(estilo);

		celda = fila.createCell(5);
		celda.setCellValue("E-mail");
		celda.setCellStyle(estilo);

		/*celda = fila.createCell(6);
		celda.setCellValue("Sexo");
		celda.setCellStyle(estilo);*/

		celda = fila.createCell(6);
		celda.setCellValue("Calle");
		celda.setCellStyle(estilo);

		celda = fila.createCell(7);
		celda.setCellValue("Municipio");
		celda.setCellStyle(estilo);

		celda = fila.createCell(8);
		celda.setCellValue("Colonia");
		celda.setCellStyle(estilo);

		celda = fila.createCell(9);
		celda.setCellValue("Ciudad");
		celda.setCellStyle(estilo);

		celda = fila.createCell(10);
		celda.setCellValue("Estado");
		celda.setCellStyle(estilo);

		celda = fila.createCell(11);
		celda.setCellValue("C.P.");
		celda.setCellStyle(estilo);

		celda = fila.createCell(12);
		celda.setCellValue("Fecha de Registro");
		celda.setCellStyle(estilo);
	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Cliente cliente : listaClientes) {
			Row fila = hoja.createRow(numeroFilas++);

			Cell celda = fila.createCell(0);
			celda.setCellValue(cliente.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(cliente.getNombre());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(cliente.getApellidoPaterno());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(cliente.getApellidoMaterno());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(cliente.getTelefono());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);

			celda = fila.createCell(5);
			celda.setCellValue(cliente.getEmail());
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);

			celda = fila.createCell(6);
			celda.setCellValue(cliente.getCalle());
			hoja.autoSizeColumn(6);
			celda.setCellStyle(estilo);

			celda = fila.createCell(7);
			celda.setCellValue(cliente.getMunicipio());
			hoja.autoSizeColumn(7);
			celda.setCellStyle(estilo);

			celda = fila.createCell(8);
			celda.setCellValue(cliente.getColonia());
			hoja.autoSizeColumn(8);
			celda.setCellStyle(estilo);

			celda = fila.createCell(9);
			celda.setCellValue(cliente.getCiudad());
			hoja.autoSizeColumn(9);
			celda.setCellStyle(estilo);

			celda = fila.createCell(10);
			celda.setCellValue(cliente.getEstado());
			hoja.autoSizeColumn(10);
			celda.setCellStyle(estilo);

			celda = fila.createCell(11);
			celda.setCellValue(cliente.getCp());
			hoja.autoSizeColumn(11);
			celda.setCellStyle(estilo);

			celda = fila.createCell(12);
			celda.setCellValue(cliente.getFechaRegistro().toString());
			hoja.autoSizeColumn(12);
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
