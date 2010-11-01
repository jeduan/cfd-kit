package com.jeduan.cfd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.jeduan.cfd.util.KeyUtils;
import com.jeduan.cfd.util.StatusLlave;

public class Llave {

	public final StatusLlave statusLlave;
	public final String keyPass;
	public final String cerNumber;
	public final String cerIssuer;
	public final String cerSubject;
	public final Date cerValidFrom;
	public final Date cerValidTo;

	private Llave(Builder b) {
		this.statusLlave = b.statusLlave;
		this.keyPass = b.keyPass;
		this.cerNumber = b.cerNumber;
		this.cerIssuer = b.cerIssuer;
		this.cerSubject = b.cerSubject;
		this.cerValidFrom = b.cerValidFrom;
		this.cerValidTo = b.cerValidTo;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Serie " + cerNumber);
		sb.append(" valido desde " + cerValidFrom + " hasta " + cerValidTo);
		return sb.toString();
	}

	public static class Builder {
		private StatusLlave statusLlave;
		private String keyPass;
		private String cerNumber;
		private String cerIssuer;
		private Date cerValidFrom;
		private String cerSubject;
		private Date cerValidTo;

		public Builder(InputStream key, String keyPass, InputStream cer)
				throws IOException {

			this.statusLlave = KeyUtils.validate(key, cer, keyPass);

			this.keyPass = keyPass;
			
			byte[] cerArray = IOUtils.toByteArray(cer);
			CertificateData data = LlaveService.INSTANCE.getCertificateData(cerArray);

			this.cerNumber = data.noCertificado;
			this.cerIssuer = data.issuer;
			this.cerSubject = data.subject;
			this.cerValidFrom = data.from;
			this.cerValidTo = data.to;
		}
		
		public Llave build() {
			return new Llave(this);
		}
	}
}