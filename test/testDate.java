
import java.math.BigDecimal;
import java.util.Date;
import util.DateUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lotfi
 */
public class testDate {

    public static void main(String[] args) {
//        String date = "01/04/2018";
//        System.out.println("ha date 1 : " + DateUtil.getSqlDateToSaveInDB(date));
//        Date dateAfter = DateUtil.subDayFromDate(DateUtil.getSqlDateToSaveInDB(date));
//
//        System.out.println("after substract : " + dateAfter);
//        System.out.println("after sub with format : " + DateUtil.getSqlDate(dateAfter));
        BigDecimal test = new BigDecimal(10);
        BigDecimal test2 = new BigDecimal(1);
        
        System.out.println("comparaison : "+test.compareTo(test2));
    }
}
