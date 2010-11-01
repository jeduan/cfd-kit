package com.jeduan.cfd.util;

public enum StatusLlave {
	NIL,
	SUCCESS, ERROR_PASSWORD, ERROR_PRIVATE_KEY, ERROR_PUBLIC_KEY, ERROR_PUBLIC_PRIVATE_KEY;

	public String mensaje() {
		switch (this) {
		case SUCCESS:
			return "Se subio correctamente la llave";
		default:
			return "Hubo un error con las llaves";
		}
	}
}
