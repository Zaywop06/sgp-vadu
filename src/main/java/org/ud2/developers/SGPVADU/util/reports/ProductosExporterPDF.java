package org.ud2.developers.SGPVADU.util.reports;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ud2.developers.SGPVADU.entity.Producto;

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

public class ProductosExporterPDF {

	private List<Producto> listaProductos;

	public ProductosExporterPDF(List<Producto> listaProductos) {
		super();
		this.listaProductos = listaProductos;
	}

	private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
		PdfPCell celda = new PdfPCell();

		celda.setBackgroundColor(Color.decode("#c62020"));
		celda.setPadding(5);

		Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
		fuente.setColor(Color.WHITE);

		celda.setPhrase(new Phrase("ID", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Nombre", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Descripción", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Categoría", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Fecha Ingreso", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Precio Kg", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Estatus", fuente));
		tabla.addCell(celda);

		celda.setPhrase(new Phrase("Stock", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Temperatura Almacen", fuente));
		tabla.addCell(celda);
		
		celda.setPhrase(new Phrase("Vida Util", fuente));
		tabla.addCell(celda);
	}

	private void escribirDatosDeLaTabla(PdfPTable tabla) {
		for (Producto producto : listaProductos) {
			tabla.addCell(String.valueOf(producto.getId()));
			tabla.addCell(producto.getNombre());
			tabla.addCell(producto.getDescripcion());
			tabla.addCell(String.valueOf(producto.getCategoria().getNombre()));
			tabla.addCell(String.valueOf(producto.getFechaIngreso()));
			tabla.addCell(String.valueOf("$" + producto.getPrecioKg()));
			if(producto.getEstatus() == 1) tabla.addCell("En Venta");
			else tabla.addCell("Descontinuado");
			tabla.addCell(String.valueOf(producto.getCantidadIngreso() + " Kg"));
			tabla.addCell(String.valueOf(producto.getTempAlmacen() + " C°"));
			tabla.addCell(String.valueOf(producto.getVidaUtil() + " días"));
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

		Paragraph titulo = new Paragraph("LISTADO DE PRODUCTOS", fuente);
		titulo.setAlignment(Paragraph.ALIGN_CENTER);
		titulo.setSpacingBefore(165);
		documento.add(titulo);

		PdfPTable tabla = new PdfPTable(10);
		tabla.setWidthPercentage(100);
		tabla.setSpacingBefore(15);
		tabla.setWidths(new float[] { 1f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f});
		tabla.setWidthPercentage(110);

		escribirCabeceraDeLaTabla(tabla);
		escribirDatosDeLaTabla(tabla);

		documento.add(tabla);
		documento.close();
	}
}
