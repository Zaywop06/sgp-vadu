package org.ud2.developers.SGPVADU.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Productos")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String descripcion;
	private Integer estatus;
	private LocalDate fechaIngreso = LocalDate.now();
	private Double precioKg;
	private Double cantidadIngreso;
	private Integer tempAlmacen = 0;
	private Integer vidaUtil = 0;
	private String imagen = "img0.png";

	@OneToOne
	@JoinColumn(name = "idCategoria")
	private Categoria categoria;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Double getPrecioKg() {
		return precioKg;
	}

	public void setPrecioKg(Double precioKg) {
		this.precioKg = precioKg;
	}

	public Double getCantidadIngreso() {
		return cantidadIngreso;
	}

	public void setCantidadIngreso(Double cantidadIngreso) {
		this.cantidadIngreso = cantidadIngreso;
	}

	public Integer getTempAlmacen() {
		return tempAlmacen;
	}

	public void setTempAlmacen(Integer tempAlmacen) {
		this.tempAlmacen = tempAlmacen;
	}

	public Integer getVidaUtil() {
		return vidaUtil;
	}

	public void setVidaUtil(Integer vidaUtil) {
		this.vidaUtil = vidaUtil;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", estatus=" + estatus
				+ ", fechaIngreso=" + fechaIngreso + ", precioKg=" + precioKg + ", cantidadIngreso=" + cantidadIngreso
				+ ", tempAlmacen=" + tempAlmacen + ", vidaUtil=" + vidaUtil + ", imagen=" + imagen + ", categoria="
				+ categoria + "]";
	}

}
