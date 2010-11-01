package com.jeduan.cfd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.jeduan.cfd.cli.Comando;
import com.jeduan.cfd.cli.OptionsParser;
import com.jeduan.cfd.util.KeyUtils;
import com.jeduan.cfd.util.StatusLlave;

public class App 
{
    public static void main( String[] args )
    {
    	
        OptionsParser opts = new OptionsParser();
        Comando comando;
        JCommander jcom = null;
        
        try {
        	
			jcom = new JCommander(opts, args);
            if (null == opts.cadenaOriginal) 
            	comando = Comando.VALIDAR;
            else
            	comando = Comando.SELLAR;
            
			InputStream privateKey = new FileInputStream(opts.privateKey);
			InputStream certificate = new FileInputStream(opts.certificate);
	        
	        StatusLlave statusLlave = KeyUtils.validate(privateKey, certificate, opts.password);
	        if (Comando.VALIDAR == comando) {
	        	if (StatusLlave.SUCCESS == statusLlave) {
	        		System.out.println("La llave privada y el certificado son validos");
	        	}
	        }
        } catch (ParameterException e) {
        	System.out.println(e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

}

