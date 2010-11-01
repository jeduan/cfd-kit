package com.jeduan.cfd;

import java.math.BigInteger;
import java.util.Date;

import javax.security.cert.CertificateEncodingException;
import javax.security.cert.X509Certificate;

import org.bouncycastle.util.encoders.Base64;

import com.jeduan.cfd.util.DigestUtils;

//TODO Agregar enum de estado y que lance excepciones si no es aceptado
public class CertificateData {
	public final String noCertificado;
	public final String certificado;
	public final String issuer;
	public final String subject;
	public final Date from;
	public final Date to;
	public final String sha1;

	private CertificateData(X509Certificate cert) throws CertificateEncodingException {
		this.noCertificado = calcularNoCertificado(cert.getSerialNumber());
		this.sha1 = DigestUtils.sha1(cert.getEncoded());
		this.certificado = new String(Base64.encode(cert.getEncoded()));
		this.subject = cert.getSubjectDN().getName();
		this.issuer = cert.getIssuerDN().getName();
		this.from = cert.getNotBefore();
		this.to = cert.getNotAfter();
	}

	public static CertificateData of(X509Certificate certificate)
			throws CertificateEncodingException {
		return new CertificateData(certificate);
	}

	private String calcularNoCertificado(BigInteger noCertificado) {
		String hex = noCertificado.toString(16);

		StringBuilder certNumber = new StringBuilder();
		for (int i = 0; i < hex.length(); i++) {
			if (i % 2 == 1) {
				certNumber.append(hex.charAt(i));
			}
		}

		return certNumber.toString();
	}

}
