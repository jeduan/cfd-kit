package mx.gob.sat.cfd;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    public BigDecimal unmarshal(String value) throws Exception {
        if(value == null){
            return null;
        }
        BigDecimal decimal = new BigDecimal(value);
        return decimal;
    }

    public String marshal(BigDecimal value) throws Exception {
    	String val = null;
        if (value != null && value.floatValue() > 0) {
        	BigDecimal decimal = value.setScale(2, RoundingMode.HALF_UP);
        	val = decimal.toPlainString();
        }
        return val;
    }
}
