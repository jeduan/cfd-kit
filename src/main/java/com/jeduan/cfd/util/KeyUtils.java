package com.jeduan.cfd.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.jeduan.cfd.LlaveService;

public class KeyUtils {
	private KeyUtils() {}
	
	public static StatusLlave validate(final InputStream key, final InputStream cer, final String keyPass) throws IOException {
		byte[] cerArray = IOUtils.toByteArray(cer);
		byte[] keyArray = IOUtils.toByteArray(key);

		StatusLlave statusLlave = LlaveService.INSTANCE.validate(keyArray,	cerArray, keyPass);
		if (StatusLlave.SUCCESS != statusLlave) {
			throw new IllegalArgumentException("La llave fue rechazada, motivo: "
							+ statusLlave.toString());
		}
		return statusLlave;
	}
}
