package com.jeduan.cfd;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.gob.sat.cfd.Comprobante;
import mx.gob.sat.cfd.TInformacionAduanera;
import mx.gob.sat.cfd.TUbicacion;
import mx.gob.sat.cfd.TUbicacionFiscal;
import mx.gob.sat.cfd.Comprobante.Emisor;
import mx.gob.sat.cfd.Comprobante.Impuestos;
import mx.gob.sat.cfd.Comprobante.Receptor;
import mx.gob.sat.cfd.Comprobante.Conceptos.Concepto;
import mx.gob.sat.cfd.Comprobante.Impuestos.Retenciones.Retencion;
import mx.gob.sat.cfd.Comprobante.Impuestos.Traslados.Traslado;

public class CadenaOriginalCreator {
	private StringBuilder sb;
	private final Comprobante c;
	private final String cadena;
	
	public static String createCadena(Comprobante c) {
		CadenaOriginalCreator creator = new CadenaOriginalCreator(c);
		return creator.getCadena();
	}

	private CadenaOriginalCreator(Comprobante c) {
		if (c == null)
			throw new NullPointerException("Comprobante is null");

		this.c = c;
		this.sb = new StringBuilder("||");

		this.cadena = cadenaOriginal();
	}

	public String getCadena() {
		return cadena;
	}

	private String cadenaOriginal() {
		// Datos SAT
		append(mx.gob.sat.cfd.Comprobante.VERSION_COMPROBANTE);
		append(c.getSerie());
		append(c.getFolio());
		append(c.getFecha());
		append(c.getNoAprobacion());
		append(c.getAnoAprobacion());
		append(c.getTipoDeComprobante());
		append(c.getFormaDePago());
		append(c.getCondicionesDePago(), true);
		append(c.getSubTotal());
		append(c.getDescuento(), true);
		append(c.getTotal());

		Emisor e = c.getEmisor();
		// Datos Emisor
		append(e.getRfc());
		append(e.getNombre());

		TUbicacionFiscal df = e.getDomicilioFiscal();
		append(df.getCalle());
		append(df.getNoExterior(), true);
		append(df.getNoInterior(), true);
		append(df.getColonia(), true);
		append(df.getLocalidad(), true);
		append(df.getReferencia(), true);
		append(df.getMunicipio());
		append(df.getEstado());
		append(df.getPais());
		append(df.getCodigoPostal());

		// Datos lugar emision
		if (null != c.getEmisor().getExpedidoEn()) {
			TUbicacion s = c.getEmisor().getExpedidoEn();
			append(s.getCalle());
			append(s.getNoExterior(), true);
			append(s.getNoInterior(), true);
			append(s.getColonia(), true);
			append(s.getLocalidad(), true);
			append(s.getReferencia(), true);
			append(s.getMunicipio(), true);
			append(s.getEstado(), true);
			append(s.getPais());
			append(s.getCodigoPostal());
		}

		// Datos receptor
		Receptor r = c.getReceptor();
		append(r.getRfc());
		append(r.getNombre());
		TUbicacion rd = r.getDomicilio();
		append(rd.getCalle());
		append(rd.getNoExterior(), true);
		append(rd.getNoInterior(), true);
		append(rd.getColonia(), true);
		append(rd.getLocalidad(), true);
		append(rd.getReferencia(), true);
		append(rd.getMunicipio(), true);
		append(rd.getEstado(), true);
		append(rd.getPais());
		append(rd.getCodigoPostal());

		// Datos informacion coneptos

		for (Concepto con : c.getConceptos().getConcepto()) {
			append(con.getCantidad());
			append(con.getUnidad(), true);
			append(con.getUnidad(), true);
			append(con.getNoIdentificacion(), true);
			append(con.getDescripcion());
			append(con.getValorUnitario());
			append(con.getImporte());
			if (!con.getInformacionAduanera().isEmpty()) {
				for (TInformacionAduanera info : con.getInformacionAduanera()) {
					append(info.getNumero());
					appendShortDate(info.getFecha());
					append(info.getAduana());
				}
			}
		}

		// Datos impuestos
		Impuestos i = c.getImpuestos();
		if (null != i.getRetenciones()) {
			List<Retencion> retenciones = i.getRetenciones().getRetencion();
			if (!retenciones.isEmpty()) {
				for (Retencion ret : retenciones) {
					append(ret.getImpuesto());
					append(ret.getImporte());
				}
				append(i.getTotalImpuestosRetenidos());
			}
		}
		if (null != i.getTraslados()) {
			List<Traslado> traslados = i.getTraslados().getTraslado();
			if (!traslados.isEmpty()) {
				for (Traslado t : traslados) {
					append(t.getImpuesto());
					append(t.getTasa());
					append(t.getImporte());
				}
				append(i.getTotalImpuestosTrasladados());
			}
		}

		sb.append("|");
		try {
			return new String(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			return sb.toString();
		}
	}

	private <T> void append(T value) {
		append(value, false);
	}

	private <T> void append(T value, boolean optional) {
		if (null == value || "".equals(value.toString())) {
			if (!optional)
				throw new IllegalArgumentException("El campo " + value
						+ " es requerido");
			else 
				return;
		}
		String data;
		if (value instanceof Date) {
			data = appendDate((Date) value);
		} else if (value instanceof BigDecimal) {
			data = appendBigDecimal((BigDecimal) value);
		} else {
			data = value.toString();
		}

		sb.append(data).append("|");
	}

	private String appendDate(Date value) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return df.format(value);
	}

	private String appendBigDecimal(BigDecimal value) {
		value = value.setScale(2, RoundingMode.HALF_EVEN);
		return value.toPlainString();
	}

	private void appendShortDate(Date value) {
		if (null == value)
			return;

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		append(df.format(value));
	}
}
