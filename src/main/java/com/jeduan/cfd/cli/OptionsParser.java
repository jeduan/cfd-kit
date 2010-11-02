package com.jeduan.cfd.cli;

import java.io.File;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class OptionsParser {
    @Parameter
    public List<String> parameters = Lists.newArrayList();
      
    @Parameter(names = {"-k", "--key"}, description = "Llave privada", required = true)
    public File privateKey;

    @Parameter(names = {"-c", "--cer"}, description = "Certificado", required = true)
    public File certificate;
    
    @Parameter(names = {"-p", "--pass"}, description = "Contraseña de la llave privada", required = true)
    public String password;
    
    @Parameter(names = {"-i", "--cadena"}, description = "Cadena Original")
    public File cadenaOriginal;
    
    @Parameter(names = "--version")
    public String version = "2.0";
    
}
