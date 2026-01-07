package org.ud2.developers.SGPVADU.util.reports;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ud2.developers.SGPVADU.entity.Perfil;
import org.ud2.developers.SGPVADU.entity.Usuario;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UsuariosExporterExcel {

	private XSSFWorkbook libro;
	private XSSFSheet hoja;

	private List<Usuario> listaUsuarios;

	public UsuariosExporterExcel(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Usuarios");
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
		celda.setCellValue("Username");
		celda.setCellStyle(estilo);

		celda = fila.createCell(5);
		celda.setCellValue("E-mail");
		celda.setCellStyle(estilo);

		celda = fila.createCell(6);
		celda.setCellValue("Estatus");
		celda.setCellStyle(estilo);

		celda = fila.createCell(7);
		celda.setCellValue("Perfil(es)");
		celda.setCellStyle(estilo);

		celda = fila.createCell(8);
		celda.setCellValue("Fecha de Registro");
		celda.setCellStyle(estilo);
	}

	private void escribirDatosDeLaTabla() {
		int numeroFilas = 1;

		CellStyle estilo = libro.createCellStyle();
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeight(14);
		estilo.setFont(fuente);

		for (Usuario usuario : listaUsuarios) {
			Row fila = hoja.createRow(numeroFilas++);
			String perfiles = "";

			Cell celda = fila.createCell(0);
			celda.setCellValue(usuario.getId());
			hoja.autoSizeColumn(0);
			celda.setCellStyle(estilo);

			celda = fila.createCell(1);
			celda.setCellValue(usuario.getNombre());
			hoja.autoSizeColumn(1);
			celda.setCellStyle(estilo);

			celda = fila.createCell(2);
			celda.setCellValue(usuario.getApellidoPaterno());
			hoja.autoSizeColumn(2);
			celda.setCellStyle(estilo);

			celda = fila.createCell(3);
			celda.setCellValue(usuario.getApellidoMaterno());
			hoja.autoSizeColumn(3);
			celda.setCellStyle(estilo);

			celda = fila.createCell(4);
			celda.setCellValue(usuario.getUsername());
			hoja.autoSizeColumn(4);
			celda.setCellStyle(estilo);

			celda = fila.createCell(5);
			celda.setCellValue(usuario.getEmail());
			hoja.autoSizeColumn(5);
			celda.setCellStyle(estilo);

			celda = fila.createCell(6);
			if (usuario.getEstatus() == 1)
				celda.setCellValue("Activo");
			else
				celda.setCellValue("Bloqueado");
			hoja.autoSizeColumn(6);
			celda.setCellStyle(estilo);

			celda = fila.createCell(7);
			for (Perfil perfil : usuario.getPerfiles()) {
				perfiles += perfil.getPerfil() + " ";
			}
			celda.setCellValue(perfiles);
			hoja.autoSizeColumn(7);
			celda.setCellStyle(estilo);

			celda = fila.createCell(8);
			celda.setCellValue(usuario.getFechaRegistro().toString());
			hoja.autoSizeColumn(8);
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
