package com.jeduan.cfd;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jeduan.cfd.util.DigestUtils;

public class AlgorithmsTest {
		
	@BeforeClass
	public void setUp() {
	}

	@Test
	public void testProviderExists() {
		String algorithm = LlaveService.algorithm;
		boolean found = false;

		Provider[] providers = Security.getProviders();
		for (Provider provider : providers) {
			for (Object o : provider.keySet()) {
				String entry = (String) o;
				if (entry.contains(algorithm)) {
					found = true;
				}
			}
		}
		Assert.assertTrue(found);
	}

	@Test
	public void testAlgorithmExists() {
		try {
			Signature signature = Signature.getInstance(LlaveService.algorithm);
			Assert.assertEquals(signature.getAlgorithm(),
					LlaveService.algorithm);
		} catch (NoSuchAlgorithmException e) {
			Assert.fail("Hubo NoSuchAlgorithmException");
		}
	}

	@Test
	public void testHash() {
		final String hashed = "4cd8ed248d7a02314c50778a37d1522d";
		final String cadenaOriginal = "||2.0|A|1|2009-08-16T16:30:00|1|2009|ingreso|Una sola exhibición|350.00|" +
				"5.25|397.25|ISP900909Q88|Industrias del Sur Poniente, S.A. de C.V.|Alvaro Obregón|37|3|Col. Roma Norte|" +
				"México|Cuauhtémoc|Distrito Federal|México|06700|Pino Suarez|23|Centro|Monterrey|Monterrey|Nuevo Léon|" +
				"México|95460|CAUR390312S87|Rosa María Calderón Uriegas|Topochico|52|Jardines del Valle|Monterrey|" +
				"Monterrey|Nuevo León|México|95465|10|Caja|Vasos decorados|20.00|200|1|pieza|Charola metálica|" +
				"150.00|150|IVA|15.00|52.50||";
		try {
			byte[] utf8 = cadenaOriginal.getBytes("UTF-8");
			String hash = DigestUtils.md5(utf8);

			Assert.assertEquals(hashed, hash);
		} catch (UnsupportedEncodingException e) {
			Assert.fail();
		}
	}

}
