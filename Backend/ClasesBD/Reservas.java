package EstructuraBD;

public class Reservas {
	private String reserva_id, cliente_id, fecha_reserva, hora_reserva, mesa_id, estado, observaciones, fecha_creacion;

	
    public Reservas(String reserva_id, String cliente_id, String fecha_reserva, String hora_reserva, String mesa_id, String estado, String observaciones, String fecha_creacion) {
    	this.reserva_id = reserva_id;
    	this.cliente_id = cliente_id;
    	this.fecha_reserva = fecha_reserva;
    	this.hora_reserva = hora_reserva;
    	this.mesa_id = mesa_id;
    	this.estado = estado;
    	this.observaciones = observaciones;
    	this.fecha_creacion = fecha_creacion;

    }

	
	public String getFecha_reserva() {
		return fecha_reserva;
	}
	
	public void setFecha_reserva(String fecha_reserva) {
		this.fecha_reserva = fecha_reserva;
	}
	
	public String getMesa_id() {
		return mesa_id;
	}
	
	public void setMesa_id(String mesa_id) {
		this.mesa_id = mesa_id;
	}
	
	public String getFecha_creacion() {
		return fecha_creacion;
	}
	
	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getReserva_id() {
		return reserva_id;
	}
	
	public void setReserva_id(String reserva_id) {
		this.reserva_id = reserva_id;
	}
	
	public String getCliente_id() {
		return cliente_id;
	}
	
	public void setCliente_id(String cliente_id) {
		this.cliente_id = cliente_id;
	}
	
	public String getHora_reserva() {
		return hora_reserva;
	}
	
	public void setHora_reserva(String hora_reserva) {
		this.hora_reserva = hora_reserva;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
