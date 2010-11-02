package com.jeduan.cfd.util;

public enum StatusLlave {
	NIL,
	SUCCESS, ERROR_PASSWORD, ERROR_PRIVATE_KEY, ERROR_PUBLIC_KEY, ERROR_PUBLIC_PRIVATE_KEY;

	public String mensaje() {
		switch (this) {
		case SUCCESS:
			return "Se subio correctamente la llave";
		case ERROR_PASSWORD:
			return "el password de la clave privada no es correcto";
		case ERROR_PRIVATE_KEY:
			return "La llave privada no es un archivo valido";
		case ERROR_PUBLIC_KEY:
			return "El certificado no es un archivo valido";
		case ERROR_PUBLIC_PRIVATE_KEY:
			return "El certificado y la llave privada no corresponden";
		default:
			return "Hubo un error con las llaves";
		}
	}
}
