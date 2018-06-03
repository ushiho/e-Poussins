
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
        String date = "03/06/2018";
        Date dateAfter = DateUtil.subDayFromDate(DateUtil.getSqlDateToSaveInDB(date));

        System.out.println("after substract : " + dateAfter);
        System.out.println("after sub with format : " + DateUtil.getSqlDate(dateAfter));
        System.out.println("num week from "+DateUtil.weekNumberFromDate(dateAfter));

          
    }
}
