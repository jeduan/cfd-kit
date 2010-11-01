package com.jeduan.cfd;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.ssl.PKCS8Key;
import org.apache.commons.ssl.ProbablyNotPKCS8Exception;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import com.jeduan.cfd.util.StatusLlave;

public enum LlaveService {
	INSTANCE;
	public static final String algorithm = "MD5withRSA";


	private LlaveService() {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * Metodo helper. Se encarga de convertir la cadena a utf-8 y regresarla
	 * codificada
	 * 
	 * @param toSign
	 *            La cadena a firmar
	 * @param keyArray
	 *            El array de bytes de la llave privada
	 * @param password
	 *            El password de la llave privada
	 * @return La cadena de entrada, firmada con la llave privada y codificada
	 *         en base64
	 * @throws ProbablyNotPKCS8Exception
	 * @throws SignatureException
	 * @throws GeneralSecurityException
	 */
	public String sign(String toSign, byte[] keyArray, String password)
			throws ProbablyNotPKCS8Exception, SignatureException,
			GeneralSecurityException {
		try {
			byte[] signed = sign(toSign.getBytes("UTF-8"), keyArray, password);
			return encode(signed);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * El metodo que firma las llaves
	 * 
	 * @param toSign
	 *            Un array de bytes codficado en utf que contiene los metodos a
	 *            firmar
	 * @param keyArray
	 *            Un array de bytes que contiene la llave privada
	 * @param password
	 *            El password the la llave privada
	 * @return Un array de bytes firmados por la llave privada
	 * @throws ProbablyNotPKCS8Exception
	 * @throws UnsupportedEncodingException
	 * @throws SignatureException
	 * @throws GeneralSecurityException
	 */
	private byte[] sign(byte[] toSign, byte[] keyArray, String password)
			throws ProbablyNotPKCS8Exception, UnsupportedEncodingException,
			SignatureException, GeneralSecurityException {
		try {
			PKCS8Key pkcs8Key = new PKCS8Key(keyArray, password.toCharArray());
			PrivateKey privateKey = pkcs8Key.getPrivateKey();

			Signature sig = Signature.getInstance(algorithm);
			sig.initSign(privateKey);
			sig.update(toSign);
			return sig.sign();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * Codifica un array de bytes en base64 y los regresa como cadena
	 * 
	 * @param signed
	 *            El array de bytes a codificar
	 * @return Una cadena codificada en base64
	 */
	private String encode(byte[] signed) {
		byte[] encode = Base64.encode(signed);
		return new String(encode);
	}

	/**
	 * Verifica una cadena con respecto al texto de referencia
	 * @param plaintext El texto de referencia
	 * @param ciphered El texto cifrado por una llave privada
	 * @param certificate Los bytes que contienen el certificado de la llave privada
	 * @return La llave que verifica si el certificado corresponde a la llave privada
	 * @throws CertificateException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	private boolean verify(String plaintext, byte[] ciphered, byte[] certificate)
			throws CertificateException, InvalidKeyException, SignatureException {
		X509Certificate cert = X509Certificate.getInstance(certificate);
		cert.checkValidity();
		
		Signature signature;
		try {
			signature = Signature.getInstance(algorithm);
			signature.initVerify(cert.getPublicKey());
			signature.update(plaintext.getBytes("UTF-8"));
			return signature.verify(ciphered);
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}
	
	/**
	 * Valida el certificado digital, la clave privada, asi como su relacion
	 * @param clavePrivada 
	 * @param password 
	 * @param certificado 
	 * 
	 * @return - SUCCESS si todo esta correcto 
	 *         - ERROR_PRIVATE_KEY si el password de la clave privada no es correcto 
	 *         - ERROR_PRIVATE_KEY si la clave privada no es un archivo que corresponda al estandar PKCS8 
	 *         - ERROR_PUBLIC_KEY si el certificado no es un archivo que corresponda al estandar PKCS10 
	 *         - ERROR_PUBLIC_PRIVATE_KEY si el certificado no corresponde a la clave privada
	 */
	public StatusLlave validate(final byte[] clavePrivada, final byte[] certificado, final String password) {
		final String textoAFirmar = "||2.0|FDF|28125|2007-09-12T12:47:31|11160|2007|ingreso|Pago en una sola exhibicion|"
			+ "TERMINOS CONTADO ESP|3674.13|4225.25|IMM9304016Z4|Ingram Micro Mexico S.A. de C.V.|Laguna de Terminos|249|"
			+ "Anahuac|Miguel Hidalgo|Distrito Federal|Mexico|11320|Av. 16 de Septiembre|225|San MartinXochinahuac|"
			+ "Azcapotzalco|Distrito Federal|Mexico|02140|CAOG8406274R0|CHAVEZ OCHOA GABRIEL|HDA. DE CORLOME NO. 51|"
			+ "COL. FLORESTA COYOACAN|DELG. TLALPAN|MEXICO, D.F. MX 14310|MX|3.00|"
			+ "TONER NEGRO P/LASERJET SUPL 2420 (6,000 PAG )|1189.04|3567.12|1.00|COMISION TARJETA DECREDITO|"
			+ "107.01|107.01|IVA|15.00|551.12|551.12||";
		
		try {
			byte[] firmado = sign(textoAFirmar.getBytes("UTF-8"), clavePrivada, password);
			boolean verify = verify(textoAFirmar, firmado, certificado);
			
			if (verify) {
				return StatusLlave.SUCCESS;
			} else {
				return StatusLlave.ERROR_PUBLIC_PRIVATE_KEY;
			}
		} catch (ProbablyNotPKCS8Exception e) {
			return StatusLlave.ERROR_PRIVATE_KEY;
		} catch (UnsupportedEncodingException e) {
			return StatusLlave.ERROR_PRIVATE_KEY;
		} catch (SignatureException e) {
			return StatusLlave.ERROR_PRIVATE_KEY;
		} catch (GeneralSecurityException e) {
			return StatusLlave.ERROR_PASSWORD;
		} catch (CertificateException e) {
			return StatusLlave.ERROR_PUBLIC_KEY;
		}
	}
	
	public CertificateData getCertificateData(byte[] certificate) {
		X509Certificate cert;
		try {
			cert = X509Certificate.getInstance(certificate);
			cert.checkValidity();
			return CertificateData.of(cert);
		} catch (CertificateException e) {
			//logger.warn("Hubo un error validando el certificado " + e.getMessage());
			return null;
		}
	}
}
