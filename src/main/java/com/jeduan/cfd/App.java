package com.jeduan.cfd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import org.apache.commons.io.IOUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.jeduan.cfd.cli.Comando;
import com.jeduan.cfd.cli.OptionsParser;
import com.jeduan.cfd.util.StatusLlave;

public class App 
{
    public static void main( String[] args )
    {
    	
        OptionsParser opts = new OptionsParser();
        Comando comando;
        try {
        	
			@SuppressWarnings("unused")
			JCommander jcom = new JCommander(opts, args);
            if (null == opts.cadenaOriginal) 
            	comando = Comando.VALIDAR;
            else
            	comando = Comando.SELLAR;
            
			InputStream privateKey = new FileInputStream(opts.privateKey);
			InputStream certificate = new FileInputStream(opts.certificate);
			
			byte[] key = IOUtils.toByteArray(privateKey);
			byte[] cer = IOUtils.toByteArray(certificate);
	        
	        StatusLlave statusLlave = LlaveService.INSTANCE.validate(key, cer, opts.password);
	        if (StatusLlave.SUCCESS != statusLlave) {
	        	//Saliendo, no se puede hacer mucho
	        	System.out.println("Hubo un error con las llaves privadas");
	        	System.out.println(statusLlave.mensaje());
        		System.exit(1);
	        }

	        if (Comando.VALIDAR == comando) {
	        	//Se pidio validar y se cumplio.
        		System.out.println("La llave privada y el certificado son validos");
        		System.exit(0);
	        } else if (Comando.SELLAR == comando) {
	        	InputStream cadena = new FileInputStream(opts.cadenaOriginal);
	        	String cadenaLeida = IOUtils.toString(cadena, "UTF-8");
	        	cadenaLeida = cadenaLeida.trim();
	        	try {
					String sello = LlaveService.INSTANCE.sign(cadenaLeida, key, opts.password);
					System.out.println(sello);
					System.exit(0);
				} catch (GeneralSecurityException e) {
					//Ignorando. Ya sabemos que la llave privada es valida
				}
	        }
        } catch (ParameterException e) {
        	JCommander rescue = new JCommander(opts);
        	rescue.usage();
		} catch (FileNotFoundException e) {
			System.out.println("El archivo no se pudo encontrar");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

}

