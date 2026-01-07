package org.ud2.developers.SGPVADU.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

public class ExporterExcel {

	public void exportarListadoEnExcel(HttpServletResponse response, String entity, List<?> lista)
			throws DocumentException, IOException {
		response.setContentType("application/octet-stream");

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=" + entity + "s_" + fechaActual + ".xlsx";

		response.setHeader(cabecera, valor);
	}

}
