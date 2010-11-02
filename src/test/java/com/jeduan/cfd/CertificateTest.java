package com.jeduan.cfd;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jeduan.cfd.util.StatusLlave;

public class CertificateTest {
	private final String keyPassword = "a0123456789";
	private final String cadenaOriginal = "||2.0|A|1|2009-08-16T16:30:00|1|2009|ingreso|Una sola exhibición|350.00|" +
	"5.25|397.25|ISP900909Q88|Industrias del Sur Poniente, S.A. de C.V.|Alvaro Obregón|37|3|Col. Roma Norte|" +
	"México|Cuauhtémoc|Distrito Federal|México|06700|Pino Suarez|23|Centro|Monterrey|Monterrey|Nuevo Léon|" +
	"México|95460|CAUR390312S87|Rosa María Calderón Uriegas|Topochico|52|Jardines del Valle|Monterrey|" +
	"Monterrey|Nuevo León|México|95465|10|Caja|Vasos decorados|20.00|200|1|pieza|Charola metálica|" +
	"150.00|150|IVA|15.00|52.50||";
	
	private byte[] cerArray;
	private byte[] keyArray;
	
	@BeforeClass
	public void openStreams() {
		String cerLocation = "aaa010101aaa_csd_01.cer";
		String keyLocation = "aaa010101aaa_csd_01.key";
		
		try {
			InputStream key = getClass().getClassLoader().getResourceAsStream(keyLocation);
			keyArray = IOUtils.toByteArray(key);
			key.close();
			
			InputStream cer = getClass().getClassLoader().getResourceAsStream(cerLocation);
			cerArray = IOUtils.toByteArray(cer);
			cer.close();
		} catch (IOException e) {}
	}

	@Test
	public void testFilesExist() {
		Assert.assertNotNull(cerArray);
		Assert.assertFalse(cerArray.length == 0);
		
		Assert.assertNotNull(keyArray);
		Assert.assertFalse(keyArray.length == 0);
	}
	
	@Test
	public void testCertificateReadsOk() {
		X509Certificate cert;
		final String certB64 = "MIIE/TCCA+WgAwIBAgIUMzAwMDEwMDAwMDAxMDAwMDA4MDAwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0"
			+ "FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMDA3MzAxNjU4NDBaFw0xMjA3MjkxNjU4NDBaMIGWMRIwEAYDVQQDDAlNYXRyaXogU0ExEjAQBgNVBCkMCU1hdHJpeiBTQTESMBAGA1UECgwJTWF0cml6IFNBMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBBQUFBMDEwMTAxQUFBMR4wHAYDVQQFExUgLyBBQUFBMDEwMTAxSERGUlhYMDExETAPBgNVBAsMCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD0ltQNthUNUfzq0t1GpIyapjzOn1W5fGM5G/pQyMluCzP9YlVAgBjGgzwYp9Z0J9gadg3y2ZrYDwvv8b72goyRnhnv3bkjVRKlus6LDc00K7Jl23UYzNGlXn5+i0Hxxu"
			+ "Wonc2GYKFGsN4rFWKVy3Fnpv8Z2D7dNqsVyT5HapEqwIDAQABo4HqMIHnMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMB0GA1UdDgQWBBSYodSwRczzj5H7mcO3+mAyXz+y0DAuBgNVHR8EJzAlMCOgIaAfhh1odHRwOi8vcGtpLnNhdC5nb2IubXgvc2F0LmNybDAzBggrBgEFBQcBAQQnMCUwIwYIKwYBBQUHMAGGF2h0dHA6Ly9vY3NwLnNhdC5nb2IubXgvMB8GA1UdIwQYMBaAFOtZfQQimlONnnEaoFiWKfU54KDFMBAGA1UdIAQJMAcwBQYDKgMEMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBBQUAA4IBAQArHQEorApwqumSn5EqDOAjbezi8fLco1cYES/PD+LQRM1Vb1g7VLE3hR4S5NNBv0bMwwWAr0WfL9lRRj0PMKLorO8y4TJjRU8MiYXfzSu"
			+ "KYL5Z16kW8zlVHw7CtmjhfjoIMwjQo3prifWxFv7VpfIBstKKShU0qB6KzUUNwg2Ola4t4gg2JJcBmyIAIInHSGoeinR2V1tQ10aRqJdXkGin4WZ75yMbQH4L0NfotqY6bpF2CqIY3aogQyJGhUJji4gYnS2DvHcyoICwgawshjSaX8Y0Xlwnuh6EusqhqlhTgwPNAPrKIXCmOWtqjlDhho/lhkHJMzuTn8AoVapbBUnj";
		final String noCertificado ="30001000000100000800" ;
		final String sha1Certificado = "ece2e2728ad785fc26896bfc564520e900c87ca7";
	
		try {
			cert = X509Certificate.getInstance(cerArray);
			CertificateData cdata = CertificateData.of(cert);
			Assert.assertEquals(cdata.noCertificado, noCertificado );
			Assert.assertEquals(cdata.sha1, sha1Certificado	);
			Assert.assertEquals(cdata.certificado, certB64);
		} catch (CertificateException e) {
			Assert.fail(e.getMessage());
		} 
	}
	
	@Test
	public void testCertificateAndKeyOpen() {
		StatusLlave validate = LlaveService.INSTANCE.validate(keyArray, cerArray, keyPassword);
		Assert.assertEquals(StatusLlave.SUCCESS, validate);
	}
	
	@Test
	public void testKeyFailsOnWrongPassword() {
		StatusLlave statusLlave = LlaveService.INSTANCE.validate(keyArray,	cerArray, "Mal password");
		Assert.assertEquals(StatusLlave.ERROR_PASSWORD, statusLlave);
	}
	
	@Test
	public void testSign() {
		final String expected = "lUye/aLZBJt3AQVyS8pd4PG81i5+vUc7iwRluTxj7ib3o2LWNbE8Jzj7yLwKHSdNhUAn8u" +
				"A1LwNTeMpLzEjPp5+ARRhxfPXKIJj+Jee+0uPcYwjOjsrOniHPtkHUvl5KRjgS6pUvoOpUpTZ0E+hZMofoi83d9TkwJTfBFMNDzj4=";
		try {
			String sign = LlaveService.INSTANCE.sign(cadenaOriginal, keyArray, keyPassword);
			Assert.assertEquals(sign, expected);
		} catch (GeneralSecurityException e) {
			Assert.fail(e.getMessage());
		}
		
	}
}
