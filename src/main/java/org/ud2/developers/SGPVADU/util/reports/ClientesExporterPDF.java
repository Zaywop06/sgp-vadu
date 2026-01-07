package org.ud2.developers.SGPVADU.util.reports;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ud2.developers.SGPVADU.entity.Cliente;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

public class ClientesExporterPDF {

	private List<Cliente> listaClientes;

	public ClientesExporterPDF(List<Cliente> listaClientes) {
		super();
		this.listaClientes = listaClientes;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.decode("#c62020"));
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Nombre(s)", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Ape. Paterno", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Ape. Materno", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Teléfono", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("E-mail", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Calle", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Municipio", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Colonia", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Ciudad", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Estado", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("C.P.", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Fecha de Registro", fuente));
		tabla.addCell(celda);
	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Cliente cliente : listaClientes) {
			tabla.addCell(String.valueOf(cliente.getId()));
			tabla.addCell(cliente.getNombre());
			tabla.addCell(cliente.getApellidoPaterno());
			tabla.addCell(cliente.getApellidoMaterno());
			tabla.addCell(String.valueOf(cliente.getTelefono()));
			tabla.addCell(cliente.getEmail());
			tabla.addCell(cliente.getCalle());
			tabla.addCell(cliente.getMunicipio());
			tabla.addCell(cliente.getColonia());
			tabla.addCell(cliente.getCiudad());
			tabla.addCell(cliente.getEstado());
			tabla.addCell(cliente.getCp());
			tabla.addCell(cliente.getFechaRegistro().toString());
		}
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4);
		PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		// Encabezado
        Paragraph dateParagraph = new Paragraph("Reporte generado el " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        documento.add(dateParagraph);
        
        // Logo
        URL imageUrl = new URL("https://sgp-vadu.up.railway.app/images/logo.png");
        Image image = Image.getInstance(imageUrl);
        image.scalePercent(20);
        image.setAbsolutePosition(250, 635);
        documento.add(image);
        
		Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		fuente.setColor(Color.BLACK);
		fuente.setSize(18);

		Paragraph titulo = new Paragraph("LISTADO DE CLIENTES", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		titulo.setSpacingBefore(165);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(13);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 0.5f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f });
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}
